package com.ucar.zkdoctor.util.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 幂等操作器
 * Created on 2018/1/16 10:31
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public abstract class IdempotentConfirmer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdempotentConfirmer.class);

    private int retry = 3;

    protected IdempotentConfirmer(int retry) {
        this.retry = retry;
    }

    public IdempotentConfirmer() {
    }

    public abstract boolean execute();

    public boolean run() {
        while (retry-- > 0) {
            try {
                boolean isOk = execute();
                if (isOk) {
                    return true;
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                continue;
            }
        }
        return false;
    }
}
