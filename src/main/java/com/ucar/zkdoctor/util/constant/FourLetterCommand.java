package com.ucar.zkdoctor.util.constant;

/**
 * Description: 四字监控命令
 * Created on 2018/1/23 11:24
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public enum FourLetterCommand {
    /**
     * conf (New in 3.3.0)输出相关服务配置的详细信息。比如端口、zk数据及日志配置路径、最大连接数，session超时时间、serverId等
     * cons (New in 3.3.0)列出所有连接到这台服务器的客户端连接/会话的详细信息。包括“接受/发送”的包数量、session id 、操作延迟、最后的操作执行等信息
     * crst New in 3.3.0)重置当前这台服务器所有连接/会话的统计信息
     * dump 列出未经处理的会话和临时节点（只在leader上有效）
     * envi 输出关于服务器的环境详细信息（不同于conf命令），比如host.name、java.version、java.home、user.dir=/data/zookeeper-3.4.6/bin之类信息
     * ruok 测试服务是否处于正确运行状态。如果正常返回"imok"，否则返回空
     * srst 重置服务器的统计信息
     * srvr (New in 3.3.0)输出服务器的详细信息。zk版本、接收/发送包数量、连接数、模式（leader/follower）、节点总数
     * stat 输出服务器的详细信息：接收/发送包数量、连接数、模式（leader/follower）、节点总数、延迟。 所有客户端的列表
     * wchs (New in 3.3.0)列出服务器watches的简洁信息：连接总数、watching节点总数和watches总数
     * wchc (New in 3.3.0)通过session分组，列出watch的所有节点，它的输出是一个与 watch 相关的会话的节点列表。如果watches数量很大的话，将会产生很大的开销，会影响性能，小心使用
     * wchp (New in 3.3.0)通过路径分组，列出所有的 watch 的session id信息。它输出一个与 session 相关的路径。如果watches数量很大的话，将会产生很大的开销，会影响性能，小心使用
     * mntr (New in 3.4.0)列出集群的健康状态。包括“接受/发送”的包数量、操作延迟、当前服务模式（leader/follower）、节点总数、watch总数、临时节点总数
     */
    conf,
    cons,
    crst,
    dump,
    envi,
    ruok,
    srst,
    srvr,
    stat,
    wchs,
    wchc,
    wchp,
    mntr,
}