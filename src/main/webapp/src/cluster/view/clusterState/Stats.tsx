import React, {Component} from "react";
import {DatePicker} from "antd";
import ReactEcharts from "echarts-for-react";
import {Link} from "react-router";
import moment from "moment/moment";

export interface ClusterStatsProps {
    // 各个指标对应的数据
    receivedChart: Array<any>;
    outstandingsChart: Array<any>;
    maxLatencyMaxChart: Array<any>;
    avgLatencyMaxChart: Array<any>;
    znodeCountChart: Array<any>;
    ephemeralsChart: Array<any>;
    watcherTotalChart: Array<any>;
    connectionTotalChart: Array<any>;
    // 各个指标时间更改触发方法
    receivedDateChange: any;
    outstandingsDateChange: any;
    maxLatencyMaxDateChange: any;
    avgLatencyMaxDateChange: any;
    znodeCountDateChange: any;
    ephemeralsDateChange: any;
    watcherTotalDateChange: any;
    connectionTotalDateChange: any;
}

export class ClusterStats extends Component<ClusterStatsProps, {}> {

    constructor(props) {
        super(props);
    }

    getReceivedChart() {
        return this.getLineChart("总收包数", this.props.receivedChart['time'] ? this.props.receivedChart['time'].slice() : [],
            this.props.receivedChart['value'] ? this.props.receivedChart['value'].slice() : [], "个");
    }

    getOutStandingsChart() {
        return this.getLineChart("总堆积请求数", this.props.outstandingsChart['time'] ? this.props.outstandingsChart['time'].slice() : [],
            this.props.outstandingsChart['value'] ? this.props.outstandingsChart['value'].slice() : [], "个");
    }

    getMaxLatencyMaxChart() {
        return this.getLineChart("最大实例延迟时长", this.props.maxLatencyMaxChart['time'] ? this.props.maxLatencyMaxChart['time'].slice() : [],
            this.props.maxLatencyMaxChart['value'] ? this.props.maxLatencyMaxChart['value'].slice() : [], "毫秒");
    }

    getAvgLatencyMaxChart() {
        return this.getLineChart("平均实例延迟时长", this.props.avgLatencyMaxChart['time'] ? this.props.avgLatencyMaxChart['time'].slice() : [],
            this.props.avgLatencyMaxChart['value'] ? this.props.avgLatencyMaxChart['value'].slice() : [], "毫秒");
    }

    getZnodeCountChart() {
        return this.getLineChart("节点数", this.props.znodeCountChart['time'] ? this.props.znodeCountChart['time'].slice() : [],
            this.props.znodeCountChart['value'] ? this.props.znodeCountChart['value'].slice() : [], "个");
    }

    getEphemeralsChart() {
        return this.getLineChart("临时节点数", this.props.ephemeralsChart['time'] ? this.props.ephemeralsChart['time'].slice() : [],
            this.props.ephemeralsChart['value'] ? this.props.ephemeralsChart['value'].slice() : [], "个");
    }

    getWatcherTotalChart() {
        return this.getLineChart("总watcher数", this.props.watcherTotalChart['time'] ? this.props.watcherTotalChart['time'].slice() : [],
            this.props.watcherTotalChart['value'] ? this.props.watcherTotalChart['value'].slice() : [], "个");
    }

    getConnectionTotalChart() {
        return this.getLineChart("总连接数", this.props.connectionTotalChart['time'] ? this.props.connectionTotalChart['time'].slice() : [],
            this.props.connectionTotalChart['value'] ? this.props.connectionTotalChart['value'].slice() : [], "个");
    }

    getLineChart(seriesName, xAxisData, yAxisData, unit) {

        let lineTooltip = {
            trigger: "axis",
            axisPointer: {
                type: 'cross'
            },
            formatter: function (params) {
                let result = params[0].name + '<br/>';
                for (var i = 0; i < params.length; i++) {
                    result += params[i].seriesName + ' : ' + params[i].value + ' ' + unit + '<br/>'
                }
                return result;
            }
        };

        return {
            tooltip: lineTooltip,
            calculable: true,
            xAxis: [
                {
                    type: "category",
                    boundaryGap: false,
                    data: xAxisData,
                }
            ],
            yAxis: {
                type: "value",
            },
            series: [
                {
                    name: seriesName,
                    type: "line",
                    data: yAxisData,
                    markLine: {
                        label: {
                            normal: {
                                position: 'middle',
                                formatter: '平均值：' + '{c}' + unit
                            }
                        },
                        data: [{
                            type: "average",
                            name: "平均值",
                        }, [{
                            symbol: 'none',
                            x: '80%',
                            y: '20%',
                            yAxis: 'max'
                        }, {
                            type: 'max',
                            symbol: 'circle',
                            label: {
                                normal: {
                                    position: 'right',
                                    formatter: '最大值：' + '{c}' + unit
                                }
                            },
                        }]
                        ]
                    }
                },
            ],
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
        let receivedDateChange = (dates) => {
            this.props.receivedDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let outstandingsDateChange = (dates) => {
            this.props.outstandingsDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let maxLatencyMaxDateChange = (dates) => {
            this.props.maxLatencyMaxDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let avgLatencyMaxDateChange = (dates) => {
            this.props.avgLatencyMaxDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let znodeCountDateChange = (dates) => {
            this.props.znodeCountDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let ephemeralsDateChange = (dates) => {
            this.props.ephemeralsDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let watcherTotalDateChange = (dates) => {
            this.props.watcherTotalDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let connectionTotalDateChange = (dates) => {
            this.props.connectionTotalDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };

        return (<div>
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                borderBottom: '1px #d7d8d9 solid',
                paddingTop: 20,
                paddingBottom: 20
            }}>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>收包数（个）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={receivedDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getReceivedChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>堆积请求数（个）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={outstandingsDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getOutStandingsChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
            </div>

            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                borderBottom: '1px #d7d8d9 solid',
                paddingTop: 20,
                paddingBottom: 20
            }}>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>最大实例延迟时长（ms）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={maxLatencyMaxDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getMaxLatencyMaxChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>平均实例延迟时长（ms）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={avgLatencyMaxDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getAvgLatencyMaxChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
            </div>

            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                borderBottom: '1px #d7d8d9 solid',
                paddingTop: 20,
                paddingBottom: 20
            }}>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>节点数（个）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={znodeCountDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getZnodeCountChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>临时节点数（个）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={ephemeralsDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getEphemeralsChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
            </div>

            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                borderBottom: '1px #d7d8d9 solid',
                paddingTop: 20,
                paddingBottom: 20
            }}>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>总watcher数（个）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={watcherTotalDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getWatcherTotalChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>总连接数（个）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={connectionTotalDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getConnectionTotalChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
            </div>
        </div>)
    };
}
;
