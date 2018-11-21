package com.vogue.sms.mvp.listener;

/**
 * Created by George on 2016/5/21.
 */

/**
 * @param <T>
 */
public interface BaseSingleLoadedListener<T> {
    /**
     * when data call back success
     *
     * @param data
     */
    void onSuccess(T data);

    /**
     * when data call back error and when data call back occurred exception
     *
     * @param msg
     */
    void onFailure(String msg);

}