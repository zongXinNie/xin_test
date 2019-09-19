package com.leyou.common;

import com.leyou.entity.Payload;
import com.leyou.entity.UserInfo;
import com.leyou.utils.JwtUtils;
import com.leyou.utils.RsaUtils;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Nie ZongXin
 * @date 2019/9/18 23:23
 */
public class AuthTest {
    private String privateFilePath="D:\\key\\id_rsa";
    private String publicFilePath ="D:\\key\\id_rsa.pub";
    @Test
    public void testRSA() throws Exception {
//        生成密钥对
        RsaUtils.generateKey(publicFilePath,privateFilePath,"hello",2048);
//        获取私钥
        PrivateKey privateKey = RsaUtils.getPrivateKey(privateFilePath);
        System.out.println("privateKey = " + privateKey);
//        获取公钥
        PublicKey publicKey = RsaUtils.getPublicKey(publicFilePath);
        System.out.println("publicKey = " + publicKey);

    }
    @Test
    public void testJWT() throws Exception {
//        获取私钥
        PrivateKey privateKey = RsaUtils.getPrivateKey(privateFilePath);
//        生成token
        String token = JwtUtils.generateTokenExpireInMinutes(new UserInfo(1L, "jack", "guest"), privateKey, 5);
        System.out.println("token = " + token);

//        获取公钥
        PublicKey publicKey = RsaUtils.getPublicKey(publicFilePath);
//        解析token
        Payload<UserInfo> info = JwtUtils.getInfoFromToken(token, publicKey, UserInfo.class);
        System.out.println("info.getExpiration() = " + info.getExpiration());
        System.out.println("info.getUserInfo() = " + info.getUserInfo());
        System.out.println("info.getId() = " + info.getId());
    }

}
