import React, {Component} from "react";
import {Button, Col, Input, notification, Popconfirm, Row, Select, Tree} from "antd";
import {Link} from "react-router";
import {ClusterZnode, ZnodeDetailInfoVo} from "../../model/ClusterModel";
import styles from "../../../themes/Search.less";
import {SysUserVo} from "../../../sys/model/SysUserModel";
import znodeStyle from "../../../sys/view/layout/common.less";

const TreeNode = Tree.TreeNode;

export interface ClusterZnodeProps {
    clusterInfoId: number;
    sysUser: SysUserVo;
    znodeRoots: Array<ClusterZnode>;
    znodeChildren: Array<ClusterZnode>;
    znodeDataDetail: ZnodeDetailInfoVo;
    initChildren: any;
    onDeleteZnode: any;
    onSearchData: any;
    onUpdateZnode: any;
    onCreateZnode: any;
}


export class ClusterZnodeList extends Component<ClusterZnodeProps, any> {

    constructor(props) {
        super(props);
        this.state = {
            selectedZnode: null,
            selectedKey: '',
            expandedKeys: [],
            znodeDataVisibility: 'hidden',
            serializable: 'string',
        }
    }

    onLoadData = (treeNode) => {
        return new Promise((resolve) => {
            // 拉取子节点数据，每次直接拉取节点数据，不缓存上次记录
            this.props.initChildren(this.props.clusterInfoId, treeNode.props.eventKey, (data) => {
                treeNode.props.dataRef.children = data;
                resolve();
            });
        });
    };

    // 实时展示所选节点信息
    onSelect = (selectedKeys, info) => {
        this.setState({selectedZnode: info.node, selectedKey: selectedKeys[0]}, () => {
            // 状态位更新成功后，设置标签显示值
            document.getElementById('znodeLabel').textContent = this.state.selectedKey;
        });
    };

    renderTreeNodes = (data) => {
        return data.map((item) => {
            if (item.children) {
                return (
                    <TreeNode title={item.title} key={item.key} dataRef={item}>
                        {this.renderTreeNodes(item.children)}
                    </TreeNode>
                );
            }
            return <TreeNode {...item} dataRef={item}/>;
        });
    };

    deleteNode = () => {
        let thisV = this;
        this.props.onDeleteZnode(this.state.selectedKey, (message) => {
                notification['success']({
                    message: message,
                    description: '',
                });
                // 前端过滤已删除的节点信息，将展开节点列表仅保留到删除节点的父节点
                let end = this.state.selectedKey.lastIndexOf("/");
                if (end == -1) {
                    end = this.state.selectedKey.length;
                    console.log("不存在'/'")
                }
                // 父节点路径
                let parent = this.state.selectedKey.substring(0, end);
                // 当前展开节点
                let expandedKeysCurrent = thisV.state.expandedKeys.filter(bean => !bean.startsWith(parent));
                thisV.setState({
                    expandedKeys: expandedKeysCurrent,
                });
            }
        )
    };

    onExpand = (expandedKeys, info) => {
        if (info.expanded) {
            this.state.expandedKeys = expandedKeys;
        } else {
            let key = info.node.props.eventKey;
            let end = key.lastIndexOf("/");
            if (end == -1) {
                end = key.length;
                console.log("关闭节点路径不存在'/'")
            }
            let parent = key.substring(0, end);
            let expandedKeysCurrent = this.state.expandedKeys.filter(bean => {
                if (bean == parent || !bean.startsWith(parent)) {
                    return true;
                } else {
                    return false;
                }
            });
            this.setState({
                expandedKeys: expandedKeysCurrent,
            });
        }
    };

    onSearchData = () => {
        if (this.state.selectedKey) {
            this.props.onSearchData(this.state.selectedKey, this.state.serializable);
            this.setState({znodeDataVisibility: ''});
        } else {
            this.setState({znodeDataVisibility: 'hidden'});
            notification['error']({
                message: '请选择所需查询的节点!',
                description: '',
            });
        }
    };

    onUpdataZnode = () => {
        this.props.onUpdateZnode(this.state.selectedKey);
    };

    onCreateZnode = () => {
        this.props.onCreateZnode(this.state.selectedKey);
    };

    handlsSerializableChange = (value) => {
        this.setState({serializable: value})
    };

    getOperateProps() {
        return {
            sysUser: this.props.sysUser,
            onSearchData: this.onSearchData,
            onUpdataZnode: this.onUpdataZnode,
            deleteNode: this.deleteNode,
            onCreateZnode: this.onCreateZnode,
            selectedKey: this.state.selectedKey,
        };
    }

