package com.ucar.zkdoctor.util.tool;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Description: hessian序列化工具
 * Created on 2018/2/5 9:31
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class HessianSerializerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HessianSerializerUtils.class);

    /**
     * 将对象序列化为字节数组
     *
     * @param obj 待序列化对象
     * @return
     */
    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        AbstractHessianOutput out = new Hessian2Output(ops);
        out.setSerializerFactory(new SerializerFactory());
        try {
            out.writeObject(obj);
            out.close();
        } catch (IOException e) {
            LOGGER.error("Hessian serialize failed.", e);
            throw new RuntimeException("Hessian serialize failed");
        }
        return ops.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes 待反序列化字节数组
     * @return
     */
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream ips = new ByteArrayInputStream(bytes);
        AbstractHessianInput in = new Hessian2Input(ips);
        in.setSerializerFactory(new SerializerFactory());
        Object value;
        try {
            value = in.readObject();
            in.close();
        } catch (IOException e) {
            LOGGER.error("Hessian deserialize failed", e);
            throw new RuntimeException("Hessian deserialize failed");
        }
        return value;
    }
}
