package top.yu717.sendmsg2.mode;

import lombok.Getter;

/**
 * 文本
 *
 */
@Getter
public class Text {
    //是    消息内容，最长不超过2048个字节
    private String content;

    public void setContent(String content) {
        this.content = content;
    }
}