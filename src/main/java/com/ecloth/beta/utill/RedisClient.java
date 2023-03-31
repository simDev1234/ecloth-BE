package com.ecloth.beta.utill;

import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisClient {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public <T> boolean put(String key, T data) {
        try {
            String value = objectMapper.writeValueAsString(data);
            log.info(value);
            redisTemplate.opsForValue().set(parseToUTF8String(key), parseToUTF8String(value));
            return true;
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> log.warn(x.toString()));
            return false;
        }
    }

    public <T> Optional<T> get(String key, Class<T> classType) {
        try {
            String value = redisTemplate.opsForValue().get(parseToUTF8String(key));
            return StringUtils.isNullOrEmpty(value)?
                    Optional.empty() : Optional.of(objectMapper.readValue(value, classType));
        } catch (Exception e) {
            e.getStackTrace();
            Arrays.stream(e.getStackTrace()).forEach(x -> log.warn(x.toString()));
            return Optional.empty();
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    private String parseToUTF8String(String s) {
        return new String(s.getBytes(), StandardCharsets.UTF_8);
    }

}

