package de.lasse.duden.database;

public class FilterObj {

    private String name, parameterName;
    private Object parameter;

    public FilterObj(String name, String parameterName, Object parameter) {
        this.name = name;
        this.parameterName = parameterName;
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Object getParameter() {
        return parameter;
    }
}
