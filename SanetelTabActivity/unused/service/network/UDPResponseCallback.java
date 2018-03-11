package com.edroplet.sanetel.services.network;

/**
 * Created on 2018/3/8.
 *
 * @author qxs
 *  自定义接口，用于处理接收到的UDP结果，主要是调用它唯一的方法onResponse,提高结果处理的灵活性
 */

public interface UDPResponseCallback {
    public void onResponse(String response,boolean isSuccess);
}
