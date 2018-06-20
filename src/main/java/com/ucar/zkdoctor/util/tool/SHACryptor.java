package com.ucar.zkdoctor.util.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Description: SHA密码器，用于密码加密
 * Created on 2018/4/25 20:14
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class SHACryptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SHACryptor.class);

    /**
     * 加密算法
     */
    public static final String KEY_SHA = "SHA";

    /**
     * 对给定字符串进行加密
     *
     * @param src 待加密字符串
     * @return
     */
    public static String encode(String src) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);
            messageDigest.update(src.getBytes("utf-8"));
            BigInteger sha = new BigInteger(messageDigest.digest());
            return sha.toString();
        } catch (Exception e) {
            LOGGER.error("Encode src {} failed.", src, e);
            throw new RuntimeException("加密错误！");
        }
    }
}
