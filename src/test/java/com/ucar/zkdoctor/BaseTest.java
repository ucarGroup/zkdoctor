package com.ucar.zkdoctor;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Description: 测试基类，基本配置
 * Created on 2018/1/5 13:41
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZkDoctorMain.class)
@WebAppConfiguration
public class BaseTest {
}
