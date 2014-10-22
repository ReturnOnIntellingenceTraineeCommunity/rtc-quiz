package net.github.rtc.quiz.util.error.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;

/**
 * A {@code RestErrorResolver} resolves an exception and produces a {@link ErrorInfo} instance that can be used
 * to render a Rest error representation to the response body.
 */
@Component
public class ErrorResolver implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorResolver.class);

    /*A set of mapping definitions to resolve the exception to a ErrorInfo instance

    The entry key is any String that might appear in a fully qualified class name of an Exception
    (or any of the Exception’s super classes).

    The entry value is a ErrorInfo definition as a comma-delimited String. The string is parsed using heuristics to
    determine how to build a ErrorInfo instance.*/
    private Map<String, String> exceptionMappingDefinitions = Collections.emptyMap();

    /*A set of pre built ErrorInfo templates to resolve the exception to a ErrorInfo instance

    The entry key is any String that might appear in a fully qualified class name of an Exception
    (or any of the Exception’s super classes).

    The entry value is an ErrorInfo instance which is a config-time template for the specific ErrorInfo.*/
    private Map<String, ErrorInfo> exceptionMappings = Collections.emptyMap();

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

    /**
     * Returns a {@code ErrorInfo} instance to render as the response body based on the given exception.
     *
     * @param ex the exception that was thrown during handler execution
     * @return a resolved {@code ErrorInfo} instance to render as the response body or <code>null</code> for default
     * processing
     */
    public ErrorInfo resolveError(Exception ex) {
        ErrorInfo template = getRestErrorTemplate(ex);
        if (template == null) {
            return null;
        }
        return errorInfoFactory.buildErrorInfoFromTemplate(template, ex);
    }

    /**
     * Returns the config-time 'template' ErrorInfo instance configured for the specified Exception, or
     * {@code null} if a match was not found.
     * <p/>
     * The config-time template is used as the basis for the ErrorInfo constructed at runtime.
     *
     * @param ex
     * @return the template to use for the RestError instance to be constructed.
     */
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

    /**
     * Return the depth to the superclass matching.
     * <p>0 means ex matches exactly. Returns -1 if there's no match.
     * Otherwise, returns depth. Lowest depth wins.
     */
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
