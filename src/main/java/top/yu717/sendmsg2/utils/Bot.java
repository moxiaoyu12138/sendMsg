package top.yu717.sendmsg2.utils;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.fluent.Request;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
public class Bot {

    private static final String HOOK_URL;
    private static boolean isSend;

    static {
        // 加载环境变量
        Dotenv dotenv = Dotenv.load();
        HOOK_URL = dotenv.get("HOOK_URL");

        // 加载 config.yaml 配置
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.yaml")) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(fis);
            log.info("YAML 配置内容: {}", config);

            // 获取 bot 配置，使用安全的类型转换方法
            Map<String, Object> botConfig = safeCast(config.get("bot"), Map.class);
            if (botConfig != null && botConfig.get("is_send") != null) {
                isSend = safeCast(botConfig.get("is_send"), Boolean.class);
            } else {
                log.warn("配置文件中缺少 'bot.is_send' 键或值为空，不发送消息。");
                isSend = false;
            }
        } catch (IOException e) {
            log.error("读取 config.yaml 异常：{}", e.getMessage());
            isSend = false; // 如果读取配置文件失败，默认不发送消息
        }
    }

    public static void sendWeChatMessage(String message) {
        if (!isSend) {
            log.info("消息推送已禁用。");
            return;
        }

        // 格式化当前时间
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String formattedMessage = String.format("%s %s", currentTime, message);

        // 发送HTTP请求
        try {
            String response = Request.post(HOOK_URL)
                    .bodyString("{\"msgtype\": \"text\", \"text\": {\"content\": \"" + formattedMessage + "\"}}",
                            org.apache.hc.core5.http.ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString();

            log.info("消息发送成功: {}", response);
        } catch (Exception e) {
            log.error("消息发送失败: {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        // 示例调用
        sendWeChatMessage("电脑正在关机，请查看原因");
    }

    /**
     * 通用的安全类型转换方法，避免未检查的类型转换警告
     * @param obj   要转换的对象
     * @param clazz 目标类型的 Class 对象
     * @param <T>   目标类型
     * @return 如果对象类型匹配，则返回转换后的对象，否则返回 null
     */
    @SuppressWarnings("unchecked")
    public static <T> T safeCast(Object obj, Class<T> clazz) {
        if (clazz.isInstance(obj)) {
            return (T) obj;
        } else {
            return null;
        }
    }
}
