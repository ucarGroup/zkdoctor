package com.ucar.zkdoctor.service.zk;

import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: zk节点操作管理器
 * Created on 2018/1/30 15:05
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class CustomZKManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomZKManager.class);

    /**
     * 默认session timeout时长为5s
     */
    private final int sessionTimeOut = 5000;

    /**
     * 默认连接超时时长为3s
     */
    private final int connectionTimeOut = 3000;

    /**
     * 路径分隔符
     */
    public static final String PATH = "/";

    /**
     * zk连接连接池,key：实例id，value：zk连接
     */
    private static final Map<Integer, CuratorFramework> ZK_CONNECT_POOL = new ConcurrentHashMap<Integer, CuratorFramework>();

    /**
     * zk客户端重试策略
     */
    private RetryPolicy defaultRetryPolicy = new ExponentialBackoffRetry(1000, 3);

    /**
     * 获取zk连接，统一入口
     *
     * @param instanceId 实例id
     * @param host       zk ip
     * @param port       zk port
     * @return
     */
    public CuratorFramework getZookeeper(final int instanceId, final String host, final int port) {
        if (!ZK_CONNECT_POOL.containsKey(instanceId)) {
            synchronized (this) { // 保证创建的客户端连接只有唯一一个
                if (!ZK_CONNECT_POOL.containsKey(instanceId)) {
                    CuratorFramework zkClient = createClient(host, port, new ConnectionStateListener() {
                        @Override
                        public void stateChanged(CuratorFramework client, ConnectionState newState) {
                            LOGGER.warn("ZK client connection state changed：{}, host:port = {}:{}.", newState, host, port);
                        }
                    });
                    if (zkClient != null) {
                        ZK_CONNECT_POOL.put(instanceId, zkClient);
                    }
                    return zkClient;
                } else {
                    return ZK_CONNECT_POOL.get(instanceId);
                }
            }
        } else {
            return ZK_CONNECT_POOL.get(instanceId);
        }
    }

    /**
     * 创建zk客户端
     *
     * @param host     zk ip
     * @param port     zk port
     * @param listener 连接监听器
     * @return
     */
    private CuratorFramework createClient(String host, int port, ConnectionStateListener listener) {
        if (StringUtils.isBlank(host)) {
            return null;
        }
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(genertateConnectStr(host, port),
                sessionTimeOut, connectionTimeOut, defaultRetryPolicy);
        zkClient.start();
        if (listener != null) {
            zkClient.getConnectionStateListenable().addListener(listener);
        }
        return zkClient;
    }

    /**
     * 连接字符串
     *
     * @param host zk ip
     * @param port zk port
     * @return
     */
    private String genertateConnectStr(String host, int port) {
        return host + HostAndPort.HOST_PORT_SEPARATOR + port;
    }

    /**
     * 关闭当前连接池中的某个实例的客户端连接
     *
     * @param instanceId 实例id
     */
    public void closeZookeeper(int instanceId) {
        closeZookeeper(ZK_CONNECT_POOL.get(instanceId));
        ZK_CONNECT_POOL.remove(instanceId);
    }

    /**
     * 关闭zk连接
     *
     * @param zkClient zk客户端实例
     */
    public void closeZookeeper(CuratorFramework zkClient) {
        if (zkClient != null) {
            zkClient.close();
        }
    }

    /**
     * 当且仅当节点不存在时，创建且返回true
     *
     * @param instanceId   实例id
     * @param host         zk ip
     * @param port         zk port
     * @param path         路径
     * @param data         数据
     * @param isPersistent 节点是否为持久化节点
     * @return
     */
    public boolean createNotExistsNode(int instanceId, String host, int port, String path, byte[] data, boolean isPersistent) {
        CuratorFramework zkClient = getZookeeper(instanceId, host, port);
        if (zkClient == null) {
            return false;
        }
        try {
            if (zkClient.checkExists().forPath(path) == null) {
                if (isPersistent) {
                    zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data);
                } else {
                    zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data);
                }
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Create not exists node - path:{} in {}:{} error.", path, host, port, e);
        }
        return false;
    }

    /**
     * 当且仅当节点不存在的时候，创建节点并返回成功
     *
     * @param host zk ip
     * @param port zk port
     * @param path 节点路径
     * @param data 节点数据
     * @return
     */
    public boolean createNotExistsPersistentNode(String host, int port, String path, byte[] data) {
        CuratorFramework zkClient = createClient(host, port, null);
        if (zkClient == null) {
            return false;
        }
        try {
            if (zkClient.checkExists().forPath(path) == null) {
                zkClient.create().creatingParentsIfNeeded().forPath(path, data);
                return true;
            }
        } catch (Exception e) {
            LOGGER.warn("Create not exists node - path:{} in {}:{} error.", path, host, port, e);
        } finally {
            closeZookeeper(zkClient);
        }
        return false;
    }

    /**
     * 判断当前节点上的数据是否和给定数据相同。仅支持string序列化数据
     *
     * @param instanceId 实例id
     * @param host       zk ip
     * @param port       zk port
     * @param path       路径
     * @param data       节点数据
     * @return
     */
    public boolean equalsZNodeData(int instanceId, String host, int port, String path, byte[] data) {
        CuratorFramework zkClient = getZookeeper(instanceId, host, port);
        if (zkClient == null) {
            return false;
        }
        try {
            byte[] currentData = zkClient.getData().forPath(path);
            if (data != null && currentData != null) {
                return new String(data).equals(new String(currentData));
            }
        } catch (Exception e) {
            LOGGER.error("Equals node data - path:{} in {}:{} error.", path, host, port, e);
        }
        return false;
    }

    /**
     * 删除给定节点，不包含其子节点
     *
     * @param instanceId 实例id
     * @param host       zk ip
     * @param port       zk port
     * @param path       路径
     * @return
     */
    public boolean deleteZNodeOnly(int instanceId, String host, int port, String path) {
        CuratorFramework zkClient = getZookeeper(instanceId, host, port);
        if (zkClient == null) {
            return false;
        }
        try {
            zkClient.delete().guaranteed().forPath(path);
            return true;
        } catch (Exception e) {
            LOGGER.warn("Delete node - path:{} in {}:{} error.", path, host, port, e);
        }
        return false;
    }

    /**
     * 删除给定节点，包含其子节点
     *
     * @param instanceId 实例id
     * @param host       zk ip
     * @param port       zk port
     * @param path       路径
     * @return
     */
    public boolean deleteZNodeAndChildren(int instanceId, String host, int port, String path) {
        CuratorFramework zkClient = getZookeeper(instanceId, host, port);
        if (zkClient == null) {
            return false;
        }
        try {
            zkClient.delete().deletingChildrenIfNeeded().forPath(path);
            return true;
        } catch (Exception e) {
            LOGGER.warn("Delete node - path:{} in {}:{} error.", path, host, port, e);
        }
        return false;
    }

    /**
     * 删除给定节点，包含其子节点
     *
     * @param host zk ip
     * @param port zk port
     * @param path 路径
     * @return
     */
    public boolean deleteZNodeAndChildren(String host, int port, String path) {
        CuratorFramework zkClient = createClient(host, port, null);
        if (zkClient == null) {
            return false;
        }
        try {
            zkClient.delete().deletingChildrenIfNeeded().forPath(path);
            return true;
        } catch (Exception e) {
            LOGGER.warn("Delete node - path:{} in {}:{} error.", path, host, port, e);
        } finally {
            closeZookeeper(zkClient);
        }
        return false;
    }

    /**
     * 获取子节点
     *
     * @param instanceId 实例id
     * @param host       zk ip
     * @param port       zk port
     * @param path       路径
     * @param watch      是否watch
     * @return
     */
    public List<String> getChildren(int instanceId, String host, int port, String path, boolean watch) {
        CuratorFramework zkClient = getZookeeper(instanceId, host, port);
        if (zkClient == null) {
            return null;
        }
        try {
            if (watch) {
                return zkClient.getChildren().watched().forPath(path);
            } else {
                return zkClient.getChildren().forPath(path);
            }
        } catch (Exception e) {
            LOGGER.warn("Get children from zk {}:{} error, path is {}.", host, port, path, e);
            return null;
        }
    }

    /**
     * 获取数据
     *
     * @param instanceId 实例id
     * @param host       zk ip
     * @param port       zk port
     * @param path       路径
     * @param stat       节点stat
     * @param watch      是否watch
     * @return
     */
    public byte[] getData(int instanceId, String host, int port, String path, Stat stat, boolean watch) {
        CuratorFramework zkClient = getZookeeper(instanceId, host, port);
        if (zkClient == null) {
            return null;
        }
        try {
            if (watch) {
                return zkClient.getData().storingStatIn(stat).watched().forPath(path);
            } else {
                return zkClient.getData().storingStatIn(stat).forPath(path);
            }
        } catch (Exception e) {
            LOGGER.warn("Get data from zk {}:{} error, path is {}.", host, port, path, e);
            return null;
        }
    }

    /**
     * 修改数据
     *
     * @param instanceId 实例id
     * @param host       zk ip
     * @param port       zk port
     * @param path       路径
     * @param data       数据
     * @param version    数据版本
     * @return
     */
    public Stat setData(int instanceId, String host, int port, String path, byte[] data, int version) {
        CuratorFramework zkClient = getZookeeper(instanceId, host, port);
        if (zkClient == null) {
            return null;
        }
        try {
            return zkClient.setData().withVersion(version).forPath(path, data);
        } catch (Exception e) {
            LOGGER.warn("Set data in zk {}:{} error, path is {}.", host, port, path, e);
            return null;
        }
    }

    /**
     * 创建节点
     *
     * @param instanceId 实例id
     * @param host       zk ip
     * @param port       zk port
     * @param path       路径
     * @param data       新数据
     * @param acl        节点acl
     * @param createMode 节点模式
     */
    public void createNode(int instanceId, String host, int port, String path, byte[] data, List<ACL> acl, CreateMode createMode) {
        CuratorFramework zkClient = getZookeeper(instanceId, host, port);
        if (zkClient == null) {
            return;
        }
        try {
            zkClient.create().
                    creatingParentsIfNeeded().
                    withMode(createMode).
                    withACL(acl).
                    forPath(path, data);
        } catch (Exception e) {
            LOGGER.warn("Create node in zk {}:{} error, path is {}.", host, port, path, e);
        }
    }

    /**
     * 创建节点
     *
     * @param instanceId              实例id
     * @param host                    zk ip
     * @param port                    zk port
     * @param path                    路径
     * @param data                    新数据
     * @param acl                     节点acl
     * @param creatingParentsIfNeeded 当父节点不存在时，是否创建父节点
     * @return
     */
    public boolean createNode(int instanceId, String host, int port, String path, byte[] data, List<ACL> acl, boolean creatingParentsIfNeeded) {
        CuratorFramework zkClient = getZookeeper(instanceId, host, port);
        if (zkClient == null) {
            return false;
        }
        try {
            if (creatingParentsIfNeeded) {
                zkClient.create().
                        creatingParentsIfNeeded().
                        withMode(CreateMode.PERSISTENT).
                        withACL(acl).
                        forPath(path, data);
            } else {
                zkClient.create().
                        withMode(CreateMode.PERSISTENT).
                        withACL(acl).
                        forPath(path, data);
            }
            return true;
        } catch (Exception e) {
            LOGGER.warn("Create node in zk {}:{} error, path is {}.", host, port, path, e);
            return false;
        }
    }
}
