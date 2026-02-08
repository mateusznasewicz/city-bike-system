package pwr.ist.fleetservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import pwr.ist.fleetservice.events.BikeLockedEvent;
import pwr.ist.fleetservice.events.BikeUnlockedEvent;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class FleetMqttListener {

    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(String event, @Header(MqttHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Odebrano MQTT. Topic: {}", topic);
        if(topic.equals("status/locked")){
            BikeLockedEvent e = objectMapper.readValue(event, BikeLockedEvent.class);
            kafkaProducer.sendBikeLocked(e);
        }else{
            BikeUnlockedEvent e = objectMapper.readValue(event, BikeUnlockedEvent.class);
            kafkaProducer.sendBikeUnlocked(e);
        }
    }
}