    render() {
        let znodeDataDetail = this.props.znodeDataDetail ? this.props.znodeDataDetail : new ZnodeDetailInfoVo();
        znodeDataDetail.data = znodeDataDetail.data ? znodeDataDetail.data : '';
        return (
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                borderBottom: '1px #d7d8d9 solid',
                paddingTop: 20,
                paddingBottom: 20
            }}>
                <div className={znodeStyle.znodeContent}>
                    <div style={{display: 'flex'}}>
                        <Tree showLine loadData={this.onLoadData}
                              onSelect={this.onSelect}
                              expandedKeys={this.state.expandedKeys}
                              onExpand={this.onExpand}>
                            {this.renderTreeNodes(this.props.znodeRoots)}
                        </Tree>
                    </div>
                </div>
                <div className={znodeStyle.znodeContent}>
                    <div>
                        <Row style={{marginBottom: 10}}>
                            <Col span={24} offset={2}>
                                <label style={{marginRight: 10, fontWeight: 'bold'}}>节点路径：</label>
                                <label id="znodeLabel"></label>
                            </Col>
                        </Row>
                        <Row className={znodeStyle.znodeOperate}>
                            <OperateManage {...this.getOperateProps()}/>
                        </Row>

                        <div style={{visibility: this.state.znodeDataVisibility}}>
                            <Row className={znodeStyle.znodeOperate}>
                                <Col span={24} offset={1} className={styles.detailCols}
                                     style={{marginTop: 10, fontWeight: 'bold'}}>
                                    节点状态(Stat)信息如下：
                                </Col>

                                <Col span={24} offset={2} className={styles.detailCols}>
                                    创建该节点事务id(czxid)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.czxid}</span>
                                </Col>
                                <Col span={24} offset={2} className={styles.detailCols}>
                                    创建该节点时间(ctime)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.ctime}</span>
                                </Col>

                                <Col span={24} offset={2} className={styles.detailCols}>
                                    修改该节点事务id(mzxid)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.mzxid}</span>
                                </Col>
                                <Col span={24} offset={2} className={styles.detailCols}>
                                    修改该节点时间(mtime)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.mtime}</span>
                                </Col>

                                <Col span={24} offset={2} className={styles.detailCols}>
                                    版本号(version)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.version}</span>
                                </Col>
                                <Col span={24} offset={2} className={styles.detailCols}>
                                    子节点版本号(cversion)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.cversion}</span>
                                </Col>

                                <Col span={24} offset={2} className={styles.detailCols}>
                                    设置节点ACL的版本号(aversion)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.aversion}</span>
                                </Col>
                                <Col span={24} offset={2} className={styles.detailCols}>
                                    最后一次修改该节点/子节点的事务id(pzxid)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.pzxid}</span>
                                </Col>

                                <Col span={24} offset={2} className={styles.detailCols}>
                                    数据长度(dataLength)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.dataLength}</span>
                                </Col>
                                <Col span={24} offset={2} className={styles.detailCols}>
                                    子节点个数(numChildren)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.numChildren}</span>
                                </Col>

                                <Col span={24} offset={2} className={styles.detailCols}>
                                    临时节点所属sessionId(ephemeralOwner,0x0表示永久节点)：<span
                                    className={styles.detailColsContent}>{znodeDataDetail.ephemeralOwner}</span>
                                </Col>

                                <Col span={24} offset={1} className={styles.detailCols}
                                     style={{marginTop: 10, fontWeight: 'bold'}}>
                                    节点数据信息如下：
                                    <Select defaultValue='string'
                                            style={{width: '200px', marginLeft: 10, marginRight: 10}}
                                            onChange={this.handlsSerializableChange}>
                                        <Select.Option value="string">string序列化</Select.Option>
                                        <Select.Option value="hessian">hessian序列化</Select.Option>
                                    </Select>
                                    <Button onClick={this.onSearchData}>重查数据</Button>
                                </Col>
                                <Col span={24} offset={2} className={styles.detailCols}>
                                    <Input.TextArea style={{width: '90%'}} autosize={{minRows: 5, maxRows: 20}}
                                                    value={znodeDataDetail.data}>
                                    </Input.TextArea>
                                </Col>
                            </Row>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

interface ManageProps {
    sysUser: SysUserVo;
    onSearchData: any;
    onUpdataZnode: any;
    deleteNode: any;
    onCreateZnode: any;
    selectedKey: string;
}

const OperateManage = (props: ManageProps) => {
    let role = props.sysUser.userRole;
    if (role == 1 || role == 2) { // 管理员或超级管理员
        return (
            <Col span={24} offset={2}>
                <Button type="primary" style={{marginRight: 5, marginTop: 10}}
                        onClick={props.onSearchData}>查询数据</Button>

                <Button type="primary" style={{marginRight: 5, marginTop: 10}}
                        onClick={props.onUpdataZnode}>修改数据</Button>

                <Popconfirm
                    title={'确定删除该节点以及其子节点吗？' + props.selectedKey}
                    onConfirm={props.deleteNode}>
                    <Button type="primary" style={{marginRight: 5, marginTop: 10}}>删除以及子节点</Button>
                </Popconfirm>

                <Button type="primary" onClick={props.onCreateZnode}>增加子节点</Button>
            </Col>
        )
    } else {
        return (
            <Col span={24} offset={2}>
                <Button type="primary" style={{marginTop: 10}}
                        onClick={props.onSearchData}>查询数据</Button>
            </Col>
        )
    }
}
