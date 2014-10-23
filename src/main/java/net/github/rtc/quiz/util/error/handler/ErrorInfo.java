package net.github.rtc.quiz.util.error.handler;

import org.springframework.http.HttpStatus;

public class ErrorInfo {
    private final HttpStatus status;
    private final int code;
    private final String message;
    private final String moreInfoUrl;

    public ErrorInfo(HttpStatus status, int code, String message, String moreInfoUrl) {
        if (status == null) {
            throw new NullPointerException("HttpStatus argument cannot be null.");
        }
        this.status = status;
        this.code = code;
        this.message = message;
        this.moreInfoUrl = moreInfoUrl;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }
}
