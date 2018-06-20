package com.ucar.zkdoctor.util.parser;

import com.ucar.zkdoctor.util.exception.ParseException;
import org.apache.commons.lang.StringUtils;

/**
 * Description: k-v数据格式转化
 * Created on 2018/1/29 19:52
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class KVPairParamParser implements LineParser<String[]> {

    private static final String DEFAULT_SEPARATOR = "=";

    private String separator = DEFAULT_SEPARATOR;

    public KVPairParamParser() {

    }

    public KVPairParamParser(String separator) {
        if (separator == null || separator.length() == 0) {
            throw new IllegalArgumentException("argument separator can't be empty");
        }
        this.separator = separator;
    }

    @Override
    public String[] parse(String line) throws ParseException {
        if (StringUtils.isBlank(line)) {
            return null;
        }
        String[] splits = line.split(separator);
        if (splits.length != 2) {
            throw new ParseException("参数解析失败，串格式错误：" + line);
        }
        return splits;
    }

}