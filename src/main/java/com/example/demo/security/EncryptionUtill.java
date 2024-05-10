package com.example.demo.security;

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
import com.example.demo.service.EncryptionException;

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

	public static SecretKey stringToSecretKey(String secretKeyString) {
		byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
		SecretKey secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
		return secretKey;
	}

	public static Object encrypt(Object data) throws Exception {
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

	public static Object decrypt(Object encryptedDataDto) throws Exception {
		try {
			if (!(encryptedDataDto instanceof DecryptDataDto)) {
				throw new IllegalArgumentException("Invalid encrypted data format");
			}
			DecryptDataDto dataDto = (DecryptDataDto) encryptedDataDto;
			SecretKey key = stringToSecretKey(dataDto.getSecretKey());
			String encryptedData1 = dataDto.getEncryptedData();
			byte[] encryptedData = Base64.getDecoder().decode(encryptedData1);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decryptedData = cipher.doFinal(encryptedData);
			Object data = SerializationUtils.deserialize(decryptedData);
			return data;
		} catch (IllegalArgumentException ex) {

			throw new InvalidDataException("Error decrypting data", ex);
		} catch (Exception ex) {
			throw new EncryptionException("Error decrypting data", ex);
		}
	}

}