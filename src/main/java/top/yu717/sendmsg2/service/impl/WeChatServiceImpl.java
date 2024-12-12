package top.yu717.sendmsg2.service.impl;

import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yu717.sendmsg2.config.WeChatParamsUtil;
import top.yu717.sendmsg2.mode.Text;
import top.yu717.sendmsg2.mode.TextMessage;
import top.yu717.sendmsg2.service.WeChatService;
import top.yu717.sendmsg2.utils.WeChatUtil;

@Service
public class WeChatServiceImpl implements WeChatService {

    static final String sendMessage_url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";

    @Autowired
    private WeChatParamsUtil weChatParamsUtil;
    /**
     * @param uid     企业用户id
     * @param content 消息内容
     *                0.公共方法：发送消息
     */
    @Override
    public boolean sendMessage(String uid, String content) {
        if (null == uid ) {
            uid = "@all";
        }
        System.out.println(weChatParamsUtil.getAgentId()+"_______"+ weChatParamsUtil.getCorpsecret());
        // 1.获取access_token:根据企业id和应用密钥获取access_token,并拼接请求url
        String accessToken = WeChatUtil.getAccessToken(weChatParamsUtil.getCorpId(), weChatParamsUtil.getCorpsecret()).getToken();
        // 2.获取发送对象，并转成json
        String jsonMessage = getString(uid, content);
        // 3.获取请求的url
        String url = sendMessage_url.replace("ACCESS_TOKEN", accessToken);

        // 4.调用接口，发送消息
        JSONObject jsonObject = WeChatUtil.httpRequest(url, "POST", jsonMessage);

        // 4.错误消息处理
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                System.out.println("消息发送失败 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
                return false;
            }
        }
        return true;
    }

    @Override
    public String getString(String uid, String content) {
        Gson gson = new Gson();
        TextMessage message = new TextMessage();
        // 1.1非必需
        message.setTouser(uid); // 不区分大小写
        //message.setToparty("1");
        //message.getTouser(totag);
        // txtMsg.setSafe(0);
        // 1.2必需
        message.setMsgtype("text");
        message.setAgentid(weChatParamsUtil.getAgentId());
        Text text = new Text();
        text.setContent(content);
        message.setText(text);
        return gson.toJson(message);
    }
}
