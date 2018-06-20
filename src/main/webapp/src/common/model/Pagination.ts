import {action, computed, observable} from "mobx";
import {asyncAction} from "mobx-utils";
import {request} from "../../common/utils/request";
import qs from "qs";
import {provideInstance} from "../utils/IOC";

class PageVo {
    pageSize: number = 10;
    pageNum: number = 1;
    total: number = 0;
}

@provideInstance(Pagination)
class Pagination {
    page: PageVo = observable.object(new PageVo);
    path: string;
    searchVo:any = {};
    private queryList(params: any) {
        let param = params ? params : {};
        return request(`${this.path}?${qs.stringify(param)}`);
    }

    queryData(searchVo?: any) {
        this.queryDataInternal(searchVo? searchVo:{});
    }

    @asyncAction
    private * queryDataInternal(searchVo: any,resetPage:boolean=true,saveParam:boolean=true) {
        if(resetPage){
            this.page.pageNum = 1;
        }
        if(saveParam){
            this.searchVo = searchVo;
        }


        const result = yield this.queryList({...searchVo,...this.page});
        if (result.success) {
            let pageResult = result.data.page;
            this.page.total = pageResult.total;
            this.queryCallBack(true, result.data.data as Array<any>);
            return;
        }
        this.queryCallBack(false, []);
    }

    queryCallBack(success: boolean, list: Array<any>): void {
    }

    @action
    onPageSizeChange(pageNum: number, pageSize: number) {
        this.page.pageNum = pageNum;
        let resetPage = !(this.page.pageSize == pageSize);
        this.page.pageSize = pageSize;
        this.queryDataInternal(this.searchVo, resetPage, false);
    }

    @computed
    get pageConfig() {
        return {
            current: this.page.pageNum,
            pageSize: this.page.pageSize,
            showQuickJumper: true,
            showSizeChanger: true,
            pageSizeOptions: [
                '10',
                '20',
                '50',
                '100'
            ],
            total: this.page.total,
            onChange: (page, pageSize) => {
                this.onPageSizeChange(page, pageSize);
            },
            onShowSizeChange: (currentPage, pageSize) => {
                this.onPageSizeChange(currentPage, pageSize);
            },
            showTotal: (total) => {
                return `共 ${total} 条`;
            }
        }
    }
}

export default Pagination
