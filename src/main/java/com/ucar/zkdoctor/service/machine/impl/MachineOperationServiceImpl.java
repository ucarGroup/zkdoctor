package com.ucar.zkdoctor.service.machine.impl;

import com.ucar.zkdoctor.service.machine.MachineOperationService;
import com.ucar.zkdoctor.util.exception.SSHException;
import com.ucar.zkdoctor.util.ssh.SSHUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Description: 远程ssh机器相关操作接口实现类 TODO
 * Created on 2018/1/23 12:29
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class MachineOperationServiceImpl implements MachineOperationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MachineOperationServiceImpl.class);

    @Override
    public String executeShell(String host, String shell) {
        if (StringUtils.isBlank(host) || StringUtils.isBlank(shell)) {
            return null;
        }
        try {
            return SSHUtil.execute(host, shell);
        } catch (SSHException e) {
            LOGGER.error("execute cmd {} in {} error :" + e.getMessage(), shell, host, e);
        }
        return null;
    }
}
