package com.lukou.publishervideo.utils;

import com.lukou.publishervideo.model.bean.EventBusMessage;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cxt on 2018/7/10.
 */

public class EventBusUtils {
    private static final String TAG = EventBusUtils.class.getSimpleName();

    /**
     * 注册EventBus
     *
     * @param subscriber 订阅者
     */
    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 注消EventBus
     *
     * @param subscriber 订阅者
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发布订阅事件
     *
     * @param eventBusMessage 发布者
     */
    public static void post(EventBusMessage eventBusMessage) {
        EventBus.getDefault().post(eventBusMessage);
    }

    /**
     * 发布粘性订阅事件
     *
     * @param eventBusMessage 发布者
     */
    public static void postSticky(EventBusMessage eventBusMessage) {
        EventBus.getDefault().postSticky(eventBusMessage);
    }


    /**
     * 移除指定的粘性订阅事件
     *
     * @param eventType 移除的内容
     * @param <T>
     */
    public static <T> void removeStickyEvent(Class<T> eventType) {
        T stickyEvent = EventBus.getDefault().getStickyEvent(eventType);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    /**
     * 取消事件传送 事件取消仅限于ThreadMode.PostThread下才可以使用
     * 不取消事件就会一直存在
     *
     * @param event
     */
    public static void cancelEventDelivery(Object event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }

    /**
     * 移除所有的粘性订阅事件
     */
    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    

}
