package org.springblade.auth.entity;

import lombok.Data;

@Data
public class Result<T> {
	private boolean isSuccess;

	private T data;

}
