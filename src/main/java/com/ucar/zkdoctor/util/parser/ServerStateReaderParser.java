package com.ucar.zkdoctor.util.parser;

import com.ucar.zkdoctor.pojo.dto.ServerStateInfoDTO;
import com.ucar.zkdoctor.util.exception.ParseException;
import com.ucar.zkdoctor.util.tool.ReflectUtils;

import java.io.BufferedReader;
import java.util.Map;

/**
 * Description: mntr四字命令结果转化器
 * Created on 2018/1/29 19:49
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ServerStateReaderParser implements ReaderParser<ServerStateInfoDTO> {

    private ReaderParser<Map<String, String>> mapReaderParser = new MapReaderParser("\t");

    @Override
    public ServerStateInfoDTO parseReader(BufferedReader reader) throws ParseException {
        Map<String, String> attrMap = mapReaderParser.parseReader(reader);
        ServerStateInfoDTO info = new ServerStateInfoDTO();
        ReflectUtils.assignAttrs(info, attrMap);
        return info;
    }
}
