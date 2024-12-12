package top.yu717.sendmsg2.mode;

import lombok.Getter;

/**
 * 文本消息
 *
 */
@Getter
public class TextMessage extends BaseMessage {
    // 文本
    private Text text;
    // 否 表示是否是保密消息，0表示否，1表示是，默认0
    private int safe;

    public void setText(Text text2) {
        this.text = text2;
    }

    public void setSafe(int safe) {
        this.safe = safe;
    }

}