package com.epam.jwd.fitness_center.controller.tag;

import javax.servlet.jsp.tagext.TagSupport;

public class PullFromSessionTag extends TagSupport {
    private String attribute;
    private String msg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public int doStartTag() {
        Object value = this.pageContext.getSession().getAttribute(attribute);
        //todo instanceof
        if(value != null) {
            this.pageContext.setAttribute(msg, (String) value);
            this.pageContext.getSession().removeAttribute(attribute);
        }
        return SKIP_BODY;
    }
}
