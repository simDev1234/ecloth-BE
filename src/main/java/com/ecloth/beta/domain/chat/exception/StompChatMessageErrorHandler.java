package com.ecloth.beta.domain.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class StompChatMessageErrorHandler {

    @MessageExceptionHandler(MessageConversionException.class)
    public ResponseEntity<String> handleConversionException(MessageConversionException ex) {
        return new ResponseEntity<>("Error converting message: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @MessageExceptionHandler(MessageDeliveryException.class)
    public ResponseEntity<String> handleMessageDeliveryException(MessageDeliveryException ex) {
        return new ResponseEntity<>("Error delivering message: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
