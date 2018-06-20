import React, {Component} from "react";
import {observer} from "mobx-react";
import {Link, withRouter} from "react-router";
import {inject} from "../../../common/utils/IOC";
import {Button, notification, Tabs} from "antd";
import {ClusterModel, ZnodeDetailInfoVo} from "../../model/ClusterModel";
import HeadInfo from "../clusterDetail/HeadInfo";
import {SysUserModel, SysUserVo} from "../../../sys/model/SysUserModel";
import {ClusterZnodeList} from "./ClusterZnodeList";
import UpdateZnodeModal from "./UpdateZnodeModal";
import CreateNewZnodeModal from "./CreateNewNodeModal";

interface ClusterZnodeState {
    clusterInfoId: number;
    clusterName: string;
    updatePath: string;
    showUpdateZnodeModal: boolean;
    parentPath: string;
    showCreateZnodeModal: boolean;
}

interface ClusterZnodePropsI {
    location: any;
    router: any;
    sysUser: SysUserVo;
}

@observer
class ClusterZnodeDetail extends Component<ClusterZnodePropsI, ClusterZnodeState> {

    @inject(ClusterModel)
    private clusterModel: ClusterModel;

    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    constructor(props) {
        super(props);
        this.state = {
            clusterInfoId: null,
            clusterName: '',
            updatePath: '',
            showUpdateZnodeModal: false,
            parentPath: '',
            showCreateZnodeModal: false,
        }
    }

    componentDidMount(): void {
        let pathname = this.props.location.pathname;
        let strArray = pathname.split('/');
        this.loadClusterZnodes(strArray[4]);
        this.setState({
            clusterInfoId: strArray[4],
            clusterName: strArray[3]
        });
    }

    loadClusterZnodes(clusterInfoId: number) {
        this.clusterModel.getClusterRootZnodes(clusterInfoId);
    }

    loadChildrenZnodes(clusterInfoId: number, znode: string, callback: any) {
        this.clusterModel.getClusterZnodesChildren(clusterInfoId, znode, callback);
    }

    getClusterZnodesProps() {
        let thisV = this;
        return {
            clusterInfoId: thisV.state.clusterInfoId,
            sysUser: this.sysUserModel.sysUser,
            znodeRoots: thisV.clusterModel.znodeRoots ? thisV.clusterModel.znodeRoots : [],
            znodeChildren: thisV.clusterModel.znodeChildren ? thisV.clusterModel.znodeChildren : [],
            znodeDataDetail: thisV.clusterModel.znodeDataDetail ? thisV.clusterModel.znodeDataDetail : new ZnodeDetailInfoVo(),
            initChildren(clusterInfoId, node, callback) {
                thisV.loadChildrenZnodes(clusterInfoId, node, callback);
            },
            onDeleteZnode(znode, callback) {
                thisV.clusterModel.deleteZnode(thisV.state.clusterInfoId, znode, callback);
            },
            onSearchData(znode, serializable) {
                thisV.clusterModel.searchZnodeData(thisV.state.clusterInfoId, znode, serializable);
            },
            onUpdateZnode(path) {
                thisV.setState({showUpdateZnodeModal: true, updatePath: path});
            },
            onCreateZnode(path) {
                thisV.setState({showCreateZnodeModal: true, parentPath: path});
            }
        };
    };

    getHeadInfoProps() {
        let thisV = this;
        return {
            clusterName: thisV.state.clusterName
        }
    }

    getModifyZnodeModal() {
        let thisV = this;
        return {
            clusterId: thisV.state.clusterInfoId,
            updatePath: this.state.updatePath,
            onOk: function (item) {
                thisV.clusterModel.updateZnode(item, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });

                thisV.setState({showUpdateZnodeModal: false});
            },
            onCancel() {
                thisV.setState({showUpdateZnodeModal: false});
            },
        }
    }

    getCreateZnodeModal() {
        let thisV = this;
        return {
            clusterId: thisV.state.clusterInfoId,
            parentPath: this.state.parentPath,
            onOk: function (item) {
                thisV.clusterModel.createZnode(item, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });

                thisV.setState({showCreateZnodeModal: false});
            },
            onCancel() {
                thisV.setState({showCreateZnodeModal: false});
            },
        }
    }

    render() {
        if (!this.state.clusterInfoId) {
            return <h3>找不到对应的集群信息!</h3>
        }

        const handleChange = (index) => {
            if (index == 1) {
                this.loadClusterZnodes(this.state.clusterInfoId)
            }
        };

        return (
            <div>
                <HeadInfo {...this.getHeadInfoProps()} />
                {this.state.showUpdateZnodeModal ? <UpdateZnodeModal {...this.getModifyZnodeModal()}/> : ''}
                {this.state.showCreateZnodeModal ? <CreateNewZnodeModal {...this.getCreateZnodeModal()}/> : ''}
                <Tabs defaultActiveKey="1" tabPosition="top" type="card"
                      tabBarExtraContent={<Link to={'/cluster/list'}><Button>返回集群列表</Button></Link>}
                      onChange={handleChange}>
                    <Tabs.TabPane tab="节点信息" key="1">
                        <ClusterZnodeList {...this.getClusterZnodesProps()}/>
                    </Tabs.TabPane>
                </Tabs>
            </div>
        );
    }
}

export default withRouter(ClusterZnodeDetail);
