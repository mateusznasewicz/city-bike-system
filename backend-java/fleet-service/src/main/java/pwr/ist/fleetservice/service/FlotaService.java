package pwr.ist.fleetservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pwr.ist.fleetservice.events.LockBikeCommand;
import pwr.ist.fleetservice.events.UnlockBikeCommand;
import tools.jackson.databind.ObjectMapper;


@Service
@RequiredArgsConstructor
@Slf4j
public class FlotaService {
    private final MqttGateway mqttGateway;
    private final ObjectMapper objectMapper;

    public void unlockBike(UnlockBikeCommand cmd) {
        log.info("Wysyłam sygnał otwarcia do roweru: {}", cmd.bikeId());
        String payload = objectMapper.writeValueAsString(cmd);
        mqttGateway.sendToMqtt(payload, "cmd/unlock");
    }

    public void lockBike(LockBikeCommand cmd) {
        log.info("Wysyłam sygnał zamkniecia do roweru: {}", cmd.bikeId());
        String payload = objectMapper.writeValueAsString(cmd);
        mqttGateway.sendToMqtt(payload, "cmd/lock");
    }
}
