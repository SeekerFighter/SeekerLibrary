package com.seeker.demo.OKHttpManger.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seeker on 2016/7/27.
 */
public class Result {

    private String msg;

    private int status;

    private List<Info> data = new ArrayList<>();

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Info> getData() {
        return data;
    }

    public void setData(List<Info> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Result{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", data=" + data.size() +
                '}';
    }
}
