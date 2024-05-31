package com.sparta.spartatodoproject.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ImageData")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageData extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String type;

	private Long size;

	@Lob
	@Column(name = "imagedata", length = 1000)
	private byte[] imageData;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToOne
	@JoinColumn(name = "todo_id")
	private Todo todo;

	@Builder
	public ImageData(String name, String type, Long size, byte[] imageData, Todo todo) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.imageData = imageData;
		this.todo = todo;
	}

	public void updateFile(byte[] imageData) {
		this.imageData = imageData;
	}
}