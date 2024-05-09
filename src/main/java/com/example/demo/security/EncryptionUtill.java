package com.example.demo.security;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.SerializationUtils;

import com.example.demo.dto.DecryptDataDto;
import com.example.demo.dto.EncryptDataDto;

public class EncryptionUtill {

	private static String algo = "AES";

	public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algo);
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey;
	}

	public static String secretKeyToString(SecretKey secretKey) {
		byte[] keyBytes = secretKey.getEncoded();
		return Base64.getEncoder().encodeToString(keyBytes);
	}

	public static SecretKey stringToSecretKey(String secretKeyString) {
		byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
		SecretKey secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
		return secretKey;
	}

	public static String encrypt(Object data) {
		try {
			byte[] byteData = SerializationUtils.serialize((Serializable) data);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey());
			byte[] encryptedData = cipher.doFinal(byteData);
			return Base64.getEncoder().encodeToString(encryptedData);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encrypt(String plainText) throws Exception {
		String key = secretKeyToString(generateSecretKey());
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algo);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static String hashString(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = md.digest(input.getBytes());
			return new String(Hex.encodeHex(hashedBytes)).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static EncryptDataDto decrypt(DecryptDataDto dataDto) {
		SecretKey key = stringToSecretKey(dataDto.getSecretKey());
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decryptedData = cipher.doFinal(dataDto.getEncryptedData().getBytes());
            String decryptedString = new String(decryptedData);
            return new EncryptDataDto(decryptedString, decryptedString);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
