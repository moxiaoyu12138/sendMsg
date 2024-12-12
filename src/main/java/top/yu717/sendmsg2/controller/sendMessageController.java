package top.yu717.sendmsg2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yu717.sendmsg2.mode.SubPush;
import top.yu717.sendmsg2.service.WeChatService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class sendMessageController {

  @Autowired
  private WeChatService weChatService;


    @GetMapping("/send")
    public String sendMessage(@RequestParam("mag") String content){
        boolean b = weChatService.sendMessage("@all", content);
        return b?"发送成功":"发送失败";
    }

    @PostMapping("/send_msg")
    public String sendMessagePost(@RequestBody String jsonString){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            SubPush data = objectMapper.readValue(jsonString, SubPush.class);
            String msg = String.join("",
                    "更新项目：" + (data.getTitle() != null ? data.getTitle() : ""),
                    ", 更新内容：" + (data.getShareName() != null ? data.getShareName() : ""),
                    ", 文件大小：" + (data.getSize() != null ? data.getSize() : ""),
                    ", 更新时间：" + (data.getUpdatedAt() != null ? data.getUpdatedAt() : ""),
                    " 消息：" + (data.getMsg() != null ? data.getMsg() : "")
            );
            weChatService.sendMessage(null, msg);
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "发送成功";
    }

}
