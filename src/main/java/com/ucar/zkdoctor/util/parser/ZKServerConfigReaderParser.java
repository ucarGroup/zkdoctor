package com.ucar.zkdoctor.util.parser;

import com.ucar.zkdoctor.pojo.dto.ZKServerConfigDTO;
import com.ucar.zkdoctor.util.exception.ParseException;
import com.ucar.zkdoctor.util.tool.ReflectUtils;

import java.io.BufferedReader;
import java.util.Map;

/**
 * Description: conf四字命令结果转化器
 * Created on 2018/1/30 21:51
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ZKServerConfigReaderParser implements ReaderParser<ZKServerConfigDTO> {

    private ReaderParser<Map<String, String>> mapReaderParser = new MapReaderParser();

    @Override
    public ZKServerConfigDTO parseReader(BufferedReader reader) throws ParseException {
        Map<String, String> attrMap = mapReaderParser.parseReader(reader);
        ZKServerConfigDTO zkServerConfigDTO = new ZKServerConfigDTO();
        ReflectUtils.assignAttrs(zkServerConfigDTO, attrMap);
        return zkServerConfigDTO;
    }

}
