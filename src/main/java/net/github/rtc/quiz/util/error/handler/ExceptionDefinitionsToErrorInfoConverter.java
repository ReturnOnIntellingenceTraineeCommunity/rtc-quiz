package net.github.rtc.quiz.util.error.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ExceptionDefinitionsToErrorInfoConverter {

    public Map<String, ErrorInfo> toRestErrors(Map<String, String> smap) {
        if (CollectionUtils.isEmpty(smap)) {
            return Collections.emptyMap();
        }
        Map<String, ErrorInfo> map = new LinkedHashMap<>(smap.size());
        for (Map.Entry<String, String> entry : smap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            ErrorInfo template = toRestError(value);
            map.put(key, template);
        }
        return map;
    }

    private ErrorInfo toRestError(String exceptionConfig) {
        String[] values = StringUtils.commaDelimitedListToStringArray(exceptionConfig);
        if (values == null || values.length == 0) {
            throw new IllegalStateException("Invalid config mapping. Exception names must map to a string configuration.");
        }
        ErrorInfo.Builder builder = new ErrorInfo.Builder();
        boolean statusSet = false;
        boolean codeSet = false;
        boolean msgSet = false;
        boolean devMsgSet = false;
        boolean moreInfoSet = false;
        for (String value : values) {
            String trimmedVal = StringUtils.trimWhitespace(value);
            //check to see if the value is an explicitly named key/value pair:
            String[] pair = StringUtils.split(trimmedVal, "=");
            if (pair != null) {
                //explicit attribute set:
                String pairKey = StringUtils.trimWhitespace(pair[0]);
                if (!StringUtils.hasText(pairKey)) {
                    pairKey = null;
                }
                String pairValue = StringUtils.trimWhitespace(pair[1]);
                if (!StringUtils.hasText(pairValue)) {
                    pairValue = null;
                }
                if ("status".equalsIgnoreCase(pairKey)) {
                    int statusCode = getRequiredInt(pairKey, pairValue);
                    builder.setStatus(statusCode);
                    statusSet = true;
                } else if ("code".equalsIgnoreCase(pairKey)) {
                    int code = getRequiredInt(pairKey, pairValue);
                    builder.setCode(code);
                    codeSet = true;
                } else if ("msg".equalsIgnoreCase(pairKey)) {
                    builder.setMessage(pairValue);
                    msgSet = true;
                } else if ("devMsg".equalsIgnoreCase(pairKey)) {
                    builder.setDeveloperMessage(pairValue);
                    devMsgSet = true;
                } else if ("infoUrl".equalsIgnoreCase(pairKey)) {
                    builder.setMoreInfoUrl(pairValue);
                    moreInfoSet = true;
                }
            } else {
                //not a key/value pair - use heuristics to determine what value is being set:
                int val;
                if (!statusSet) {
                    val = getInt("status", trimmedVal);
                    if (val > 0) {
                        builder.setStatus(val);
                        statusSet = true;
                        continue;
                    }
                }
                if (!codeSet) {
                    val = getInt("code", trimmedVal);
                    if (val > 0) {
                        builder.setCode(val);
                        codeSet = true;
                        continue;
                    }
                }
                if (!moreInfoSet && trimmedVal.toLowerCase().startsWith("http")) {
                    builder.setMoreInfoUrl(trimmedVal);
                    moreInfoSet = true;
                    continue;
                }
                if (!msgSet) {
                    builder.setMessage(trimmedVal);
                    msgSet = true;
                    continue;
                }
                if (!devMsgSet) {
                    builder.setDeveloperMessage(trimmedVal);
                    devMsgSet = true;
                    continue;
                }
                if (!moreInfoSet) {
                    builder.setMoreInfoUrl(trimmedVal);
                    moreInfoSet = true;
                    //noinspection UnnecessaryContinue
                    continue;
                }
            }
        }
        return builder.build();
    }
    private static int getRequiredInt(String key, String value) {
        try {
            int anInt = Integer.valueOf(value);
            return Math.max(-1, anInt);
        } catch (NumberFormatException e) {
            String msg = "Configuration element '" + key + "' requires an integer value. The value " +
              "specified: " + value;
            throw new IllegalArgumentException(msg, e);
        }
    }
    private static int getInt(String key, String value) {
        try {
            return getRequiredInt(key, value);
        } catch (IllegalArgumentException iae) {
            return 0;
        }
    }
}
