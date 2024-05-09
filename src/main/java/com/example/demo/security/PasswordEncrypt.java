package com.example.demo.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class PasswordEncrypt {

	public static String getEncryptedPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest algorithm;
		algorithm = MessageDigest.getInstance("SHA-1");
		byte[] digest = algorithm.digest(password.getBytes());
		return new String(Hex.encodeHex(digest));
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(getEncryptedPassword("hcl@2020"));
	}

}
