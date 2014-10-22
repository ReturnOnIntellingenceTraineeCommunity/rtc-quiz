package net.github.rtc.quiz.util.error.handler;

import org.springframework.stereotype.Component;

/**
 * Creates a specific error info instance from ErrorInfo template and exception
 */
@Component
public class ErrorInfoFactory {
    public static final String DEFAULT_EXCEPTION_MESSAGE_VALUE = "_exmsg";
    public static final String DEFAULT_MESSAGE_VALUE = "_msg";

    private String defaultMoreInfoUrl;
    private boolean defaultEmptyCodeToStatus;
    private String defaultDeveloperMessage;

    public ErrorInfoFactory() {
        this.defaultEmptyCodeToStatus = true;
        this.defaultDeveloperMessage = DEFAULT_EXCEPTION_MESSAGE_VALUE;
    }

    public void setDefaultMoreInfoUrl(String defaultMoreInfoUrl) {
        this.defaultMoreInfoUrl = defaultMoreInfoUrl;
    }

    public void setDefaultEmptyCodeToStatus(boolean defaultEmptyCodeToStatus) {
        this.defaultEmptyCodeToStatus = defaultEmptyCodeToStatus;
    }

    public void setDefaultDeveloperMessage(String defaultDeveloperMessage) {
        this.defaultDeveloperMessage = defaultDeveloperMessage;
    }

    /**
     * Creates a specific ErrorInfo instance from ErrorInfo template and exception
     *
     * @param template template to build specific ErrorInfo instance from
     * @param ex received exception from handler
     * @return specific ErrorInfo instance
     */
    public ErrorInfo buildErrorInfoFromTemplate(ErrorInfo template, Exception ex) {
        ErrorInfo.Builder builder = new ErrorInfo.Builder();
        builder.setStatus(getStatusValue(template));
        builder.setCode(getCode(template));
        builder.setMoreInfoUrl(getMoreInfoUrl(template));
        String msg = getMessage(template, ex);
        if (msg != null) {
            builder.setMessage(msg);
        }
        msg = getDeveloperMessage(template, ex);
        if (msg != null) {
            builder.setDeveloperMessage(msg);
        }
        return builder.build();
    }


    protected int getStatusValue(ErrorInfo template) {
        return template.getStatus().value();
    }

    protected int getCode(ErrorInfo template) {
        int code = template.getCode();
        if (code <= 0 && defaultEmptyCodeToStatus) {
            code = template.getStatus().value();
        }
        return code;
    }

    protected String getMoreInfoUrl(ErrorInfo template) {
        String moreInfoUrl = template.getMoreInfoUrl();
        if (moreInfoUrl == null) {
            moreInfoUrl = this.defaultMoreInfoUrl;
        }
        return moreInfoUrl;
    }

    protected String getMessage(ErrorInfo template, Exception ex) {
        return getMessage(template.getMessage(), ex);
    }

    protected String getDeveloperMessage(ErrorInfo template, Exception ex) {
        String devMsg = template.getDeveloperMessage();
        if (devMsg == null && defaultDeveloperMessage != null) {
            devMsg = defaultDeveloperMessage;
        }
        if (DEFAULT_MESSAGE_VALUE.equals(devMsg)) {
            devMsg = template.getMessage();
        }
        return getMessage(devMsg, ex);
    }

    /**
     * Returns the response status message to return to the client, or {@code null} if no
     * status message should be returned.
     *
     * @return the response status message to return to the client, or {@code null} if no
     * status message should be returned.
     */
    protected String getMessage(String msg, Exception ex) {
        if (msg != null) {
            if (msg.equalsIgnoreCase("null") || msg.equalsIgnoreCase("off")) {
                return null;
            }
            if (msg.equalsIgnoreCase(DEFAULT_EXCEPTION_MESSAGE_VALUE)) {
                msg = ex.getMessage();
            }
            //ToDo: add message source with messages
        }
        return msg;
    }


}
