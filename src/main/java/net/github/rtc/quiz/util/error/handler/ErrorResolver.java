package net.github.rtc.quiz.util.error.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A {@code RestErrorResolver} resolves an exception and produces a {@link ErrorInfo} instance that can be used
 * to render a Rest error representation to the response body.
 */
@Component
public class ErrorResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorResolver.class);
    private static final Map<Class<? extends Throwable>, HttpStatus> exceptionStatusMap;

    static {
        exceptionStatusMap = new HashMap<>();
        //400
        exceptionStatusMap.put(HttpMessageNotReadableException.class,  HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(MissingServletRequestParameterException.class,  HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(TypeMismatchException.class,  HttpStatus.BAD_REQUEST);
        // 404
        exceptionStatusMap.put(NoSuchRequestHandlingMethodException.class,  HttpStatus.NOT_FOUND);
        // 405
        exceptionStatusMap.put(HttpRequestMethodNotSupportedException.class,  HttpStatus.METHOD_NOT_ALLOWED);
        // 406
        exceptionStatusMap.put(HttpMediaTypeNotAcceptableException.class,   HttpStatus.NOT_ACCEPTABLE);
        // 415
        exceptionStatusMap.put(HttpMediaTypeNotSupportedException.class,   HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        //500
        exceptionStatusMap.put(Throwable.class,  HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Autowired
    private ErrorInfoFactory errorInfoFactory;

    public ErrorResolver() {
    }

    /**
     * Returns a {@code ErrorInfo} instance to render as the response body based on the given exception.
     *
     * @param ex the exception that was thrown during handler execution
     * @return a resolved {@code ErrorInfo} instance to render as the response body or <code>null</code> for default
     * processing
     */
    public ErrorInfo resolveError(Exception ex) {
        return getRestErrorTemplate(ex);
    }

    /**
     * Returns closest ErrorInfo for current exception
     *
     * @param ex handled exception
     * @return closest ErrorInfo
     */
    private ErrorInfo getRestErrorTemplate(Exception ex) {
        Map<Class<? extends Throwable>, HttpStatus> mappings = this.exceptionStatusMap;
        Class dominantMapping = Throwable.class;
        int deepest = Integer.MAX_VALUE;
        for (Map.Entry<Class<? extends Throwable>, HttpStatus> entry : mappings.entrySet()) {
            int depth = getDepth(entry.getKey(), ex.getClass(), 0);
            if (depth >= 0 && depth < deepest) {
                deepest = depth;
                dominantMapping = entry.getKey();
            }
        }
        return errorInfoFactory.buildErrorInfoFromTemplate(mappings.get(dominantMapping));
    }

    /**
     * Return the depth to the superclass matching.
     * 0 means ex matches exactly. Returns -1 if there's no match.
     * Otherwise, returns depth. Lowest depth wins.
     */
    protected int getDepth(Class<? extends Throwable> mappedExceptionClass,
                            Class receivedExceptionClass, int depth) {
        if (mappedExceptionClass.getName().equals(receivedExceptionClass.getClass().getName())) {
            return depth;
        }
        if (receivedExceptionClass.equals(Throwable.class)) {
            return -1;
        }
        return getDepth(mappedExceptionClass, receivedExceptionClass.getSuperclass(), depth + 1);
    }

}
