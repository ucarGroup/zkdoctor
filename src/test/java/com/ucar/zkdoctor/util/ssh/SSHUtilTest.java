package com.ucar.zkdoctor.util.ssh;

import org.junit.Test;

import java.util.Map;

/**
 * Description: SSH工具类测试
 * Created on 2018/2/22 10:21
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class SSHUtilTest {

    @Test
    public void collectMachineState() throws Exception {
        Map<String, String> result = SSHUtil.collectMachineState("127.0.0.1");
        if (result != null) {
            for (Map.Entry<String, String> entry : result.entrySet()) {
                System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
            }
        }
    }

}