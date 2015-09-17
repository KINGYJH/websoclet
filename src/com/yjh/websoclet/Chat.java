package com.yjh.websoclet;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YJH on 2015/9/1.
 */
@ServerEndpoint(value = "/websocket.ws", configurator = GetHttpSessionConfigurator.class)
public class Chat {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");    // 日期格式化

    //httpSession
    private HttpSession httpSession;

    /**
     * 打开连接时触发
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Websocket打开连接:" + session.getId());
    }

    /**
     * 收到客户端消息时触发
     *
     * @param message
     * @param session
     * @return
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("发送信息：" + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        jsonObject.put("date", DATE_FORMAT.format(new Date()));
        for (Session openSession : session.getOpenSessions()) {
            jsonObject.put("isSelf", openSession.equals(session));
            openSession.getAsyncRemote().sendText(jsonObject.toString());
        }
    }

    /**
     * 异常时触发
     *
     * @param session
     */
    @OnError
    public void onError(Throwable throwable, Session session) {
        System.out.println("Websocket 连接异常:" + session.getId() + "/t" + throwable.getMessage());
    }

    /**
     * 关闭连接时触发
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("Websocket 关闭连接:" + session.getId());
    }

}
