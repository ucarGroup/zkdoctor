package com.ucar.zkdoctor.service.zk;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.vo.ZnodeTreeNodeVO;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: zk服务测试类
 * Created on 2018/2/1 18:15
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ZKServiceTest extends BaseTest {

    @Resource
    private ZKService zkService;

    @Test
    public void testInitZnodesTree() throws Exception {
        List<ZnodeTreeNodeVO> znodeTreeNodeVOList = zkService.initZnodesTree(1);
        if (CollectionUtils.isNotEmpty(znodeTreeNodeVOList)) {
            for (ZnodeTreeNodeVO znodeTreeNodeVO : znodeTreeNodeVOList) {
                System.out.println(znodeTreeNodeVO);
            }
        } else {
            System.out.println("znodeTreeNodeVOList is NULL.");
        }
    }
}