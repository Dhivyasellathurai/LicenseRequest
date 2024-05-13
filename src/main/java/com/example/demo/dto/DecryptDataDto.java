package com.example.demo.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DecryptDataDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String encryptedData;

	private String secretKey;

}
