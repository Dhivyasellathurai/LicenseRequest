package com.example.demo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncryptDataDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String licenseKey;
	private String email;

}
