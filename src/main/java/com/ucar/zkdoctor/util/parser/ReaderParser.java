package com.ucar.zkdoctor.util.parser;

import com.ucar.zkdoctor.util.exception.ParseException;

import java.io.BufferedReader;

/**
 * Description: 数据流读转化器
 * Created on 2018/1/23 11:28
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface ReaderParser<T> {

    T parseReader(BufferedReader reader) throws ParseException;

}