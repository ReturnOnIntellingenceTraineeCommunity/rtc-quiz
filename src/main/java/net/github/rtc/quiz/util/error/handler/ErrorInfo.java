package net.github.rtc.quiz.util.error.handler;

import org.springframework.http.HttpStatus;

public class ErrorInfo {
    private final HttpStatus status;
    private final int code;
    private final String message;
    private final String developerMessage;
    private final String moreInfoUrl;
    private final Throwable throwable;

    public ErrorInfo(HttpStatus status, int code, String message, String developerMessage, String moreInfoUrl, Throwable throwable) {
        if (status == null) {
            throw new NullPointerException("HttpStatus argument cannot be null.");
        }
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
        this.moreInfoUrl = moreInfoUrl;
        this.throwable = throwable;
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

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }

    public Throwable getThrowable() {
        return throwable;
    }


    public static class Builder {
        private HttpStatus status;
        private int code;
        private String message;
        private String developerMessage;
        private String moreInfoUrl;
        private Throwable throwable;
        public Builder() {
        }
        public Builder setStatus(int statusCode) {
            this.status = HttpStatus.valueOf(statusCode);
            return this;
        }
        public Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }
        public Builder setCode(int code) {
            this.code = code;
            return this;
        }
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
        public Builder setDeveloperMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }
        public Builder setMoreInfoUrl(String moreInfoUrl) {
            this.moreInfoUrl = moreInfoUrl;
            return this;
        }
        public Builder setThrowable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }
        public ErrorInfo build() {
            if (this.status == null) {
                this.status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return new ErrorInfo(this.status, this.code, this.message, this.developerMessage, this.moreInfoUrl, this.throwable);
        }
    }
}
