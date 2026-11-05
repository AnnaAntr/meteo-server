package ru.antanna.meteoserver.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    public static final String HEADER_NAME = "X-Api-Key";

    private final byte[] expectedKey;

    public ApiKeyInterceptor(@Value("${app.api-key:}") String expectedKey) {
        this.expectedKey = expectedKey.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // ключ проверяется только при POST-запросе
        if (!HttpMethod.POST.matches(request.getMethod())) {
            return true;
        }

        String inputKey = request.getHeader(HEADER_NAME);
        if (inputKey == null || expectedKey.length == 0
                || !MessageDigest.isEqual(expectedKey, inputKey.getBytes(StandardCharsets.UTF_8))) {
            
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}
