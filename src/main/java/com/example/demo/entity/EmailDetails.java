package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "smtp_email")
public class EmailDetails {

	@Id
	private int id;
	private String recipient;
	private String msgBody;
	private String subject;
	private String attachment;
}
