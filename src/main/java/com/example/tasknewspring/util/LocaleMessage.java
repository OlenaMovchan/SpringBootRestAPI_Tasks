package com.example.tasknewspring.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocaleMessage {

    private final MessageSource messageSource;

    public String getLocaleMessage(String msgCode, Locale locale, Object... args) {

        return messageSource.getMessage(msgCode, args, locale);
    }
}
