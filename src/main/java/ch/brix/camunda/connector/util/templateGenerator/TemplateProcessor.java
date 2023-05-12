package ch.brix.camunda.connector.util.templateGenerator;

import ch.brix.camunda.connector.util.templateGenerator.schema.Template;

/**
 * To be able to modify the schema generated from the annotations before the JSON is created.
 * E.g. to add other special properties. Requires no-args constructor.
 */
public interface TemplateProcessor {
    /**
     * @param template the template generated from the annotations
     */
    void process(Template template);
}
