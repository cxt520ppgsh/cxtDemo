package com.lukou.publishervideo.model.bean;

/**
 * Created by cxt on 2018/6/15.
 */

public class Asiginer {
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAsignerName() {
        return asignerName;
    }

    public void setAsignerName(String asignerName) {
        this.asignerName = asignerName;
    }

    public int getAsignCnt() {
        return asignCnt;
    }

    public void setAsignCnt(int asignCnt) {
        this.asignCnt = asignCnt;
    }

    public String getUpdatecntTime() {
        return updatecntTime;
    }

    public void setUpdatecntTime(String updatecntTime) {
        this.updatecntTime = updatecntTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String updateTime;
    private String createTime;
    private String asignerName;
    private int asignCnt;
    private String updatecntTime;
    private int id;
}
