package com.pago.dotodo.model.elements;

import java.util.Map;
import java.util.TreeMap;

public class MenuItem {
    private TreeMap<String, String> properties;

    public MenuItem() {
        properties = new TreeMap<>();
    }

    public MenuItem add(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public String get(String key) {
        return properties.get(key);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public MenuItem addProperties(TreeMap<String, String> properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "properties=" + properties +
                '}';
    }
}
