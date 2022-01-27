package com.epam.jwd.fitness_center.controller.command;

public class PlainCommandResponse implements CommandResponse {
    private final boolean redirect;
    private final boolean ajax;
    private final String ajaxData;
    private final String path;

    public PlainCommandResponse(String path) {
        this(false, path);
    }

    public PlainCommandResponse(boolean redirect, String path) {
        this(redirect, path, false, path);
    }

    public PlainCommandResponse(boolean redirect, String path, boolean ajax, String ajaxData) {
        this.redirect = redirect;
        this.ajax = ajax;
        this.path = path;
        this.ajaxData = ajaxData;
    }

    public static PlainCommandResponse createAjaxResponse(String data) {
        return new PlainCommandResponse(false, null, true, data);
    }

    @Override
    public boolean isRedirect() {
        return redirect;
    }

    @Override
    public boolean isAjax() {
        return ajax;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getAjaxData() {
        return ajaxData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlainCommandResponse that = (PlainCommandResponse) o;

        if (redirect != that.redirect) return false;
        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        int result = (redirect ? 1 : 0);
        result = 31 * result + path.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlainCommandResponse{");
        sb.append("redirect=").append(redirect);
        sb.append(", path='").append(path).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
