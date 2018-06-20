package com.ucar.zkdoctor.web.controller.manager;

import com.ucar.zkdoctor.pojo.bo.AddNewConfigFileBO;
import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.vo.UploadedJarFileVO;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.zk.ZKService;
import com.ucar.zkdoctor.util.config.ModifiableConfig;
import com.ucar.zkdoctor.util.constant.protocol.MachineProtocol;
import com.ucar.zkdoctor.web.ConResult;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: 实例管理相关Controller，管理员权限功能
 * Created on 2018/1/9 10:57
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/manage/instance")
public class InstanceManageController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceManageController.class);

    @Resource
    private InstanceService instanceService;

    @Resource
    private MachineService machineService;

    @Resource
    private ZKService zkService;

    /**
     * 增加新实例信息
     *
     * @param clusterId 集群id
     * @param host      新实例ip
     * @param port      新实例port
     * @return
     */
    @RequestMapping(value = "/addNewInstance", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doAddNewInstance(Integer clusterId, String host, Integer port) {
        if (clusterId == null || StringUtils.isBlank(host) || port == null) {
            return ConResult.fail("所填信息为NULL，请检查后重试。");
        }
        try {
            // 保存机器信息，如果之前没有加入该机器信息
            machineService.insertIfNotExistsMachine(host);
            // 检测该实例是否被其他集群占用
            boolean checkExistsInstance = instanceService.existsInstances(new HostAndPort(host, port));
            if (checkExistsInstance) {
                return ConResult.fail("该实例已被某集群占用，请检查！");
            }
            // 保存实例信息
            boolean result = instanceService.insertInstanceForCluster(clusterId, host, port);
            if (result) {
                return new ConResult(true, "新增实例成功！", null);
            } else {
                return ConResult.fail("新增实例失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("新增实例失败：" + e.getMessage());
        }
    }

    /**
     * 移除实例信息
     *
     * @param instanceId 实例id
     * @return
     */
    @RequestMapping(value = "/removeInstance", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doRemoveInstance(Integer instanceId) {
        if (instanceId == null) {
            return ConResult.fail("实例id为NULL，请检查后重试。");
        }
        try {
            boolean result = instanceService.removeInstanceForCluster(instanceId);
            if (result) {
                return new ConResult(true, "移除实例成功！", null);
            } else {
                return ConResult.fail("移除实例相关信息失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("移除实例相关信息失败：" + e.getMessage());
        }
    }

    /**
     * 下线实例
     *
     * @param instanceId 实例id
     * @return
     */
    @RequestMapping(value = "/offLineInstance", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doOffLineInstance(Integer instanceId) {
        if (instanceId == null) {
            return ConResult.fail("实例id为NULL，请检查后重试。");
        }
        try {
            boolean result = zkService.stopZKInstance(instanceId);
            if (result) {
                return new ConResult(true, "下线实例成功！", null);
            } else {
                return ConResult.fail("下线实例失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("下线实例失败：" + e.getMessage());
        }
    }

    /**
     * 重启实例
     *
     * @param instanceId 实例id
     * @return
     */
    @RequestMapping(value = "/restartInstance", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doRestartInstance(Integer instanceId) {
        if (instanceId == null) {
            return ConResult.fail("实例id为NULL，请检查后重试。");
        }
        try {
            boolean result = zkService.restartZKInstance(instanceId);
            if (result) {
                return new ConResult(true, "重启实例成功！", null);
            } else {
                return ConResult.fail("重启实例失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("重启实例失败：" + e.getMessage());
        }
    }

    /**
     * 更改实例连接信息收集开关状态
     *
     * @param instanceId 实例id
     * @param monitor    开关状态
     * @return
     */
    @RequestMapping(value = "/updateInstanceConnCollectMonitor")
    @ResponseBody
    public ConResult doUpdateInstanceConnCollectMonitor(Integer instanceId, Boolean monitor) {
        if (instanceId == null || monitor == null) {
            return ConResult.fail("实例id或开关状态为NULL，请检查后重试。");
        }
        try {
            boolean result = instanceService.updateInstanceConnMonitor(instanceId, monitor);
            if (result) {
                return ConResult.success();
            } else {
                return ConResult.fail("修改实例连接信息收集开关失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("修改实例连接信息收集开关失败：" + e.getMessage());
        }
    }

    /**
     * 查询实例zoo.cfg配置文件内容
     *
     * @param host 实例ip
     * @return
     */
    @RequestMapping(value = "/queryInstanceConfig")
    @ResponseBody
    public ConResult doQueryInstanceConfig(String host) {
        if (StringUtils.isBlank(host)) {
            return ConResult.fail("实例host不能为NULL，请检查后重试。");
        }
        try {
            String config = instanceService.queryInstanceConfig(host);
            return ConResult.success(config);
        } catch (RuntimeException e) {
            return ConResult.fail("获取实例配置信息失败：" + e.getMessage());
        }
    }

    /**
     * 修改实例zoo.cfg配置文件
     *
     * @param host               实例ip
     * @param zooConfFileContent 新配置文件内容
     * @return
     */
    @RequestMapping(value = "/instanceConfOps")
    @ResponseBody
    public ConResult doInstanceConfOps(String host, String zooConfFileContent) {
        if (StringUtils.isBlank(host) || StringUtils.isBlank(zooConfFileContent)) {
            return ConResult.fail("实例ip以及配置文件内容不能为NULL，请检查后重试。");
        }
        try {
            boolean result = instanceService.instanceConfOps(host, zooConfFileContent);
            if (result) {
                return new ConResult(true, "修改实例配置成功！", null);
            } else {
                return ConResult.fail("修改实例配置失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("修改实例配置失败：" + e.getMessage());
        }
    }

    /**
     * 新增配置文件
     *
     * @param addNewConfigFileBO 新配置相关参数
     * @return
     */
    @RequestMapping(value = "/addNewConfigFile")
    @ResponseBody
    public ConResult doAddNewConfigFile(AddNewConfigFileBO addNewConfigFileBO) {
        try {
            isLegalAddNewConfigFileParams(addNewConfigFileBO);
        } catch (RuntimeException e) {
            return ConResult.fail("失败：" + e.getMessage());
        }
        try {
            boolean result = instanceService.addNewConfigFile(addNewConfigFileBO);
            if (result) {
                return new ConResult(true, "新增配置成功！", null);
            } else {
                return ConResult.fail("新增配置失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("新增配置失败：" + e.getMessage());
        }
    }

    /**
     * 升级jar文件上传
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/uploadNewJarFile", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doUploadNewJarFile(HttpServletRequest request, HttpServletResponse response) {
        // 上传文件结果
        UploadedJarFileVO uploadedJarFileVO = new UploadedJarFileVO();
        uploadedJarFileVO.setStatus("error");
        if (ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map fileMap = multipartRequest.getFileMap();
            MultipartFile multipartFile = (MultipartFile) fileMap.get("file");
            if (multipartFile != null) {
                String fileName = multipartFile.getOriginalFilename();
                File file = new File(ModifiableConfig.uploadFileDir + fileName);
                // 先判断指定路径是否存在，如果不存在则创建对应目录
                if (!file.getParentFile().exists()) {
                    boolean result = file.getParentFile().mkdirs();
                    if (!result) {
                        LOGGER.warn("Create file dir {} failed when uploading file {}.", ModifiableConfig.uploadFileDir, fileName);
                        return ConResult.success(uploadedJarFileVO);
                    }
                }
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = multipartFile.getInputStream();
                    outputStream = new FileOutputStream(file);
                    // 开辟10M空间
                    byte[] buffer = new byte[10485760];
                    int bytes;
                    while ((bytes = inputStream.read(buffer)) != -1) {
                        if (bytes == 0) {
                            bytes = inputStream.read();
                            if (bytes < 0) {
                                break;
                            }
                            outputStream.write(bytes);
                            outputStream.flush();
                        } else {
                            outputStream.write(buffer, 0, bytes);
                            outputStream.flush();
                        }
                    }
                    uploadedJarFileVO.setStatus("done");
                } catch (Exception e) {
                    LOGGER.warn("Upload file {} failed.", fileName, e);
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            LOGGER.warn("Close file {} input stream failed.", fileName, e);
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            LOGGER.warn("Close file {} output stream failed.", fileName, e);
                        }
                    }
                }
            }
        }
        return ConResult.success(uploadedJarFileVO);
    }

    /**
     * 查询已经上传的升级jar文件是否存在
     *
     * @return
     */
    @RequestMapping(value = "/queryUploadedJarFile")
    @ResponseBody
    public ConResult doQueryUploadedJarFile() {
        try {
            List<UploadedJarFileVO> uploadedJarFileVOList = instanceService.queryUploadedJarFile();
            return ConResult.success(uploadedJarFileVOList == null ? new ArrayList<UploadedJarFileVO>() :
                    uploadedJarFileVOList);
        } catch (RuntimeException e) {
            return ConResult.fail("查询已上传Jar文件失败：" + e.getMessage());
        }
    }

    /**
     * 服务升级：替换jar包，重启zk服务
     *
     * @param instanceId 待升级实例id
     * @return
     */
    @RequestMapping(value = "/instanceUpdateServer")
    @ResponseBody
    public ConResult doInstanceUpdateServer(Integer instanceId) {
        if (instanceId == null) {
            return ConResult.fail("实例id为NULL，请检查后重试。");
        }
        try {
            boolean update = instanceService.updateServer(instanceId);
            if (update) {
                return new ConResult(true, "升级服务成功！", null);
            } else {
                return ConResult.fail("升级服务失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("升级服务失败：" + e.getMessage());
        }
    }

    /**
     * 校验新增配置文件参数
     *
     * @param addNewConfigFileBO 参数
     * @return
     */
    private boolean isLegalAddNewConfigFileParams(AddNewConfigFileBO addNewConfigFileBO) {
        if (addNewConfigFileBO == null) {
            throw new RuntimeException("新增信息为NULL，请检查后重试。");
        } else if (StringUtils.isBlank(addNewConfigFileBO.getHost())) {
            throw new RuntimeException("实例ip不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(addNewConfigFileBO.getConfDir())) {
            throw new RuntimeException("配置文件目录不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(addNewConfigFileBO.getConfFileName())) {
            throw new RuntimeException("配置文件名称不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(addNewConfigFileBO.getConfFileContent())) {
            throw new RuntimeException("配置文件内容不能为空，请检查后重试。");
        } else if (addNewConfigFileBO.getConfDir().endsWith(MachineProtocol.PATH)) {
            throw new RuntimeException("配置文件目录不要以'/'结尾，请检查后重试。");
        }
        return true;
    }
}
