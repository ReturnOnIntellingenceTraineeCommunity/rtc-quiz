package net.github.rtc.quiz.template;

import freemarker.cache.TemplateLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.List;

public class HtmlFreeMarkerConfigurer extends FreeMarkerConfigurer {

    @Override
    protected TemplateLoader getAggregateTemplateLoader(
      final List<TemplateLoader> templateLoaders) {
        logger.info("Using HtmlTemplateLoader to enforce HTML-safe content");
        return new HtmlTemplateLoader(
          super.getAggregateTemplateLoader(templateLoaders));
    }
}
