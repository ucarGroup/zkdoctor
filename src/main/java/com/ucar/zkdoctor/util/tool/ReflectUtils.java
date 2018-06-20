package com.ucar.zkdoctor.util.tool;

import com.ucar.zkdoctor.service.monitor.MonitorBase;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description: 反射工具类
 * Created on 2018/1/29 19:50
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ReflectUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectUtils.class);

    /**
     * 通过反射赋值
     *
     * @param object  需要赋值的对象
     * @param attrMap 数据信息
     * @param <T>
     */
    public static <T> void assignAttrs(T object, Map<String, String> attrMap) {
        for (String key : attrMap.keySet()) {
            try {
                Field field = object.getClass().getDeclaredField(key);
                String originValue = attrMap.get(key);
                if (field != null && originValue != null) {
                    String fieldType = field.getGenericType().toString();
                    Object value = originValue;
                    if (fieldType.equals("class java.lang.Integer")) {
                        value = Integer.valueOf(originValue);
                    } else if (fieldType.equals("class java.lang.Long")) {
                        value = Long.valueOf(originValue);
                    }
                    String setMethodName = getSetMethodName(key);
                    Method method = object.getClass().getDeclaredMethod(setMethodName, field.getType());
                    if (method != null) {
                        method.invoke(object, value);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("通过反射赋值异常", e);
            }
        }
    }

    /**
     * 获取set打头的方法名称
     *
     * @param name 名称
     * @return
     */
    private static String getSetMethodName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return "set" + name;
    }

    /**
     * 获取所有监控类
     * 监控类要求：
     * 1、为MonitorBase子类
     * 2、位于com.ucar.zkdoctor.service.monitor.detail包下
     * 3、重写monitor()与init()方法
     * 即可
     *
     * @param basePackage 指定包
     * @return
     */
    public static Set<Class<? extends MonitorBase>> getMonitorClassesSet(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getSubTypesOf(MonitorBase.class);
    }

    /**
     * 加载指定类的所有公有field
     *
     * @param clazz     类
     * @param fieldList field列表
     */
    public static void getPublicFields(Class<?> clazz, List<Field> fieldList) {
        Collections.addAll(fieldList, clazz.getFields());
        if (clazz.getSuperclass() != null) {
            getPublicFields(clazz.getSuperclass(), fieldList);
        }
    }

    /**
     * 设置指定类的field值
     *
     * @param clazz 类
     * @param field field
     * @param value 值
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setField(Class<?> clazz, Field field, Object value)
            throws IllegalArgumentException, IllegalAccessException {
        Class<?> fieldType = field.getType();
        if (int.class.equals(fieldType)) {
            field.setInt(clazz, Integer.parseInt(String.valueOf(value)));
        } else if (boolean.class.equals(fieldType)) {
            field.setBoolean(clazz, Boolean.parseBoolean(String.valueOf(value)));
        } else if (byte.class.equals(fieldType)) {
            field.setByte(clazz, Byte.parseByte(String.valueOf(value)));
        } else if (double.class.equals(fieldType)) {
            field.setDouble(clazz, Double.parseDouble(String.valueOf(value)));
        } else if (float.class.equals(fieldType)) {
            field.setFloat(clazz, Float.parseFloat(String.valueOf(value)));
        } else if (long.class.equals(fieldType)) {
            field.setLong(clazz, Long.parseLong(String.valueOf(value)));
        } else if (short.class.equals(fieldType)) {
            field.setShort(clazz, Short.parseShort(String.valueOf(value)));
        } else if (char.class.equals(fieldType) && value instanceof Character) {
            field.setChar(clazz, (Character) value);
        } else {
            field.set(clazz, value);
        }
    }

    /**
     * 根据类的filed名称，获取该类中对应的field对象
     *
     * @param clazz     类
     * @param fieldName filed名称
     * @return
     * @throws SecurityException
     */
    public static Field getField(Class<?> clazz, String fieldName) throws SecurityException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            LOGGER.info("No such filed in clazz {}, try to find in it's super class.");
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), fieldName);
            } else {
                LOGGER.info("No such filed in clazz {}, and there is no super class.");
                return null;
            }
        }
    }

}
