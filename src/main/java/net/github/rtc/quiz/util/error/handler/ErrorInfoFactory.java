package net.github.rtc.quiz.util.error.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Creates a specific error info instance from ErrorInfo template and exception
 */
@Component
public class ErrorInfoFactory {

    @Value("${info.url}")
    private static String MORE_INFO_URL;

    /**
     * Creates a specific ErrorInfo from Http status
     *
     * @return specific ErrorInfo instance
     */
    public ErrorInfo buildErrorInfoFromTemplate(HttpStatus status) {
        return new ErrorInfo(status, status.value(), status.getReasonPhrase(), MORE_INFO_URL);
    }
}
