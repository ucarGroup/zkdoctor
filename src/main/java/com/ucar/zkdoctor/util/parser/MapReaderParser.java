package com.ucar.zkdoctor.util.parser;

import com.ucar.zkdoctor.util.exception.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: map数据格式转化
 * Created on 2018/1/29 19:51
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MapReaderParser implements ReaderParser<Map<String, String>> {

    private LineParser<String[]> paramParser;

    public MapReaderParser() {
        this.paramParser = new KVPairParamParser();
    }

    public MapReaderParser(String separator) {
        this.paramParser = new KVPairParamParser(separator);
    }

    public MapReaderParser(LineParser<String[]> paramParser) {
        this.paramParser = paramParser;
    }

    @Override
    public Map<String, String> parseReader(BufferedReader reader) throws ParseException {
        try {
            Map<String, String> result = new HashMap<String, String>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] splits = paramParser.parse(line);
                if (splits == null) {
                    continue;
                }
                if (splits.length == 2) {
                    result.put(splits[0], splits[1]);
                } else {
                    throw new RuntimeException("解析器返回结果数组长度错误");
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("流读取异常", e);
        }
    }

}

