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

/**
 * A class to build default mappings for Spring-specific exceptions
 */
@Component
public class ExceptionMappingDefinitionsFactory {
    public static final String DEFAULT_EXCEPTION_MESSAGE_VALUE = "_exmsg";

    /**
     * Build default exception mappings
     * @return  default exception mappings
     */
    public final Map<String, String> createDefaultExceptionMappingDefinitions() {
        Map<String, String> exceptionMappingDefinitions = new LinkedHashMap<>();
        // 400
        applyDef(exceptionMappingDefinitions, HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
        applyDef(exceptionMappingDefinitions, MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST);
        applyDef(exceptionMappingDefinitions, TypeMismatchException.class, HttpStatus.BAD_REQUEST);
        applyDef(exceptionMappingDefinitions, "javax.validation.ValidationException", HttpStatus.BAD_REQUEST);
        // 404
        applyDef(exceptionMappingDefinitions, NoSuchRequestHandlingMethodException.class, HttpStatus.NOT_FOUND);
        applyDef(exceptionMappingDefinitions, NoSuchRequestHandlingMethodException.class, HttpStatus.NOT_FOUND);
        applyDef(exceptionMappingDefinitions, "org.hibernate.ObjectNotFoundException", HttpStatus.NOT_FOUND);
        // 405
        applyDef(exceptionMappingDefinitions, HttpRequestMethodNotSupportedException.class,
          HttpStatus.METHOD_NOT_ALLOWED);
        // 406
        applyDef(exceptionMappingDefinitions, HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE);
        // 409
        //can't use the class directly here as it may not be an available dependency:
        applyDef(exceptionMappingDefinitions, "org.springframework.dao.DataIntegrityViolationException",
          HttpStatus.CONFLICT);
        // 415
        applyDef(exceptionMappingDefinitions, HttpMediaTypeNotSupportedException.class,
          HttpStatus.UNSUPPORTED_MEDIA_TYPE);

        applyDef(exceptionMappingDefinitions, HttpMediaTypeNotSupportedException.class,
          HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        //500
        applyDef(exceptionMappingDefinitions, Throwable.class, HttpStatus.INTERNAL_SERVER_ERROR);
        return exceptionMappingDefinitions;
    }

    private void applyDef(Map<String, String> exceptionMappingDefinitions, Class clazz, HttpStatus status) {
        applyDef(exceptionMappingDefinitions, clazz.getName(), status);
    }

    private void applyDef(Map<String, String> exceptionMappingDefinitions, String key, HttpStatus status) {
        exceptionMappingDefinitions.put(key, definitionFor(status));
    }

    private String definitionFor(HttpStatus status) {
        return status.value() + ", " + DEFAULT_EXCEPTION_MESSAGE_VALUE;
    }
}
