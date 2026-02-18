package com.example.releasetracker.config;

import com.example.releasetracker.domain.enums.ReleaseStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, ReleaseStatus.class, ReleaseStatus::fromDisplayName);
    }
}

