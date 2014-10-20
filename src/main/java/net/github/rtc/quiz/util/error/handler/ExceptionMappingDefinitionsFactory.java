package net.github.rtc.quiz.util.error.handler;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ExceptionMappingDefinitionsFactory {
    public static final String DEFAULT_EXCEPTION_MESSAGE_VALUE = "_exmsg";

    //ToDo add more exceptions
    public final Map<String,String> createDefaultExceptionMappingDefinitions() {
        Map<String,String> m = new LinkedHashMap<>();
        // 400
        applyDef(m, HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
        applyDef(m, MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST);
        applyDef(m, TypeMismatchException.class, HttpStatus.BAD_REQUEST);
        applyDef(m, "javax.validation.ValidationException", HttpStatus.BAD_REQUEST);
        // 404
        applyDef(m, NoSuchRequestHandlingMethodException.class, HttpStatus.NOT_FOUND);
        applyDef(m, "org.hibernate.ObjectNotFoundException", HttpStatus.NOT_FOUND);
        // 405
        applyDef(m, HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED);
        // 406
        applyDef(m, HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE);
        // 409
        //can't use the class directly here as it may not be an available dependency:
        applyDef(m, "org.springframework.dao.DataIntegrityViolationException", HttpStatus.CONFLICT);
        // 415
        applyDef(m, HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        return m;
    }

    private void applyDef(Map<String,String> m, Class clazz, HttpStatus status) {
        applyDef(m, clazz.getName(), status);
    }
    private void applyDef(Map<String,String> m, String key, HttpStatus status) {
        m.put(key, definitionFor(status));
    }
    private String definitionFor(HttpStatus status) {
        return status.value() + ", " + DEFAULT_EXCEPTION_MESSAGE_VALUE;
    }
}
