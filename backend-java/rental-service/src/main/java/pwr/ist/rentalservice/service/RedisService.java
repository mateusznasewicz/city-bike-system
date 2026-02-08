package pwr.ist.rentalservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pwr.ist.rentalservice.enums.StatusRoweru;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void saveBikeStatus(String bikeId, StatusRoweru status){
        redisTemplate.opsForValue().set(bikeId, status.toString());
    }

    public StatusRoweru getBikeStatus(String bikeId){
        String status = redisTemplate.opsForValue().get(bikeId);

        return Optional.ofNullable(status)
                .map(StatusRoweru::valueOf)
                .orElseGet(() -> {
                    StatusRoweru defaultStatus = StatusRoweru.AVAILABLE;
                    saveBikeStatus(bikeId, defaultStatus);
                    return defaultStatus;
                });
    }
}
