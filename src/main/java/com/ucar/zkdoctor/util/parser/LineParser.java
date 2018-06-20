package com.ucar.zkdoctor.util.parser;

import com.ucar.zkdoctor.util.exception.ParseException;

/**
 * Description: 行转化器
 * Created on 2018/1/29 19:52
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface LineParser<T> {
    T parse(String line) throws ParseException;
}
