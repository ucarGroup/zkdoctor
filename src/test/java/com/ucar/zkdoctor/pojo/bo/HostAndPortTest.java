package com.ucar.zkdoctor.pojo.bo;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import java.util.List;

/**
 * Description: 工具类测试
 * Created on 2018/1/15 14:59
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class HostAndPortTest {
    @Test
    public void testTransformToHostAndPort() throws Exception {
        HostAndPort hostAndPort = HostAndPort.transformToHostAndPort("127.0.0.1:2181");
        System.out.println(hostAndPort == null ? "NULL" : hostAndPort);
    }

    @Test
    public void testTransformToHostAndPortList() throws Exception {
        List<HostAndPort> hostAndPortList = HostAndPort.transformToHostAndPortList("127.0.0.1:2181\n127.0.0.2:2181");
        if (CollectionUtils.isNotEmpty(hostAndPortList)) {
            for (HostAndPort hostAndPort : hostAndPortList) {
                System.out.println(hostAndPort);
            }
        }
    }

}