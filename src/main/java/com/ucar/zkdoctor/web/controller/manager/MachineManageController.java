package com.ucar.zkdoctor.web.controller.manager;

import com.ucar.zkdoctor.pojo.po.MachineInfo;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.util.constant.SymbolConstant;
import com.ucar.zkdoctor.util.constant.protocol.MachineProtocol;
import com.ucar.zkdoctor.util.exception.SSHException;
import com.ucar.zkdoctor.util.ssh.SSHUtil;
import com.ucar.zkdoctor.web.ConResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 机器管理相关Controller，管理员权限功能
 * Created on 2018/1/19 16:52
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/manage/machine")
public class MachineManageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MachineManageController.class);

    @Resource
    private MachineService machineService;

    /**
     * ssh到目标服务器后默认的目录
     */
    private String sshDefaultDirectory = "/home/zkdoctor";

    /**
     * 修改机器信息
     *
     * @param machineInfo 机器信息
     * @return
     */
    @RequestMapping(value = "/modifyMachine")
    @ResponseBody
    public ConResult doModifyMachine(MachineInfo machineInfo) {
        if (machineInfo == null || machineInfo.getId() == null) {
            return ConResult.fail("所需修改的信息为NULL，请重新尝试。");
        }
        boolean result = machineService.updateMachineInfo(machineInfo);
        if (result) {
            return new ConResult(true, "修改机器信息成功！", null);
        } else {
            return ConResult.fail("修改失败，请重试！");
        }
    }

    /**
     * 删除机器信息
     *
     * @param machineId 机器id
     * @return
     */
    @RequestMapping(value = "/deleteMachine")
    @ResponseBody
    public ConResult doDeleteMachine(Integer machineId) {
        if (machineId == null) {
            return ConResult.fail("所需删除的机器id为NULL，请重新尝试。");
        }
        boolean result = machineService.deleteMachineInfoById(machineId);
        if (result) {
            return new ConResult(true, "删除机器成功！", null);
        } else {
            return ConResult.fail("删除失败，请重试！");
        }
    }

    /**
     * 增加新的机器信息
     *
     * @param machineInfo 机器信息
     * @return
     */
    @RequestMapping(value = "/addMachine")
    @ResponseBody
    public ConResult doAddMachine(MachineInfo machineInfo) {
        if (machineInfo == null || machineInfo.getHost() == null) {
            return ConResult.fail("新机器信息为NULL，请重新尝试。");
        }
        if (machineService.getMachineInfoByHost(machineInfo.getHost()) != null) {
            return ConResult.fail("该机器已经存在，请检查后重试。");
        }
        // 新加入的机器，默认可用
        machineInfo.setAvailable(true);
        boolean result = machineService.insertMachine(machineInfo);
        if (result) {
            return new ConResult(true, "新创建机器成功！", null);
        } else {
            return ConResult.fail("新创建机器失败，请重试！");
        }
    }

    /**
     * 更新机器监控开关
     *
     * @param machineId 机器id
     * @param monitor   是否监控
     * @return
     */
    @RequestMapping(value = "/updateMonitorStatus")
    @ResponseBody
    public ConResult doUpdateMonitorStatus(Integer machineId, Boolean monitor) {
        if (machineId == null || monitor == null) {
            return ConResult.fail("机器id为NULL，请检查后重试。");
        }
        try {
            boolean result = machineService.updateMachineMonitor(machineId, monitor);
            if (result) {
                return ConResult.success();
            } else {
                return ConResult.fail("修改机器监控状态失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("修改机器监控状态失败：" + e.getMessage());
        }
    }

    /**
     * 下载服务器初始化脚本
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downloadMachineInitScript")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition",
                "attachment;fileName=" + "machine_init.sh");
        byte[] buffer = new byte[4096];
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        try {
            inputStream = MachineManageController.class.getResourceAsStream("/machine_init.sh");
            if (inputStream == null) {
                LOGGER.warn("There is no machine_init.sh to download.");
                return;
            }
            bis = new BufferedInputStream(inputStream);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            LOGGER.error("Download machine init script failed.", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    LOGGER.error("Download machine init script failed in closing BufferedInputStream.", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("Download machine init script failed  in closing InputStream.", e);
                }
            }
        }
    }

    /**
     * ssh终端功能
     *
     * @param command          待执行命令
     * @param hostIp           机器ip
     * @param currentDirectory 当前目录
     * @return
     */
    @RequestMapping(value = "/supermanage/sshCommandExecute")
    @ResponseBody
    public ConResult doSSHCommandExecute(String command, String hostIp, String currentDirectory) {
        String result = StringUtils.EMPTY;
        String directory = sshDefaultDirectory;
        if (StringUtils.isBlank(command) || StringUtils.isBlank(hostIp)) {
            result = "尚未登录或输入的命令为空，请检查后重试";
        } else {
            if (StringUtils.isNotBlank(currentDirectory)) {
                directory = currentDirectory;
            }
            command = command.trim();
            if (command.startsWith("cd")) {
                String nextDirectory = getCurrentDirectory(command, directory, hostIp);
                if (nextDirectory == null) {
                    result = "No such file or directory! Check your directory!";
                } else {
                    directory = nextDirectory;
                }
            } else {
                command = "cd " + directory + ";" + command;
                try {
                    result = SSHUtil.execute(hostIp, command, false);
                } catch (SSHException e) {
                    result = "SSHException: " + e.getMessage();
                    LOGGER.warn("Execute command {} in {} failed.", command, hostIp, e);
                }
            }
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", result);
        data.put("currentDirectory", directory);
        return ConResult.success(data);
    }

    /**
     * 获取当前执行命令所在目录信息
     *
     * @param command          命令
     * @param currentDirectory 当前目录
     * @param hostIp           目标服务器
     * @return
     */
    private String getCurrentDirectory(String command, String currentDirectory, String hostIp) {
        String[] cdCommands = command.split(SymbolConstant.ANY_BLANK);
        if (cdCommands[0].equals("cd") && cdCommands.length == 2) {
            String cdCommand = command;
            if (!cdCommands[1].startsWith(MachineProtocol.PATH)) {
                cdCommand = "cd " + currentDirectory + MachineProtocol.PATH + cdCommands[1];
            }
            try {
                String cdResult = SSHUtil.execute(hostIp, cdCommand);
                if (cdResult == null) { // 返回结果为null，代表有该目录
                    return SSHUtil.execute(hostIp, cdCommand + ";pwd", false);
                }
            } catch (SSHException e) {
                LOGGER.error("Execute command {} failed at host {}.", cdCommand, hostIp, e);
            }
        } else if (cdCommands[0].equals("cd") && cdCommands.length == 1) {
            return sshDefaultDirectory;
        }
        return null;
    }

}
