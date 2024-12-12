package top.yu717.sendmsg2.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 企业微信参数
 *
 */
@Component
@Data
public class WeChatParamsUtil {
    Dotenv dotenv = Dotenv.load();
    // 1.微信参数
    // 企业ID
    public   String corpId  = dotenv.get("corpId");
    // 企业应用私钥OA
    public   String corpsecret  = dotenv.get("corpsecret");
    // 企业应用的id
    public   int agentId = Integer.parseInt(dotenv.get("agentId"));
}