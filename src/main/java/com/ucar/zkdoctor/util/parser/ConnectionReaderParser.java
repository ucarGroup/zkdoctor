package com.ucar.zkdoctor.util.parser;

import com.ucar.zkdoctor.pojo.dto.ConnectionInfoDTO;
import com.ucar.zkdoctor.util.exception.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: cons四字命令结果转化器
 * Created on 2018/1/31 14:03
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ConnectionReaderParser implements ReaderParser<List<ConnectionInfoDTO>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionReaderParser.class);

    private LineParser<ConnectionInfoDTO> lineParser = new ConnectionInfoParser();

    @Override
    public List<ConnectionInfoDTO> parseReader(BufferedReader reader) throws ParseException {
        try {
            List<ConnectionInfoDTO> result = new ArrayList<ConnectionInfoDTO>();
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    ConnectionInfoDTO info = lineParser.parse(line);
                    if (info != null) {
                        result.add(info);
                    }
                } catch (ParseException e) {
                    LOGGER.warn("ConnectionInfo parse failed.", e);
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("流读取异常", e);
        }
    }

}