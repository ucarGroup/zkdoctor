package com.ucar.zkdoctor.service.zk;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;

/**
 * Description: zk节点操作测试
 * Created on 2018/1/30 15:22
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class CustomZKManagerTest {

    private CustomZKManager customZKManager = new CustomZKManager();

    @Test
    public void testCreateNotExistsPersistentNode() throws Exception {
        boolean result = customZKManager.createNotExistsNode(1, "127.0.0.1", 2181, "/zkdoctor/test1",
                "test".getBytes(), true);
        System.out.println("result is " + result);
    }

    @Test
    public void testCreateNotExistsPersistentNode1() throws Exception {
        for (int i = 0; i < 10; i++) {
            boolean result = customZKManager.createNotExistsPersistentNode("127.0.0.1", 2181, "/zk/test/test1/test2/test" + i, "test".getBytes());
            System.out.println("result is " + result);
        }
    }

    @Test
    public void testEqualsZNodeData() throws Exception {
        boolean result = customZKManager.equalsZNodeData(1, "127.0.0.1", 2181, "/zkdoctor/test", "test".getBytes());
        System.out.println("result is " + result);
    }

    @Test
    public void testDeleteZNodeOnly() throws Exception {
        boolean result = customZKManager.deleteZNodeOnly(1, "127.0.0.1", 2181, "/zkdoctor");
        System.out.println("result is " + result);
    }

    @Test
    public void testDeleteZNodeAndChildren() throws Exception {
        boolean result = customZKManager.deleteZNodeAndChildren(1, "127.0.0.1", 2181, "/zkdoctor");
        System.out.println("result is " + result);
    }

    @Test
    public void testDeleteZNodeAndChildren1() throws Exception {
        boolean result = customZKManager.deleteZNodeAndChildren("127.0.0.1", 2181, "/zkdoctor");
        System.out.println("result is " + result);
    }

    @Test
    public void testGetChildren() throws Exception {
        List<String> childrenData = customZKManager.getChildren(1, "127.0.0.1", 2181, "/", false);
        if (CollectionUtils.isNotEmpty(childrenData)) {
            for (String child : childrenData) {
                System.out.println(child);
            }
        } else {
            System.out.println("Result is NULL.");
        }
    }

    @Test
    public void testGetData() throws Exception {
        byte[] data = customZKManager.getData(1, "127.0.0.1", 2181, "/zkdoctor/test", null, false);
        if (data != null) {
            System.out.println(new String(data));
        } else {
            System.out.println("Result is NULL.");
        }
    }

    @Test
    public void testSetData() throws Exception {
        Stat setData = customZKManager.setData(1, "127.0.0.1", 2181, "/zkdoctor/test", "testdata".getBytes(), -1);
        if (setData != null) {
            System.out.println(setData.toString());
        } else {
            System.out.println("Result is NULL.");
        }
    }
}