package com.ucar.zkdoctor.util.tool;

import org.junit.Test;

import java.io.Serializable;

/**
 * Description: 序列化测试类
 * Created on 2018/2/5 9:50
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class HessianSerializerUtilsTest implements Serializable {

    private static final long serialVersionUID = 6510936416683102059L;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HessianSerializerUtilsTest{" +
                "id=" + id +
                '}';
    }

    @Test
    public void serialize() throws Exception {
        HessianSerializerUtilsTest object = new HessianSerializerUtilsTest();
        object.setId(100);
        byte[] data = HessianSerializerUtils.serialize(object);
        System.out.println(HessianSerializerUtils.deserialize(data));
    }

}