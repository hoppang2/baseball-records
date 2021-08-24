package com.baseball.records.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.baseball.records.common.Prop;

public class EncUtil {

	private static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00 };

	static final Logger logger = LogManager.getLogger(EncUtil.class);

	/**
	 * 암호화
	 *
	 * @param str
	 * @return
	 */
	public static String AesEncode(String str) {

		return AesEncode(str, null);
	}

	/**
	 * 암호화
	 *
	 * @param str
	 * @param ifKey
	 * @return
	 */
	public static String AesEncode(String str, String ifKey) {
		if (StringUtils.isBlank(str)) {
			return "";
		}

		String key = "";
		if (StringUtils.isBlank(ifKey)) {
			key = Prop.conf.getString("enc.key");
		} else {
			key = ifKey;
		}

		try {
			byte[] textBytes = str.getBytes("UTF-8");
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);

			SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
			byte[] encrypt = cipher.doFinal(textBytes);

			return new String(Base64.encodeBase64(encrypt));
		} catch (Exception e) {
			logger.error("enc error", e);
			return "";
		}
	}

	/**
	 * 복호화
	 *
	 * @param str
	 * @return
	 */
	public static String AesDecode(String str) {
		return AesDecode(str, null);
	}

	public static String AesDecode(String str, String ifKey) {

		if (StringUtils.isBlank(str)) {
			return "";
		}

		String key = "";
		if (StringUtils.isBlank(ifKey)) {
			key = Prop.conf.getString("enc.key");
		} else {
			key = ifKey;
		}

		byte[] textBytes = Base64.decodeBase64(str);
		try {
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
			return new String(cipher.doFinal(textBytes), "UTF-8");
		} catch (Exception e) {
			logger.error("dec error", e);
			return "";
		}
	}

	public static byte[] AesDecodeByte(byte[] byt, String ifKey)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String key = "";
		if (StringUtils.isBlank(ifKey)) {
			key = Prop.conf.getString("enc.key");
		} else {
			key = ifKey;
		}

		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		return cipher.doFinal(byt);
	}

}
