package com.cipher.ciphersystem.controller;


import com.cipher.ciphersystem.service.func.AESCrypto;
import com.cipher.ciphersystem.service.func.RSACoder;
import com.cipher.ciphersystem.util.RedisUtil;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cipher.ciphersystem.service.func.AESCrypto.*;
import org.bouncycastle.util.encoders.Base64;
import com.cipher.ciphersystem.service.func.RSACrt4ypto;

import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ApiController {
    @RequestMapping("/test")
    public String test(@RequestParam("id") String kkp) {
        AESCrypto aesCrypto = new AESCrypto();
        byte[] encrypto = kkp.getBytes();
        byte[] iv = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6};
        byte[] key = "admin123...Dozer".getBytes();
        byte[] cipher = aesCrypto.encrypt(encrypto,key,iv);

        return new String(Base64.encode(cipher));
    }
    @RequestMapping("/getKey")
    public String getKey() {
        return "{\"encryptKey\":\"1234567890123456\",\"iv\":\"1234567890123456\"}";
    }
    @RequestMapping("/getPublicKey")
    public String testRsa() throws Exception {
        Map<String, Object> keyMap = RSACoder.initKey();
        //公钥
        byte[] publicKey = RSACoder.getPublicKey(keyMap);

        //私钥
        byte[] privateKey = RSACoder.getPrivateKey(keyMap);
        System.out.println(privateKey.length);
        System.out.println("公钥：\n" + new String(Base64.encode(publicKey)));
        System.out.println("私钥：\n" + new String(Base64.encode(privateKey)));
        RedisUtil.set(new String(Base64.encode(publicKey)),new String(Base64.encode(privateKey)));
//        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
//        String str = "RSA密码交换算法";
//        System.out.println("\n===========甲方向乙方发送加密数据==============");
//        System.out.println("原文:" + str);
//        甲方进行数据的加密
//        byte[] code1 = RSACoder.encryptByPrivateKey(str.getBytes(), privateKey);
//        System.out.println("加密后的数据：" + new String(Base64.encode(code1)));
//        System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
        //乙方进行数据的解密
//        byte[] decode1 = RSACoder.decryptByPublicKey(code1, publicKey);
//        System.out.println("乙方解密后的数据：" + new String(decode1) + "\n\n");
//
//        System.out.println("===========反向进行操作，乙方向甲方发送数据==============\n\n");
//
//        str = kkp;
//
//        System.out.println("原文:" + str);
//
//        乙方使用公钥对数据进行加密
//        byte[] code2 = RSACoder.encryptByPublicKey(str.getBytes(), publicKey);
//        System.out.println("===========乙方使用公钥对数据进行加密==============");
//        System.out.println("加密后的数据：" + new String(Base64.encode(code2)));
//
//        System.out.println("=============乙方将数据传送给甲方======================");
//        System.out.println("===========甲方使用私钥对数据进行解密==============");

        //甲方使用私钥对数据进行解密
//        byte[] decode2 = RSACoder.decryptByPrivateKey(code2, privateKey);

//        System.out.println("甲方解密后的数据：" + new String(decode2));
        String ublicKeyString=new String(Base64.encode(publicKey));
        return "{\"publicKey\":\""+ublicKeyString+"\"}";
    }
    @PostMapping("/exchangeKey")
    public String exchageKey(@RequestParam(value = "user", required = true) String user,@RequestParam(value = "cipher", required = true) String cipher,@RequestParam(value = "publicKey", required = true) String publicKey) throws Exception {
        String privateKey= (String) RedisUtil.get(publicKey);
        System.out.println(privateKey);
        System.out.println(cipher);
        byte[] privates=Base64.decode(privateKey);
        System.out.println(privates.length);
        byte decode[] = RSACoder.decryptByPrivateKey(Base64.decode(cipher),privates);

        String AESkey= new String(decode);//;
        RedisUtil.set(user,AESkey);
        return "OK";
    }
    @PostMapping("/exchangefile")
    public String exchangeFile(@RequestParam("user") String user,@RequestParam("cipher") String cipher) {
        String AESKey= (String) RedisUtil.get(user);
        System.out.println(AESKey);
        AESCrypto aesCrypto = new AESCrypto();

        byte[] encrypto = Base64.decode(cipher);
        byte[] iv = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6};
        byte[] key = AESKey.getBytes();
        byte[] outputs = aesCrypto.decrypt(encrypto,key,iv);
        return new String(Base64.encode(outputs));

    }
}
