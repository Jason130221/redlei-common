package com.redlei.common.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public class RSA {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";
	public static final int KEY_SIZE = 2048;
	private static KeyPairGenerator keyPairGenerator;
	static {
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 生成密钥对
	 * 
	 * @return
	 */
	public static Map<String, byte[]> generateKeyBytes() {
//		try {
//			keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
//			keyPairGenerator.initialize(KEY_SIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			Map<String, byte[]> keyMap = new HashMap<String, byte[]>();

			keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
			keyMap.put(PRIVATE_KEY, privateKey.getEncoded());
			return keyMap;
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
	}

	/**
	 * 还原公钥
	 * 
	 * @param keyBytes
	 * @return
	 */
	public static PublicKey restorePublicKey(byte[] keyBytes) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 还原私钥
	 * 
	 * @param keyBytes
	 * @return
	 */
	public static PrivateKey restorePrivateKey(byte[] keyBytes) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 获取默认公钥
	 * @return
	 */
	public static PublicKey getGeneratePublicKey() {
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair.getPublic();
	}

	/**
	 * 获得默认私钥
	 * @return
	 */
	public static PrivateKey getGeneratePrivateKey() {
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair.getPrivate();
	}
	
	

	/**
	 * 得到私钥
	 * @param key
	 * @return 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes = buildPKCS8Key(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		
		return privateKey;
	}
	
	public static PrivateKey getSHA256WithRSA(String key) throws Exception{
		 if (key == null || key.isEmpty()) {
	            return null;
	        }

		 KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		 InputStream ins=new ByteArrayInputStream(key.getBytes());
	        byte[] encodedKey = StreamUtil.readText(ins).getBytes();

	        encodedKey = Base64.decodeBase64(encodedKey);

	        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
	}

	private static byte[] buildPKCS8Key(String privateKey) throws IOException {
		if (privateKey.contains("-----BEGIN PRIVATE KEY-----")) {
			return Base64.decode(privateKey.replaceAll("-----\\w+ PRIVATE KEY-----", ""));
		} else if (privateKey.contains("-----BEGIN RSA PRIVATE KEY-----")) {
			final byte[] innerKey = Base64.decode(privateKey.replaceAll("-----\\w+ RSA PRIVATE KEY-----", ""));
			final byte[] result = new byte[innerKey.length + 26];
			System.arraycopy(Base64.decode("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKY="), 0, result, 0, 26);
			System.arraycopy(BigInteger.valueOf(result.length - 4).toByteArray(), 0, result, 2, 2);
			System.arraycopy(BigInteger.valueOf(innerKey.length).toByteArray(), 0, result, 24, 2);
			System.arraycopy(innerKey, 0, result, 26, innerKey.length);
			return result;
		} else {
			return Base64.decode(privateKey);
		}
	}

	/**
	 * 通过证书路径获取秘钥信息
	 * 
	 * @param pfxpath
	 * @param password
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 */
	public static KeyInfo getKeyInfoByPFXPath(String pfxpath, String password) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		FileInputStream fis = new FileInputStream(pfxpath);
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(fis, password.toCharArray());
		fis.close();
		Enumeration<String> enumas = ks.aliases();
		String keyAlias = null;
		if (enumas.hasMoreElements())// we are readin just one certificate.
		{
			keyAlias = enumas.nextElement();
		}

		KeyInfo keyInfo = new KeyInfo();

		PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, password.toCharArray());
		Certificate cert = ks.getCertificate(keyAlias);
		PublicKey pubkey = cert.getPublicKey();

		keyInfo.privateKey = prikey;
		keyInfo.publicKey = pubkey;
		return keyInfo;
	}

	/**
	 * 通过证书字符串获取秘钥信息
	 * 
	 * @param pfxStr
	 * @param password
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 */
	public static KeyInfo getKeyInfoByPFXStr(String pfxStr, String password) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		InputStream fis = new ByteArrayInputStream(Base64.decode(pfxStr));
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(fis, password.toCharArray());
		fis.close();
		Enumeration<String> enumas = ks.aliases();
		String keyAlias = null;
		if (enumas.hasMoreElements())// we are readin just one certificate.
		{
			keyAlias = enumas.nextElement();
		}

		KeyInfo keyInfo = new KeyInfo();

		PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, password.toCharArray());
		Certificate cert = ks.getCertificate(keyAlias);
		PublicKey pubkey = cert.getPublicKey();

		keyInfo.privateKey = prikey;
		keyInfo.publicKey = pubkey;
		return keyInfo;
	}

	public static class KeyInfo {

		PublicKey publicKey;
		PrivateKey privateKey;

		public PublicKey getPublicKey() {
			return publicKey;
		}

		public PrivateKey getPrivateKey() {
			return privateKey;
		}
	}

	/**
	 * 获得公钥
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		if (key == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		byte[] buffer = Base64.decode(key);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		return keyFactory.generatePublic(keySpec);
	}

	public static String getKey(String content) throws Exception {
		return content.replaceAll("\\-{5}[\\w\\s]+\\-{5}[\\r\\n|\\n]", "");
	}
}
