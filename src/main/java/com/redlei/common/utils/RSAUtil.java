package com.redlei.common.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.redlei.common.exception.BaseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Enumeration;

public class RSAUtil {
    private static RSAUtil single = null;

    public static final String SIGNA_SHA256_WITH_RSA = "SHA256WithRSA";
    public static final String SIGN_SHA1_WITH_RSA = "SHA1WithRSA";
    public static final String SIGN_MD5_WITH_RSA = "MD5WithRSA";
    public static final String RSATrans = "RSA";
    /**
     * AES对称加密算法
     */
    public static final String KEY_ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
     */
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    /**
     * 算法/工作模式/填充方式.
     */
    private static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    /**
     * 编码 格式
     */
    private static String encoding = "UTF-8";

    /**
     * 静态工厂方法
     *
     * @param enc
     * @return
     */
    public static RSAUtil getInstance(String enc) {
        if (single == null) {
            synchronized (RSAUtil.class) {
                encoding = enc;
                single = new RSAUtil();
            }
        }
        return single;
    }

    /**
     * 签名(SHA256WithRSA)
     *
     * @param priKey
     * @param tobeSigned
     * @return
     * @throws BaseException
     */
    public String signSHA256(RSAPrivateCrtKey priKey, String tobeSigned) throws BaseException {
        try {
            Signature sign = Signature.getInstance(SIGNA_SHA256_WITH_RSA);
            sign.initSign(priKey);
            sign.update(tobeSigned.getBytes(encoding));
            byte signed[] = sign.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            throw new BaseException("签名失败");
        }
    }

    /**
     * 生成RSA签名
     *
     * @param content
     * @param input_charset
     * @param key
     * @param rsaType
     * @param isPub
     * @param isUseBase
     * @return
     * @throws Exception
     */
    public static String sign(String content, String input_charset, String key, boolean isPub, boolean isUseBase) {
        try {
            byte[] signed = sign(content, input_charset, key);
            if (isUseBase) {
                return new String(Base64.encodeBase64(signed));
            } else {
                return CodingUtil.bytesToHexString(signed);
            }
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

    }

    /**
     * 生成RSA签名
     *
     * @param content
     * @param input_charset
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] sign(String content, String input_charset, String key) throws Exception {
        byte[] signature = null;

        PublicKey publicKey = RSA.getPublicKey(key);

        signature = signByPubliceKey(content, input_charset, publicKey, RSATrans);

        return signature;
    }

    /**
     * 公钥加密
     *
     * @param TobeEncryptMsg
     * @param pubKey
     * @return
     * @throws BaseException
     */
    public String encryptMsgByPublic(String TobeEncryptMsg, X509Certificate pubKey) throws BaseException {

        // 获取公钥
        Cipher instance;
        try {
            instance = Cipher.getInstance(RSA_ALGORITHM);
            instance.init(Cipher.ENCRYPT_MODE, pubKey);
            return Base64.encode(instance.doFinal(Base64.decode(TobeEncryptMsg)));
        } catch (Exception e) {
            throw new BaseException("加密失败");
        }
    }

    /**
     * 公钥签名
     *
     * @param content
     * @param input_charset
     * @param key
     * @param transformation
     * @return
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    private static byte[] signByPubliceKey(String content, String input_charset, Key key, String transformation)
            throws UnsupportedEncodingException, Exception {
        Cipher cipher;
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] src = content.getBytes(input_charset);
            int len = src.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (len - offSet > 0) {
                if (len - offSet > 32) {
                    cache = cipher.doFinal(src, offSet, 32);
                } else {
                    cache = cipher.doFinal(src, offSet, len - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 32;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        }
    }

    /**
     * 解密
     *
     * @param content       密文
     * @param private_key   商户私钥
     * @param input_charset 编码格式
     * @return 解密后的字符串
     */
    public static String decryptByPrivate(String content, String private_key, String input_charset) {
        try {
            PrivateKey prikey = RSA.getPrivateKey(private_key);
            return decrypt(content, input_charset, prikey);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    private static String decrypt(String content, String input_charset, Key key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchProviderException {
        return decrypt(content, input_charset, key, "RSA", 256);
    }

    public static String decrypt(String content, String input_charset, Key key, String transformation, int contentSize)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedData = Base64.decode(content);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > contentSize) {
                cache = cipher.doFinal(encryptedData, offSet, contentSize);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * contentSize;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();

        return new String(decryptedData, input_charset);
    }

    /**
     * 获取私钥 [相对路径]
     */
    public RSAPrivateCrtKey getPriKeyByRelativePath(String keyFile, String passWord) throws BaseException {
        ClassLoader cl = RSAUtil.class.getClassLoader();
        InputStream fiKeyFile = cl.getResourceAsStream(keyFile);
        return getPriKey(fiKeyFile, passWord);
    }

    /**
     * 获取私钥
     */
    private RSAPrivateCrtKey getPriKey(InputStream priKey, String keyPassword) throws BaseException {
        String keyAlias;
        RSAPrivateCrtKey rsaPrikey = null;
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(priKey, keyPassword.toCharArray());
            Enumeration<?> myEnum = ks.aliases();
            while (myEnum.hasMoreElements()) {
                keyAlias = (String) myEnum.nextElement();
                if (ks.isKeyEntry(keyAlias)) {
                    rsaPrikey = (RSAPrivateCrtKey) ks.getKey(keyAlias, keyPassword.toCharArray());
                    break;
                }
            }
        } catch (Exception e) {
            if (priKey != null) {
                try {
                    priKey.close();
                } catch (IOException e1) {
                    throw new BaseException("流关闭异常");
                }
            }
            throw new BaseException("加载私钥失败");
        }

        if (rsaPrikey == null) {
            throw new BaseException("私钥不存在");
        }

        return rsaPrikey;
    }

    /**
     * 获取公钥 [相对路径]
     */
    public X509Certificate getPubKeyByRelativePath(String certFile) throws BaseException {
        ClassLoader cl = RSAUtil.class.getClassLoader();
        InputStream certfile = cl.getResourceAsStream(certFile);
        return getPublicKey(certfile);
    }

    /**
     * 获取公钥
     */
    private X509Certificate getPublicKey(InputStream pubKey) throws BaseException {
        X509Certificate x509cert;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            x509cert = (X509Certificate) cf.generateCertificate(pubKey);
        } catch (CertificateException e) {
            if (pubKey != null) {
                try {
                    pubKey.close();
                } catch (IOException e1) {
                    throw new BaseException("文件流关闭异常");
                }
            }
            throw new BaseException("初始化公钥异常");
        }
        return x509cert;
    }

    /**
     * 生成密钥
     *
     * @throws BaseException
     */
    public String genAESkey() throws BaseException {
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            kg.init(128);
            SecretKey secretKey = kg.generateKey();
            return Base64.encode(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new BaseException("gen the key failure.msg is : " + e.getMessage());
        }
    }

    /**
     * 加密数据
     */
    public String encryptByAES(String data, String key, String iv) throws Exception {
        Key k = toKey(Base64.decode(key));
        IvParameterSpec ivs = new IvParameterSpec(iv.getBytes());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k, ivs);
        return Base64.encode(cipher.doFinal(data.getBytes()));
    }

    /**
     * 解密数据
     */
    public String decryptByAES(String data, String key, String iv) throws Exception {
        Key k = toKey(Base64.decode(key));
        IvParameterSpec ivs = new IvParameterSpec(iv.getBytes());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k, ivs);
        return new String(cipher.doFinal(Base64.decode(data)));
    }

    /**
     * 转换密钥
     */
    private static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }
}
