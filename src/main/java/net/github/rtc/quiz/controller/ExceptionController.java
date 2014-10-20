package net.github.rtc.quiz.controller;

import net.github.rtc.quiz.util.error.handler.ErrorInfo;
import net.github.rtc.quiz.util.error.handler.ErrorResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    private ErrorResolver errorResolver;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> databaseError(HttpServletRequest req, Exception exception) {
        LOGGER.error("Request raised " + exception.getClass());
        ErrorInfo error = errorResolver.resolveError(exception);
        return new ResponseEntity<>(error, error.getStatus());
    }
}
