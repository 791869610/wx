package TemplateMessage.entity;

public class TemplateEntity {
    public String template_id;
    public String content;

    public TemplateEntity(String template_id, String content) {
        this.template_id = template_id;
        this.content = content;
    }

    public TemplateEntity() {

    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
