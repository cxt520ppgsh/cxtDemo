package com.flyaudio.base.bean;

/**
 * Created by cxt on 2018/7/10.
 */

public class EventBusMessage {

    private String code;
    private Object msg;

    private EventBusMessage() {

    }

    public EventBusMessage(String code, Object msg) {
        this.code = code;
        this.msg = msg;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "EventBusMessage{" +
                "msg=" + msg +
                '}';
    }
}
