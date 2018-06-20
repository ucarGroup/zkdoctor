package com.ucar.zkdoctor.util.parser;

import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.dto.ConnectionInfoDTO;
import com.ucar.zkdoctor.util.exception.ParseException;
import com.ucar.zkdoctor.util.tool.ReflectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 连接信息转化器
 * Created on 2018/1/31 14:05
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ConnectionInfoParser implements LineParser<ConnectionInfoDTO> {

    private LineParser<String[]> paramParser = new KVPairParamParser();

    @Override
    public ConnectionInfoDTO parse(String line) throws ParseException {
        if (StringUtils.isBlank(line)) {
            return null;
        }
        ConnectionInfoDTO info = new ConnectionInfoDTO();
        if (line.contains("/") && line.contains(":")) {
            info.setIp(line.substring(line.indexOf("/") + 1, line.indexOf(":")));
            info.setPort(Integer.parseInt(line.substring(line.indexOf(":") + 1, line.indexOf("["))));
        } else {
            info.setIp(line.substring(0, line.indexOf("[")));
            info.setPort(0);
        }
        info.setHostInfo(info.getIp() + HostAndPort.HOST_PORT_SEPARATOR + info.getPort());
        info.setIndex(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))));
        String paramStr = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        Map<String, String> attrMap = convertToMap(paramStr);
        ReflectUtils.assignAttrs(info, attrMap);
        info.setInfoLine(line);
        return info;
    }

    /**
     * 将连接信息转化为k-v对
     *
     * @param paramStr 参数字符串
     * @return
     * @throws ParseException
     */
    private Map<String, String> convertToMap(String paramStr) throws ParseException {
        Map<String, String> attrMap = new HashMap<String, String>();
        String[] paramArray = paramStr.split(",");
        for (String paramPair : paramArray) {
            if (paramPair != null && !paramPair.equals("\n")) {
                String[] splits = paramParser.parse(paramPair);
                attrMap.put(splits[0], splits[1]);
            }
        }
        return attrMap;
    }
}

