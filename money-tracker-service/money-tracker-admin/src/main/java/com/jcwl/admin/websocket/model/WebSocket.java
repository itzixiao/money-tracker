package com.jcwl.admin.websocket.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述:WebSocket
 *
 * @author zixiao
 * @date 2022-05-01 20:00:00
 */
@Component
@ServerEndpoint("/websocket/{username}")
@Slf4j
public class WebSocket {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
     */
    private static ConcurrentHashMap<String, Session> clients = new ConcurrentHashMap<>();

    public static synchronized int getOnlineCount() {
        return clients.size();
    }

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        log.info("现在来连接的客户id:{}, 用户名:{}", session.getId(), username);
        clients.put(username, session);
        log.info("新用户:{}加入！ 当前在线人数:{}.", username, clients.size());
        // messageType 1:代表上线 2:代表下线 3:代表在线名单 4:代表普通消息
        // 先给所有人发送通知，说我上线了
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("messageType", 1);
        map.put("username", username);
        sendMessageAll(JSON.toJSONString(map));

        // 给自己发一条消息:告诉自己现在都有谁在线
        Map<String, Object> onlineUserMap = new LinkedHashMap<>();
        onlineUserMap.put("messageType", 3);
        // 移除掉自己
        Set<String> set = clients.keySet();
        onlineUserMap.put("onlineUsers", set);
        sendMessageTo(JSON.toJSONString(onlineUserMap), username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("服务端发生了错误 Session:{}", session.toString());
        log.info("服务端发生了错误 Throwable:{}", throwable.getMessage());
    }

    @OnClose
    public void onClose(@PathParam(value = "username") String username) {
        clients.remove(username);
        // messageType 1:代表上线 2:代表下线 3:代表在线名单 4:代表普通消息
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("messageType", 2);
        map.put("username", username);
        map.put("onlineUsers", clients.keySet());
        sendMessageAll(JSON.toJSONString(map));
        log.info("有用户退出！{} 当前在线人数{}", username, clients.size());
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            log.info("来自客户端消息:{}, 客户端的id是:{}", message, session.getId());
            JSONObject jsonObject = JSON.parseObject(message);
            String textMessage = jsonObject.getString("message");
            String fromusername = jsonObject.getString("form");
            String tousername = jsonObject.getString("to");
            // 如果不是发给所有，那么就发给某一个人
            // messageType 1:代表上线 2:代表下线 3:代表在线名单 4:代表普通消息
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("messageType", 4);
            map.put("textMessage", textMessage);
            map.put("fromusername", fromusername);
            if ("All".equals(tousername)) {
                map.put("tousername", "所有人");
                sendMessageAll(JSON.toJSONString(map));
            } else {
                map.put("tousername", tousername);
                sendMessageTo(JSON.toJSONString(map), tousername);
            }
        } catch (Exception e) {
            log.info("onMessage发生了错误:{}", e.getMessage());
        }
    }

    public void sendMessage(Session session, String message) throws IOException {
        if (session != null) {
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        }
    }

    public void sendMessageTo(String message, String toUserName) {
        if (StringUtils.isNotEmpty(toUserName)) {
            String[] sourceStrArray = toUserName.split(",");
            for (int i = 0; i < sourceStrArray.length; i++) {
                Session session = clients.get(sourceStrArray[i]);
                try {
                    sendMessage(session, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendMessageAll(String message) {
        for (Session session : clients.values()) {
            try {
                sendMessage(session, message);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public Set<String> onlineUsers() {
        Set<String> clientSet = clients.keySet();
        log.info("当前在线用户:{}", clientSet);
        return clientSet;
    }

}