package com.ltmap.halobiosmaintain.common.utils.excel_import;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Stack;

/**
 * Created by User on 2014/6/30.
 */
public abstract class ContextUtil {

    private static final Logger logger = LoggerFactory.getLogger(ContextUtil.class);

    private static final String XML_FILE_EXTENSION = ".xml";

    public static Context GetContext(String resourceName) {
        Context context = null;
        BufferedReader bufferedReader = null;
        try {
            Properties properties = GetProperty(resourceName);
            context = new Context(properties);
        } catch (IOException e) {
            logger.error("不能载入文件: " + resourceName + " (I/O 失败) - 异常跟踪.", e);
        } finally {
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.warn("不能关闭文件读取: " + resourceName, e);
                }
            }
        }
        return context;
    }

    public static Properties GetProperty(String resourceName) throws IOException {
        return GetProperty(resourceName, null);
    }

    public static Properties GetProperty(String resourceName, ClassLoader classLoader) throws IOException {
        //Assert.notNull(resourceName, "Resource name must not be null");
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = GetDefaultClassLoader();
        }
        Enumeration<URL> urls = (classLoaderToUse != null ? classLoaderToUse.getResources(resourceName) :
                ClassLoader.getSystemResources(resourceName));
        Properties props = new Properties();
        Stack<URL> stack = new Stack<URL>();
        while (urls.hasMoreElements()) {
            stack.push(urls.nextElement());
        }
        while (!stack.isEmpty()) {
            URL url = stack.pop();
            URLConnection con = url.openConnection();
            UseCachesIfNecessary(con);
            InputStream is = con.getInputStream();
            try {
                if (resourceName != null && resourceName.endsWith(XML_FILE_EXTENSION)) {
                    props.loadFromXML(is);
                }
                else {
                    props.load(is);
                }
            }
            finally {
                is.close();
            }
        }
        return props;
    }

    public static ClassLoader GetDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ContextUtil.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public static void UseCachesIfNecessary(URLConnection con) {
        con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
    }
}

