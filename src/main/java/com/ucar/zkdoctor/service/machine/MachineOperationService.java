package com.ucar.zkdoctor.service.machine;

/**
 * Description: 远程ssh机器相关操作接口
 * Created on 2018/1/23 12:28
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface MachineOperationService {
    /**
     * 在机器上执行shell命令
     *
     * @param host  目标服务器ip
     * @param shell shell命令
     * @return
     */
    String executeShell(String host, String shell);
}
