package com.ltmap.halobiosmaintain.common.utils.excel_import;

import java.util.*;

/**
 * <p>Title:Context</p>
 * <p>Description:配置上下文 </p>
 * <p>Company: 天人环境</p>
 * @author    Will Tong
 * @version   v1.0
 */
public class Context {

    private Map<String, String> parameters;

    public Context() {
        parameters = Collections.synchronizedMap(new HashMap<String, String>());
    }

    public Context(Properties properties) {
        this();
        this.putAll(toMap(properties));
    }

    public Context(Map<String, String> paramters) {
        this();
        this.putAll(paramters);
    }

    public Map<String, String> getParameters() {
        synchronized (parameters) {
            Map<String, String> maps = new HashMap<String, String>();
            for (String key : parameters.keySet()) {
                maps.put(key, parameters.get(key));
            }
            return maps;
        }
    }

    public void clear() {
        parameters.clear();
    }

    public Map<String, String> getSubProperties(String prefix) {
        Map<String, String> result = new HashMap<String, String>();
        synchronized (parameters) {
            for (String key : parameters.keySet()) {
                if (key.startsWith(prefix)) {
                    String name = key.substring(prefix.length());
                    result.put(name, parameters.get(key));
                }
            }
        }
        return result;
    }

    public Map<String, Context> getSubContexts(String key) {
        Map<String, Context> result = new HashMap<String, Context>();
        String value = parameters.get(key);
        if (null != value) {
            String[] values = value.trim().split("\\s+");
            for (String val : values) {
                Context context = new Context(getSubProperties(key + "." + val + "."));
                result.put(val, context);
            }
        }
        return result;
    }

    public void putAll(Map<String, String> map) {
        parameters.putAll(map);
    }

    public void put(String key, String value) {
        parameters.put(key, value);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = get(key);
        if (value != null) {
            return Boolean.parseBoolean(value.trim());
        }
        return defaultValue;
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        String value = get(key);
        if (value != null) {
            return Integer.parseInt(value.trim());
        }
        return defaultValue;
    }

    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Long getLong(String key, Long defaultValue) {
        String value = get(key);
        if (value != null) {
            return Long.parseLong(value.trim());
        }
        return defaultValue;
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public String getString(String key, String defaultValue) {
        return get(key, defaultValue);
    }

    public String getString(String key) {
        return get(key);
    }

    private String get(String key, String defaultValue) {
        String result = parameters.get(key);
        if (result != null) {
            return result;
        }
        return defaultValue;
    }

    private String get(String key) {
        return get(key, null);
    }

    private Map<String, String> toMap(Properties properties) {
        Map<String, String> result = new HashMap<String, String>();
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String name = (String) propertyNames.nextElement();
            String value = properties.getProperty(name);
            result.put(name, value);
        }
        return result;
    }

    @Override
    public String toString() {
        return "{ parameters:" + parameters + " }";
    }

}

