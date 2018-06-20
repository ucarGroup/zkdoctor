package com.ucar.zkdoctor.util.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 配置文件转化工具类
 * Created on 2018/1/30 9:26
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class PropertiesReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesReader.class);

    /**
     * 缓存的配置文件信息
     */
    private static final Map<String, Properties> PROPERTIES_MAP = new ConcurrentHashMap<String, Properties>();

    /**
     * 读取配置文件
     *
     * @param propertiesName 配置文件名称
     * @return
     */
    public static Properties getProperties(String propertiesName) {
        return getProperties(propertiesName, true);
    }

    /**
     * 读取配置文件
     *
     * @param propertiesName 配置文件名称
     * @param isCache        配置是否进行缓存
     * @return
     */
    public static Properties getProperties(String propertiesName, boolean isCache) {
        Properties properties = PROPERTIES_MAP.get(propertiesName);
        if (!PROPERTIES_MAP.containsKey(propertiesName)) {
            synchronized (PROPERTIES_MAP) {
                if (!PROPERTIES_MAP.containsKey(propertiesName)) {
                    properties = createProperties(propertiesName);
                    if (isCache && properties != null) {
                        PROPERTIES_MAP.put(propertiesName, properties);
                    }
                }
            }
        }
        return properties;
    }

    /**
     * 转化配置文件信息
     *
     * @param propertiesName 配置文件名称
     * @return
     */
    private static Properties createProperties(String propertiesName) {
        InputStream fis = null;
        Properties properties = null;
        try {
            fis = PropertiesReader.class.getResourceAsStream("/" + propertiesName + ".properties");
            if (fis != null) {
                properties = new Properties();
                properties.load(fis);
            }
        } catch (Exception e) {
            LOGGER.error("Create properties : {}.properties failed", propertiesName, e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.warn("Close properties input stream : {}.properties failed.", propertiesName, e);
                }
            }
        }
        return properties;
    }
}
