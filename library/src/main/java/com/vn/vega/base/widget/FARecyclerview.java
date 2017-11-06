package com.vn.vega.base.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.vn.vega.adapter.multipleviewtype.IViewBinder;
import com.vn.vega.adapter.multipleviewtype.VegaBindAdapter;
import com.vn.vega.base.net.FARequest;
import com.vn.vega.base.net.request.RequestType;
import com.vn.vega.widget.RecyclerViewWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leobui on 11/2/2017.
 */

public class FARecyclerview extends RecyclerViewWrapper implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener {

    public FARecyclerview(Context context) {
        super(context);
    }

    public FARecyclerview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FARecyclerview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onRefresh() {

        offset =0;
        loadData(false);
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        offset = adapter.size() -1;
        loadData(true);
    }

    private Object container;
    private boolean isCanRefresh = false;
    private boolean isCanLoadMore = false;
    private int limit = 20;
    private int offset = 0;
    private RequestType type;
    private String path;
    private Map<String, String> params = new HashMap<>();
    private VegaBindAdapter adapter;
    private FARequest request;

    private String limitName = "limit";
    private String offsetName ="offset";
    public FARecyclerview api(FARequest request) {
        this.request = request;
        return this;
    }

    public FARecyclerview params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public FARecyclerview path(String path) {
        this.path = path;
        return this;
    }

    public FARecyclerview limit(int limit) {
        this.limit = limit;
        return this;
    }
    public FARecyclerview limit(String limitName, int limit) {
        this.limit = limit;
        this.limitName = limitName;
        return this;
    }
    public FARecyclerview offset(String offsetName, int offset) {
        this.offset = offset;
        this.offsetName = offsetName;
        return this;
    }
    public FARecyclerview offset(int offset) {
        this.offset = offset;
        return this;
    }

    public FARecyclerview container(Object container) {
        this.container = container;
        return this;
    }

    public FARecyclerview canLoadMore(boolean isCanLoadMore) {
        this.isCanLoadMore = isCanLoadMore;
        return this;
    }

    public FARecyclerview canRefresh(boolean isCanRefresh) {
        this.isCanRefresh = isCanRefresh;
        return this;
    }

    public FARecyclerview type(RequestType type) {
        this.type = type;
        return this;
    }

    public boolean isCanRefresh() {
        return isCanRefresh;
    }

    public void setCanRefresh(boolean canRefresh) {
        isCanRefresh = canRefresh;
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        isCanLoadMore = canLoadMore;
    }

    @Override
    public VegaBindAdapter getAdapter() {
        return adapter;
    }

    public void load() {
        if (isCanLoadMore) {
            setOnMoreListener(this);
        }
        if (isCanRefresh) {
            setRefreshListener(this);
        }
        loadData(false);
    }

    private void loadData(final boolean isLoadMore) {
        params.put(limitName, limit+"");
        params.put(offsetName, offset +"");
        type = type == null?this.request.getType():type;
        if (this.request != null) {
            this.request.addCallBack(new FARequest.RequestCallBack<List<IViewBinder>>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(Throwable t) {
                    Log.e("err", "err");
                    if (adapter == null){
                        adapter = new VegaBindAdapter();
                        setAdapter(adapter);
                    }
                    endData();
                }

                @Override
                public void onFinish(List<IViewBinder> result) {
                    if (adapter == null){
                        adapter = new VegaBindAdapter();
                        setAdapter(adapter);
                    }
                    if (!isLoadMore){
                        adapter.clear();
                    }
                    if (result != null && result.size() >0) {
                        adapter.addAllDataObject(result);
                        if (result.size() <limit){
                            endData();
                        }
                    }else{
                        endData();
                    }
                }
            })
                    .params(params)
                    .path(path)
                    .type(type == null?RequestType.GET:type)
                    .container(container == null? "recycler_request":container)
                    .doRequest();
        }
    }
}
