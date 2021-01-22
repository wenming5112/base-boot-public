package com.base.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author .
 * @version 1.0.0
 * @date 2020/7/8 14:24
 **/
@Slf4j
public class Md5Utils {

    /**
     * MD5加密函数（32位小写）
     */
    public static String md5(String str) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(str.getBytes());
            int i;
            StringBuilder stringBuilder = new StringBuilder();
            byte[] bytes = bmd5.digest();
            for (byte b : bytes) {
                i = b;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(i));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 公钥加密
     * 描述：
     * 1字节 = 8位；
     * 最大加密长度如 1024位私钥时，最大加密长度为 128-11 = 117字节，不管多长数据，加密出来都是 128 字节长度。
     *
     * @param sourceStr          源消息
     * @param publicKeyBase64Str 公钥字符串
     * @return Base64String
     */
    public static String rsaEncrypt(String sourceStr, String publicKeyBase64Str) {
        byte[] publicBytes = Base64.decodeBase64(publicKeyBase64Str);
        // 公钥加密
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicBytes);
        List<byte[]> alreadyEncodeListData = new LinkedList<>();

        int maxEncodeSize = ConstantForSecurity.RSA_ENCODE_PART_SIZE - 11;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] sourceBytes = sourceStr.getBytes("utf-8");
            int sourceLen = sourceBytes.length;
            for (int i = 0; i < sourceLen; i += maxEncodeSize) {
                int curPosition = sourceLen - i;
                int tempLen = curPosition;
                if (curPosition > maxEncodeSize) {
                    tempLen = maxEncodeSize;
                }
                // 待加密分段数据
                byte[] tempBytes = new byte[tempLen];
                System.arraycopy(sourceBytes, i, tempBytes, 0, tempLen);
                byte[] tempAlreadyEncodeData = cipher.doFinal(tempBytes);
                alreadyEncodeListData.add(tempAlreadyEncodeData);
            }
            // 加密次数
            int partLen = alreadyEncodeListData.size();

            int allEncodeLen = partLen * ConstantForSecurity.RSA_ENCODE_PART_SIZE;
            //存放所有RSA分段加密数据
            byte[] encodeData = new byte[allEncodeLen];
            for (int i = 0; i < partLen; i++) {
                byte[] tempByteList = alreadyEncodeListData.get(i);
                System.arraycopy(tempByteList, 0, encodeData, i * ConstantForSecurity.RSA_ENCODE_PART_SIZE, ConstantForSecurity.RSA_ENCODE_PART_SIZE);
            }
            return Base64.encodeBase64String(encodeData);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 私钥解密 RSA
     *
     * @param sourceBase64RSA     源消息
     * @param privateKeyBase64Str 私钥
     */
    public static String rsaDecrypt(String sourceBase64RSA, String privateKeyBase64Str) {
        byte[] privateBytes = Base64.decodeBase64(privateKeyBase64Str);
        byte[] encodeSource = Base64.decodeBase64(sourceBase64RSA);
        int encodePartLen = encodeSource.length / ConstantForSecurity.RSA_ENCODE_PART_SIZE;
        // 所有解密数据
        List<byte[]> decodeListData = new LinkedList<>();
        String decodeStrResult;
        // 私钥解密
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 初始化所有被解密数据长度
            int allDecodeByteLen = 0;
            for (int i = 0; i < encodePartLen; i++) {
                byte[] tempEncodedData = new byte[ConstantForSecurity.RSA_ENCODE_PART_SIZE];
                System.arraycopy(encodeSource, i * ConstantForSecurity.RSA_ENCODE_PART_SIZE, tempEncodedData, 0, ConstantForSecurity.RSA_ENCODE_PART_SIZE);
                byte[] decodePartData = cipher.doFinal(tempEncodedData);
                decodeListData.add(decodePartData);
                allDecodeByteLen += decodePartData.length;
            }
            byte[] decodeResultBytes = new byte[allDecodeByteLen];
            for (int i = 0, curPosition = 0; i < encodePartLen; i++) {
                byte[] tempSorceBytes = decodeListData.get(i);
                int tempSourceBytesLen = tempSorceBytes.length;
                System.arraycopy(tempSorceBytes, 0, decodeResultBytes, curPosition, tempSourceBytesLen);
                curPosition += tempSourceBytesLen;
            }
            decodeStrResult = new String(decodeResultBytes, "UTF-8");
            return decodeStrResult;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | UnsupportedEncodingException | InvalidKeySpecException | IllegalBlockSizeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA 签名
     *
     * @param resource            加密数据
     * @param privateKeyBase64Str 私钥
     * @return Base64Str
     */
    public static String signatureByRsa(String resource, String privateKeyBase64Str) {
        try {
            byte[] privateBytes = Base64.decodeBase64(privateKeyBase64Str);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            // 签名 SHA256withRSA
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            byte[] bysData = resource.getBytes("UTF-8");
            sign.update(bysData);
            byte[] signByte = sign.sign();
            Base64 base64 = new Base64();
            return base64.encodeAsString(signByte);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException("签名验证失败");
        }

    }

    /**
     * 验证签名
     *
     * @param signStr            签名后的数据
     * @param verStr             原始数据
     * @param publicKeyBase64Str 公钥
     * @return Boolean
     */
    public static Boolean verifyByRsa(String signStr, String verStr, String publicKeyBase64Str) {
        try {
            byte[] publicBytes = Base64.decodeBase64(publicKeyBase64Str);
            //公钥加密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Base64 base64 = new Base64();
            byte[] signed = base64.decode(signStr);
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(publicKey);
            sign.update(verStr.getBytes("UTF-8"));
            return sign.verify(signed);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException("签名验证失败");
        }
    }

    /**
     * 创建公钥秘钥
     *
     * @return map
     */
    public static Map<String, String> createRSAKeys() {
        //里面存放公私秘钥的Base64位加密
        Map<String, String> keyPairMap = new HashMap<>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ConstantForSecurity.RSA_KEY_TYPE);
            keyPairGenerator.initialize(ConstantForSecurity.RSA_KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            //获取公钥秘钥
            String publicKeyValue = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
            String privateKeyValue = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());

            //存入公钥秘钥，以便以后获取
            keyPairMap.put(ConstantForSecurity.RSA_PUBLIC_KEY_NAME, publicKeyValue);
            keyPairMap.put(ConstantForSecurity.RSA_PRIVATE_KEY_NAME, privateKeyValue);
            return keyPairMap;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("当前JDK版本没找到RSA加密算法！");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCF2Bviwh62uKqlN5uVETTzJEvfulFINwzm3RvKAl8APIe3uEQgt6CHAlzx40yMXqGMvsc+cUcAFRRoyKkvjUDlw5cELn2eBtvZ660uGxL/YB7y9zXLNisrJNu+lcqYBqwbIL3iMrpPXkOqwiiRfDkpvypwLMUl+1fAcWF+iS9BJQIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIXYG+LCHra4qqU3m5URNPMkS9+6UUg3DObdG8oCXwA8h7e4RCC3oIcCXPHjTIxeoYy+xz5xRwAVFGjIqS+NQOXDlwQufZ4G29nrrS4bEv9gHvL3Ncs2Kysk276VypgGrBsgveIyuk9eQ6rCKJF8OSm/KnAsxSX7V8BxYX6JL0ElAgMBAAECgYBr6mApiAzNlL16MwjNuy7vL//BJu+sgz5y0io6yYihnyxqVDcSDhxvXtdj7Lnmn9Ivsg7h6OGvTk/DYK8Q+RouPNNolMbI/2cA4QPqKr+F741o8drtdVGDjv/lpV8SitzJu+CAOtG5zYj34vfPIy0z3Ve8Pr8hrHxllX5YOcqDgQJBAL6EKnsZX7rk8C9gp9xsKiwEkl7JMPbIxK58+9Zrb1nvUo0ezqU3JNYv0MMtcVU7FJDGRyniAYhxt2udkvIyHcUCQQCz2UYTiIgKzY/camQnb/GS6GHZUeWbJzt0CuBGW1O+odqIZl5OKHwEvjlPuEDdK/x9Z+DLIyEjCuwxLeFsxyvhAkA0cfujwXtp3oYD9M6gziHZ1jRY5XeWJ/SEuCE3iIGxt0D5Wia2snRwhd8pK4RMWyQxKnQCopWbcvp0JH+ELqmlAkA2KbQiA9c8saWmst/QxLTTEmsNgM2OSWVtUC004yI4YhQnSfUSwrx+zS8DZDEcqreSqOsZIk5DeHGjafbexKjhAkEAqLqFD5A/Od21LooeFGCftzlROmiy0y/uaAb4ntqdPWPSbXkHJdnqJ9XKI2iK+CaSIjFhYkZuC7cm1K6kXrFWBw==";

        JSONObject data = new JSONObject();
        data.put("tel", "18692345390");

        // 加密之后的内容
        String encryptStr = rsaEncrypt(data.toJSONString(), pubKey);
        System.out.println(encryptStr);

        // 解密密文
        String decodeStr = rsaDecrypt(encryptStr, privateKey);
        System.out.println(decodeStr);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("parameter", "K5yhZYjoRuh1MRU814Ia580LkXJNJmO+e/MrLFf75yWvfchjP12uO+iPQhpp1IRiRe/iSqkVuotLxFWKRxXLnFMeTkrt0VTG5wP31g/jt4f3NcAMsKKB80J4OXTz4ZIKDd3fQMaZ//bYCman7AGj977GJV0lXIOmsLQXp0Z6QJU3AEo64d3ncxsUkpVGVIjNOx/MqzSRiPYgWitqPy/tEFk6f5fhGtoSi8CUudRiGcKEO9MnZxRTgqPHZY4aK+qPMDHmwgrDiAA+iTzbfuhzlNioHr9Kfan1COnTF7TqKdLdxHpdCAnocRSrIpxIZ+rhaUefBZIChg6PIrbguyquvQSjDTl2T4qh8AMfOHpysmQ2T3n8wLxGl3If0CPa4+GJCkwIyOdx7UEiPWRhpAgeTeehCpG6QWdIOS4ISmB6jwL8US+58QwlZCT/4n2AoIyfVtOO012p+XEreC5b4kkC564+s21BhSjP9NQBXkyhc6w6bHPq41ILzrz7d7T/lJTjLJm3jnG/FFy7XsxhizNPEu8bWnN0JpeSSkwuOazeASyyPFdu5ihz0qzE002aZJ5wopA3Y2fbr3s1YD6tFzuZ/8aZ0OVCgHdYhcFGyRObmco6MPq/49J5QySqUs2Hr5e0YhXpWLcW2WrvjGDLSyNvR3L7OLswdOqyYhoNb5YYFks=");

        String url = "http://localhost:8030/wallet/transfer";

        String res = HttpRequestUtil.doPost(url, StandardCharsets.UTF_8, parameter);

        System.out.println("请求结果：" + res);
    }
}
