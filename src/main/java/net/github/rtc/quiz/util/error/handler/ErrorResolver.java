package net.github.rtc.quiz.util.error.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Collections;
import java.util.Map;

@Component
public class ErrorResolver implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorResolver.class);
    private Map<String, ErrorInfo> exceptionMappings = Collections.emptyMap();
    private Map<String, String> exceptionMappingDefinitions = Collections.emptyMap();

    @Autowired
    private ExceptionMappingDefinitionsFactory exceptionMappingDefinitionsFactory;
    @Autowired
    private ExceptionDefinitionsToErrorInfoConverter exceptionDefinitionsToRestErrorConverter;
    @Autowired
    private ErrorInfoFactory errorInfoFactory;

    public ErrorResolver() {
    }

    public void setDefaultMoreInfoUrl(String defaultMoreInfoUrl) {
        errorInfoFactory.setDefaultMoreInfoUrl(defaultMoreInfoUrl);
    }
    public void setDefaultEmptyCodeToStatus(boolean defaultEmptyCodeToStatus) {
        errorInfoFactory.setDefaultEmptyCodeToStatus(defaultEmptyCodeToStatus);
    }
    public void setDefaultDeveloperMessage(String defaultDeveloperMessage) {
        errorInfoFactory.setDefaultDeveloperMessage(defaultDeveloperMessage);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, String> definitions = exceptionMappingDefinitionsFactory.createDefaultExceptionMappingDefinitions();
        if (this.exceptionMappingDefinitions != null && !this.exceptionMappingDefinitions.isEmpty()) {
            definitions.putAll(this.exceptionMappingDefinitions);
        }
        this.exceptionMappings = exceptionDefinitionsToRestErrorConverter.toRestErrors(definitions);
    }

    public ErrorInfo resolveError(Exception ex) {
        ErrorInfo template = getRestErrorTemplate(ex);
        if (template == null) {
            return null;
        }
        return errorInfoFactory.buildErrorInfoFromTemplate(template, ex);
    }

    private ErrorInfo getRestErrorTemplate(Exception ex) {
        Map<String, ErrorInfo> mappings = this.exceptionMappings;
        if (CollectionUtils.isEmpty(mappings)) {
            return null;
        }
        ErrorInfo template = null;
        String dominantMapping = null;
        int deepest = Integer.MAX_VALUE;
        for (Map.Entry<String, ErrorInfo> entry : mappings.entrySet()) {
            String key = entry.getKey();
            int depth = getDepth(key, ex);
            if (depth >= 0 && depth < deepest) {
                deepest = depth;
                dominantMapping = key;
                template = entry.getValue();
            }
        }
        if (template != null && LOGGER.isDebugEnabled()) {
            LOGGER.debug(
              "Resolving to RestError template '" + template + "' for exception of type [" + ex.getClass().getName() 
                + "], based on exception mapping [" + dominantMapping + "]");
        }
        return template;
    }

    protected int getDepth(String exceptionMapping, Exception ex) {
        return getDepth(exceptionMapping, ex.getClass(), 0);
    }

    private int getDepth(String exceptionMapping, Class exceptionClass, int depth) {
        if (exceptionClass.getName().contains(exceptionMapping)) {
            // Found it!
            return depth;
        }
        // If we've gone as far as we can go and haven't found it...
        if (exceptionClass.equals(Throwable.class)) {
            return -1;
        }
        return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
    }

}
