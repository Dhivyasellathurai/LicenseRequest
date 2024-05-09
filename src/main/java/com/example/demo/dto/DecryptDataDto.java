package com.example.demo.dto;

import lombok.Data;

@Data
public class DecryptDataDto {

	private String encryptedData;

	private String secretKey; 

}
