package top.yu717.sendmsg2.mode;

import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

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

    public static void main(String[] args) throws Exception {
        byte[] originalKey = "这是一个15字节的密钥".getBytes();

        // 检查密钥长度
        if (originalKey.length != 16 && originalKey.length != 24 && originalKey.length != 32) {
            System.out.println("原始密钥长度不正确，将生成新的密钥。");
            originalKey = generateValidAesKey();
        }

        System.out.println("修正后的密钥长度为 " + originalKey.length + " 字节" + originalKey);
        System.out.println("修正后的密钥为: " + Arrays.toString(originalKey));

        try {
            // 使用修正后的密钥进行加密
            byte[] encryptedData = encryptData("要加密的数据", originalKey);
            System.out.println("加密后的数据: " + Arrays.toString(encryptedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] generateValidAesKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(new byte[16]); // 设置随机种子
        keyGen.init(128, secureRandom); // 生成128位密钥
        SecretKey secretKey = keyGen.generateKey();
        return secretKey.getEncoded();
    }

    private static byte[] encryptData(String data, byte[] key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return encryptedData;
    }
}