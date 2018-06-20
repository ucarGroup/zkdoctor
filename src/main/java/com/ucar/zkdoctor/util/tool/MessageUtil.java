package com.ucar.zkdoctor.util.tool;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description: 消息发送工具，包括邮件和短信。需自行定义
 * Created on 2018/3/8 11:35
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MessageUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageUtil.class);

    /**
     * 发送短信
     *
     * @param content    短信内容
     * @param mobileList 手机号列表
     */
    public static void sendSMS(String content, List<String> mobileList) {
        if (CollectionUtils.isEmpty(mobileList)) {
            LOGGER.info("Send sms:{} but mail phone list is empty.", content);
            return;
        }
        for (String phone : mobileList) {
            sendSMS(content, phone);
        }
    }

    /**
     * 发送短信
     *
     * @param content 短信内容
     * @param mobile  手机号
     */
    public static void sendSMS(String content, String mobile) {
        // TODO 自行实现短信发送
        LOGGER.info("Sent sms to {}. the content is {}.", mobile, content);
    }

    /**
     * 发送邮件
     *
     * @param title            邮件标题
     * @param content          邮件内容
     * @param mailAddressLists 邮箱列表
     */
    public static void sendMail(String title, String content, List<String> mailAddressLists) {
        if (CollectionUtils.isEmpty(mailAddressLists)) {
            LOGGER.info("Send mail {}:{} but mail address list is empty.", title, content);
            return;

        }
        for (String mailAddress : mailAddressLists) {
            sendMail(title, content, mailAddress);
        }
    }

    /**
     * 发送邮件
     *
     * @param title       邮件标题
     * @param content     邮件内容
     * @param mailAddress 邮箱地址
     */
    public static void sendMail(String title, String content, String mailAddress) {
        // TODO 自行实现邮件发送
        LOGGER.info("Sent mail to {}. the title is {}, content is {}.", mailAddress, title, content);
    }
}
