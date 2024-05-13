package com.example.demo.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.SerializationUtils;

import com.example.demo.dto.DecryptDataDto;
import com.example.demo.dto.EncryptDataDto;

public class EncryptionUtill {

	private static final String SECRET_KEY = "Ebrain2024";
	private static final String SALT = "license";
	private static final int KEY_LENGTH = 256;
	private static final int ITERATION_COUNT = 10000;
	private static String algo = "AES";

	public static SecretKey generateSecretKey() throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), ITERATION_COUNT, KEY_LENGTH);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), algo);
	}

	public static String secretKeyToString(SecretKey secretKey) {
		byte[] keyBytes = secretKey.getEncoded();
		return Base64.getEncoder().encodeToString(keyBytes);
	}

	public static DecryptDataDto encrypt(Object data) throws Exception {
		SecretKey key = generateSecretKey();
		try {
			byte[] byteData = SerializationUtils.serialize((Serializable) data);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptedData = cipher.doFinal(byteData);
			String encryptedData1 = Base64.getEncoder().encodeToString(encryptedData);
			DecryptDataDto dataDto = DecryptDataDto.builder().encryptedData(encryptedData1)
					.secretKey(secretKeyToString(key)).build();
			return dataDto;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
	

	public static EncryptDataDto decrypt(DecryptDataDto encryptedData) throws Exception {
		SecretKey key = stringToSecretKey(encryptedData.getSecretKey());
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decodedData = Base64.getDecoder().decode(encryptedData.getEncryptedData());
			byte[] decryptedData = cipher.doFinal(decodedData);
			EncryptDataDto dataDto = deserialize(decryptedData, EncryptDataDto.class);
			return dataDto;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static <T> T deserialize(byte[] bytes, Class<T> clazz) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
			Object obj = in.readObject();
			return clazz.cast(obj);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static SecretKey stringToSecretKey(String secretKeyString) {
		byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
		SecretKey secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
		return secretKey;
	}

}