package com.ucar.zkdoctor.pojo.dto;

import java.io.Serializable;

/**
 * Description: zk服务端状态信息，mntr四字命令结果
 * Created on 2018/1/29 19:43
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ServerStateInfoDTO implements Serializable {

    private static final long serialVersionUID = -8126055597792348340L;

    /**
     * zk版本
     */
    private String zk_version;

    /**
     * 平均延时
     */
    private Long zk_avg_latency;

    /**
     * 最大延时
     */
    private Long zk_max_latency;

    /**
     * 最小延时
     */
    private Long zk_min_latency;

    /**
     * 收包数
     */
    private Long zk_packets_received;

    /**
     * 发包数
     */
    private Long zk_packets_sent;

    /**
     * 连接数
     */
    private Integer zk_num_alive_connections;

    /**
     * 堆积请求数
     */
    private Long zk_outstanding_requests;

    /**
     * 状态（角色）
     */
    private String zk_server_state;

    /**
     * znode数量
     */
    private Integer zk_znode_count;

    /**
     * watch数量
     */
    private Integer zk_watch_count;

    /**
     * 临时节点数量
     */
    private Integer zk_ephemerals_count;

    /**
     * 数据大小
     */
    private Long zk_approximate_data_size;

    /**
     * 打开的文件描述符数量
     */
    private Long zk_open_file_descriptor_count;

    /**
     * 最大文件描述符数量
     */
    private Long zk_max_file_descriptor_count;

    /**
     * follower数量
     */
    private Integer zk_followers;

    /**
     * 同步的follower数量
     */
    private Integer zk_synced_followers;

    /**
     * 准备同步的数量
     */
    private Integer zk_pending_syncs;

    @Override
    public String toString() {
        return "ServerStateInfoDTO{" +
                "zk_version='" + zk_version + '\'' +
                ", zk_avg_latency=" + zk_avg_latency +
                ", zk_max_latency=" + zk_max_latency +
                ", zk_min_latency=" + zk_min_latency +
                ", zk_packets_received=" + zk_packets_received +
                ", zk_packets_sent=" + zk_packets_sent +
                ", zk_num_alive_connections=" + zk_num_alive_connections +
                ", zk_outstanding_requests=" + zk_outstanding_requests +
                ", zk_server_state='" + zk_server_state + '\'' +
                ", zk_znode_count=" + zk_znode_count +
                ", zk_watch_count=" + zk_watch_count +
                ", zk_ephemerals_count=" + zk_ephemerals_count +
                ", zk_approximate_data_size=" + zk_approximate_data_size +
                ", zk_open_file_descriptor_count=" + zk_open_file_descriptor_count +
                ", zk_max_file_descriptor_count=" + zk_max_file_descriptor_count +
                ", zk_followers=" + zk_followers +
                ", zk_synced_followers=" + zk_synced_followers +
                ", zk_pending_syncs=" + zk_pending_syncs +
                '}';
    }

    public String getZk_version() {
        return zk_version;
    }

    public void setZk_version(String zk_version) {
        this.zk_version = zk_version;
    }

    public Long getZk_avg_latency() {
        return zk_avg_latency;
    }

    public void setZk_avg_latency(Long zk_avg_latency) {
        this.zk_avg_latency = zk_avg_latency;
    }

    public Long getZk_max_latency() {
        return zk_max_latency;
    }

    public void setZk_max_latency(Long zk_max_latency) {
        this.zk_max_latency = zk_max_latency;
    }

    public Long getZk_min_latency() {
        return zk_min_latency;
    }

    public void setZk_min_latency(Long zk_min_latency) {
        this.zk_min_latency = zk_min_latency;
    }

    public Long getZk_packets_received() {
        return zk_packets_received;
    }

    public void setZk_packets_received(Long zk_packets_received) {
        this.zk_packets_received = zk_packets_received;
    }

    public Long getZk_packets_sent() {
        return zk_packets_sent;
    }

    public void setZk_packets_sent(Long zk_packets_sent) {
        this.zk_packets_sent = zk_packets_sent;
    }

    public Integer getZk_num_alive_connections() {
        return zk_num_alive_connections;
    }

    public void setZk_num_alive_connections(Integer zk_num_alive_connections) {
        this.zk_num_alive_connections = zk_num_alive_connections;
    }

    public Long getZk_outstanding_requests() {
        return zk_outstanding_requests;
    }

    public void setZk_outstanding_requests(Long zk_outstanding_requests) {
        this.zk_outstanding_requests = zk_outstanding_requests;
    }

    public String getZk_server_state() {
        return zk_server_state;
    }

    public void setZk_server_state(String zk_server_state) {
        this.zk_server_state = zk_server_state;
    }

    public Integer getZk_znode_count() {
        return zk_znode_count;
    }

    public void setZk_znode_count(Integer zk_znode_count) {
        this.zk_znode_count = zk_znode_count;
    }

    public Integer getZk_watch_count() {
        return zk_watch_count;
    }

    public void setZk_watch_count(Integer zk_watch_count) {
        this.zk_watch_count = zk_watch_count;
    }

    public Integer getZk_ephemerals_count() {
        return zk_ephemerals_count;
    }

    public void setZk_ephemerals_count(Integer zk_ephemerals_count) {
        this.zk_ephemerals_count = zk_ephemerals_count;
    }

    public Long getZk_approximate_data_size() {
        return zk_approximate_data_size;
    }

    public void setZk_approximate_data_size(Long zk_approximate_data_size) {
        this.zk_approximate_data_size = zk_approximate_data_size;
    }

    public Long getZk_open_file_descriptor_count() {
        return zk_open_file_descriptor_count;
    }

    public void setZk_open_file_descriptor_count(Long zk_open_file_descriptor_count) {
        this.zk_open_file_descriptor_count = zk_open_file_descriptor_count;
    }

    public Long getZk_max_file_descriptor_count() {
        return zk_max_file_descriptor_count;
    }

    public void setZk_max_file_descriptor_count(Long zk_max_file_descriptor_count) {
        this.zk_max_file_descriptor_count = zk_max_file_descriptor_count;
    }

    public Integer getZk_followers() {
        return zk_followers;
    }

    public void setZk_followers(Integer zk_followers) {
        this.zk_followers = zk_followers;
    }

    public Integer getZk_synced_followers() {
        return zk_synced_followers;
    }

    public void setZk_synced_followers(Integer zk_synced_followers) {
        this.zk_synced_followers = zk_synced_followers;
    }

    public Integer getZk_pending_syncs() {
        return zk_pending_syncs;
    }

    public void setZk_pending_syncs(Integer zk_pending_syncs) {
        this.zk_pending_syncs = zk_pending_syncs;
    }
}
