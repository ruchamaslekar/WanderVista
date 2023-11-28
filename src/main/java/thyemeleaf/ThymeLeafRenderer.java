package thyemeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.PrintWriter;

/** This class helps to render to HTML templates using Thymeleaf */
public class ThymeLeafRenderer {

    private final TemplateEngine templateEngine;
    private final Context commonContext;

    public ThymeLeafRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.commonContext = new Context();
    }

    public void setVariable(String key, Object value) {
        commonContext.setVariable(key, value);
    }

    public void render(String templateName, PrintWriter writer) {
        templateEngine.process(templateName, commonContext, writer);
    }
}