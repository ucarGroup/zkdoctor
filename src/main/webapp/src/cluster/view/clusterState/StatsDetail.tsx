import React, {Component} from "react";
import {DatePicker, Radio, Select} from "antd";
import ReactEcharts from "echarts-for-react";
import moment from "moment/moment";
import "moment/locale/zh-cn";
moment.locale('zh-cn');

export interface ClusterStatsDetailProps {
    indicator: string;
    allInsChart: Array<any>;
    handleDatePickerChange: any;
    handleTimeChange: any;
    indicatorChange: any;
}

export class ClusterStatsDetail extends Component<ClusterStatsDetailProps, any> {

    constructor(props) {
        super(props);
    }

    getAllInsChart() {
        let xAxisData = this.props.allInsChart['time'] ? this.props.allInsChart['time'].slice() : [];
        let yAxisDataArrayAll = this.props.allInsChart['value'] ? this.props.allInsChart['value'] : [];
        let yAxisDataArray = [];
        let keyName = [];
        for (let [key, value] of Object.entries(yAxisDataArrayAll)) {
            yAxisDataArray.push(value.slice());
            keyName.push(key);
        }
        let seriesAll = [];
        for (let j = 0; j < yAxisDataArray.length; j++) {
            seriesAll.push({
                name: keyName[j],
                type: "line",
                data: yAxisDataArray[j],
            })
        }

        let yAxisConfig = {
            type: "value",
        };

        let isServerState;
        let unit = "个";
        if (this.props.indicator === "serverStateLag") {
            isServerState = true;
            unit = "";
            yAxisConfig = {
                type: "value",
                max: 4,
                axisLabel: {
                    formatter: function (value) {
                        if (value == "0") {
                            return 'follower';
                        } else if (value == "1") {
                            return 'leader';
                        } else if (value == "2") {
                            return 'observer';
                        } else if (value == "3") {
                            return 'standalone';
                        } else {
                            return 'null';
                        }
                    }
                }
            };
        } else if (this.props.indicator == "maxLatency" || this.props.indicator == "avgLatency"
            || this.props.indicator == "minLatency") {
            unit = "毫秒"
        } else if (this.props.indicator == "approximateDataSize") {
            unit = "byte";
        }

        let lineTooltip = {
            trigger: "axis",
            axisPointer: {
                type: 'cross'
            },
            formatter: function (params) {
                let result = params[0].name + '<br/>';
                if (isServerState == true) {
                    for (var i = 0; i < params.length; i++) {
                        let value = "null";
                        if (params[i].value == "0") {
                            value = 'follower';
                        } else if (params[i].value == "1") {
                            value = 'leader';
                        } else if (params[i].value == "2") {
                            value = 'observer';
                        } else if (params[i].value == "3") {
                            value = 'standalone';
                        }
                        result += params[i].seriesName + ' : ' + value + ' ' + unit + '<br/>'
                    }
                } else {
                    for (var i = 0; i < params.length; i++) {
                        result += params[i].seriesName + ' : ' + params[i].value + ' ' + unit + '<br/>'
                    }
                }
                return result;
            }
        };

        return {
            tooltip: lineTooltip,
            calculable: true,
            legend: {
                data: keyName,
            },
            xAxis: [
                {
                    type: "category",
                    boundaryGap: false,
                    data: xAxisData,
                }
            ],
            yAxis: yAxisConfig,
            series: seriesAll,
            grid: {
                left: 80,
                right: 30,
                top: 30,
                bottom: 30
            },
            dataZoom: {
                show: true,
                type: 'inside',
            },
            toolbox: {
                show: true,
                orient: 'horizontal',
                left: 'right',
                feature: {
                    dataView: {show: true, readOnly: true},
                    restore: {show: true},
                }
            },
        }
    }

    render() {
        const RadioButton = Radio.Button;
        const RadioGroup = Radio.Group;
        return (<div>
            <div>
                <span style={{fontWeight: 'bold'}}>时间选择：</span>
                <RadioGroup defaultValue="c" size="small" style={{marginLeft: 5}}
                            onChange={this.props.handleTimeChange}>
                    <RadioButton value="a">30m</RadioButton>
                    <RadioButton value="b">1h</RadioButton>
                    <RadioButton value="c">6h</RadioButton>
                    <RadioButton value="d">12h</RadioButton>
                    <RadioButton value="e">1d</RadioButton>
                    <RadioButton value="f">3d</RadioButton>
                    <RadioButton value="g">7d</RadioButton>
                    <RadioButton value="h">14d</RadioButton>
                </RadioGroup>
                <DatePicker.RangePicker style={{marginLeft: 5, marginRight: 30}}
                                        defaultValue={[moment().subtract(6, 'hours'), moment()]}
                                        showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                        onOk={this.props.handleDatePickerChange}/>
            </div>
            <div style={{marginTop: 15}}>
                <span style={{marginRight: 5, fontWeight: 'bold'}}>统计指标：</span>
                <Select style={{width: 300}}
                        defaultValue={'received'}
                        onChange={this.props.indicatorChange}>
                    <Select.Option value='received'>收包数</Select.Option>
                    <Select.Option value='outstandings'>堆积请求数</Select.Option>
                    <Select.Option value='send'>发包数</Select.Option>
                    <Select.Option value='maxLatency'>最大延迟时长</Select.Option>
                    <Select.Option value='avgLatency'>平均延迟时长</Select.Option>
                    <Select.Option value='minLatency'>最小延迟时长</Select.Option>
                    <Select.Option value='znodeCount'>节点数</Select.Option>
                    <Select.Option value='ephemerals'>临时节点数</Select.Option>
                    <Select.Option value='watcherCount'>watcher数</Select.Option>
                    <Select.Option value='connections'>连接数</Select.Option>
                    <Select.Option value='approximateDataSize'>数据大小</Select.Option>
                    <Select.Option value='openFileDescriptorCount'>打开文件描述符数</Select.Option>
                    <Select.Option value='maxFileDescriptorCount'>最大文件描述符数</Select.Option>
                    <Select.Option value='followers'>follower数</Select.Option>
                    <Select.Option value='syncedFollowers'>同步follower数</Select.Option>
                    <Select.Option value='pendingSyncs'>待同步follower数</Select.Option>
                    <Select.Option value='serverStateLag'>实例角色标识</Select.Option>
                </Select>
            </div>
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                borderBottom: '1px #d7d8d9 solid',
                paddingTop: 20,
                paddingBottom: 20
            }}>
                <div style={{width: '100%'}}>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getAllInsChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
            </div>
        </div>)
    };
}
;

