package top.yu717.sendmsg2.service;


import org.springframework.stereotype.Service;


/**
 * 发送消息
 *
 */
@Service
public interface WeChatService {



    /**
     * @param uid     企业用户id
     * @param content 消息内容
     *  0.公共方法：发送消息
     */
    public  boolean sendMessage(String uid, String content);

    public String getString(String uid, String content);




}