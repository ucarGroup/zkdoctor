package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.valueobject;

import java.io.Serializable;

public class IndexCollectInfo implements Serializable {
    private int sumTotal;//总数量
    private int runningTotal;//正在运行的数量
    private int notRunningTotal;//未运行的数量
    private int exceptionsTotal;// 运行异常的数量
    private int referralTotal;//已经下线的数量
    private int unmonitoredTotal;//未监控的数量

    public IndexCollectInfo() {
    }

    public IndexCollectInfo(int sumTotal, int runningTotal, int notRunningTotal, int exceptionsTotal, int referralTotal, int unmonitoredTotal) {
        this.sumTotal = sumTotal;
        this.runningTotal = runningTotal;
        this.notRunningTotal = notRunningTotal;
        this.exceptionsTotal = exceptionsTotal;
        this.referralTotal = referralTotal;
        this.unmonitoredTotal = unmonitoredTotal;
    }

    public int getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(int sumTotal) {
        this.sumTotal = sumTotal;
    }

    public int getRunningTotal() {
        return runningTotal;
    }

    public void setRunningTotal(int runningTotal) {
        this.runningTotal = runningTotal;
    }

    public int getNotRunningTotal() {
        return notRunningTotal;
    }

    public void setNotRunningTotal(int notRunningTotal) {
        this.notRunningTotal = notRunningTotal;
    }

    public int getExceptionsTotal() {
        return exceptionsTotal;
    }

    public void setExceptionsTotal(int exceptionsTotal) {
        this.exceptionsTotal = exceptionsTotal;
    }

    public int getReferralTotal() {
        return referralTotal;
    }

    public void setReferralTotal(int referralTotal) {
        this.referralTotal = referralTotal;
    }

    public int getUnmonitoredTotal() {
        return unmonitoredTotal;
    }

    public void setUnmonitoredTotal(int unmonitoredTotal) {
        this.unmonitoredTotal = unmonitoredTotal;
    }
}
