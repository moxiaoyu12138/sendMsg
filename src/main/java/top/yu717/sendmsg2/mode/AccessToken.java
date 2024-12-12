package top.yu717.sendmsg2.mode;

import lombok.Getter;

@Getter
public class AccessToken {
    private String token;
    // 凭证有效时间，单位：秒
    private int expiresIn;

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
