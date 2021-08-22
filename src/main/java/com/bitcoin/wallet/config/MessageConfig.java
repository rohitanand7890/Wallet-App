package com.bitcoin.wallet.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import javax.annotation.Resource;

@Configuration
@AllArgsConstructor
public class MessageConfig {
    @Resource
    private final Environment env;
    public String getMessage(final String key, final Object... args) {
        String format = this.env.getProperty(key);
        if (format == null) {
            throw new IllegalArgumentException(this.env.getProperty("messages.errors_resource_not_found", "Specified message"));
        }
        return String.format(format, args);
    }
}