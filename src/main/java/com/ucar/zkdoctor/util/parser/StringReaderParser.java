package com.ucar.zkdoctor.util.parser;

import com.ucar.zkdoctor.util.exception.ParseException;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Description: 字符串解析器
 * Created on 2018/1/23 11:40
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class StringReaderParser implements ReaderParser<String> {

    @Override
    public String parseReader(BufferedReader reader) throws ParseException {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("流读取异常", e);
        }
    }

}
