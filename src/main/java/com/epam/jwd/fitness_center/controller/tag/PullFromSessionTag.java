package com.epam.jwd.fitness_center.controller.tag;

import javax.servlet.jsp.tagext.TagSupport;

public class PullFromSessionTag extends TagSupport {
    private String attribute;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public int doStartTag() {
        Object value = this.pageContext.getSession().getAttribute(attribute);
        if(value != null) {
            this.pageContext.setAttribute(name, value);
            this.pageContext.getSession().removeAttribute(attribute);
        }
        return SKIP_BODY;
    }
}
