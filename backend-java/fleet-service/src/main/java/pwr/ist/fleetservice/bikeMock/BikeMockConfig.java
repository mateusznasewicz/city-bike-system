package pwr.ist.fleetservice.bikeMock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.ChannelInterceptor;
import pwr.ist.fleetservice.events.BikeLockedEvent;
import pwr.ist.fleetservice.events.BikeUnlockedEvent;
import pwr.ist.fleetservice.events.LockBikeCommand;
import pwr.ist.fleetservice.events.UnlockBikeCommand;
import pwr.ist.fleetservice.service.MqttGateway;
import tools.jackson.databind.ObjectMapper;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BikeMockConfig {
    private final ObjectMapper objectMapper;
    private final MqttGateway mqttGateway;

    @Bean
    public MessageChannel bikeCommandsChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer bikeInboundAdapter(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("BikeSim_Listener_Fixed", mqttClientFactory, "cmd/unlock", "cmd/lock");

        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(bikeCommandsChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "bikeCommandsChannel")
    public MessageHandler bikeCommandHandler() {
        return message -> {
            log.info("🚴 [HANDLER] Otrzymano: {}", message.getPayload());
            log.info("🚴 [HANDLER] Headers: {}", message.getHeaders());
            String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC, String.class);
            if(topic.equals("cmd/unlock")){
                this.unlockBike(message);
            }else{
                this.lockBike(message);
            }
        };
    }

    private void unlockBike(Message<?> message){
        UnlockBikeCommand cmd = objectMapper.readValue(
                message.getPayload().toString(),
                UnlockBikeCommand.class
        );
        log.info("Odblokowanie roweru: {}", cmd.bikeId());

        BikeUnlockedEvent event = new BikeUnlockedEvent(cmd.userId(), cmd.bikeId(), cmd.wypozyczenieId(), true);
        String response = objectMapper.writeValueAsString(event);
        mqttGateway.sendToMqtt(response, "status/unlocked");
    }

    private void lockBike(Message<?> message){
        LockBikeCommand cmd = objectMapper.readValue(
                message.getPayload().toString(),
                LockBikeCommand.class
        );
        log.info("Zablokowanie roweru: {}", cmd.bikeId());

        BikeLockedEvent event = new BikeLockedEvent(cmd.userId(), cmd.bikeId(), cmd.wypozyczenieId(), true);
        String response = objectMapper.writeValueAsString(event);
        mqttGateway.sendToMqtt(response, "status/locked");
    }
}
