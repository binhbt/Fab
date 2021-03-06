package com.example.binhbt.demo.net;

import com.example.binhbt.demo.model.User;
import com.google.gson.reflect.TypeToken;
import com.vn.fa.base.data.net.request.RequestType;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;


/**
 * Created by leobui on 11/9/2017.
 */

public class UserListRequest  extends BaseRequest{
    @Override
    protected String getPath() {
        return "test/user_list.json";
    }

    @Override
    protected Type getDataType() {
        Type type = new TypeToken<List<User>>() {}.getType();
        return type;
    }



    @Override
    public RequestType getType() {
        return RequestType.GET;
    }


    @Override
    public Observable getApi() {
        return super.getApi();
    }
}
