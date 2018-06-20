import * as React from "react";
import Console from "react-console-component";
import {Tabs} from "antd";
import "../../themes/console.less";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {SSHModel} from "../model/SSHModel";

export interface StateI {
    promptLabel: string;
}

@observer
export default class SSHInit extends React.Component<{}, StateI> {

    @inject(SSHModel)
    private sshModel: SSHModel;

    constructor(props) {
        super(props);
        this.state = {
            promptLabel: "not connected> ",
        };
    }

    child: {
        console?: Console,
    } = {};

    echo = (text: string) => {
        this.sshModel.sshCommandExecute(text, this.child.console, () => {
            this.setState(
                {promptLabel: this.sshModel.hostIp ? 'zkdoctor@' + this.sshModel.hostIp + ":" + this.sshModel.currentDirectory + "> " : 'not connected>'},
                this.child.console.return);
        });
    };

    // TODO 后续加入多窗口功能
    // tabBarExtraContent={<Button icon="plus">新建窗口</Button>}

    render() {
        return (<Tabs
            defaultActiveKey="1"
            tabPosition="top"
            type="card"
        >
            <Tabs.TabPane tab="SSH终端" key="1">
                <div>
                    <label style={{fontWeight: "bold", textAlign: "left"}}>
                        PS：<br/>
                        1、连接目标服务器命令为：ssh&nbsp;&nbsp;&lt;hostIp&gt;
                        <br/>
                        2、退出目标服务器命令为：exit
                    </label>
                    <br/>
                    <br/>
                    <Console ref={ref => this.child.console = ref}
                             handler={this.echo}
                             promptLabel={this.state.promptLabel}
                             welcomeMessage={"************************ SSH终端，暂不支持交互式命令 ************************"}
                             autofocus={true}
                    />
                </div>
            </Tabs.TabPane>
        </Tabs>)
    }
}