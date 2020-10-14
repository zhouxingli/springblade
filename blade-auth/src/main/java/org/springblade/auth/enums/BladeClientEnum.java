package org.springblade.auth.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 客户端枚举类
 * */

@Getter
@AllArgsConstructor
public enum BladeClientEnum {

	/**
	 * web
	 */
	WEB("web", 1),

	/**
	 * app
	 */
	APP("app", 2),
	;

	final String name;
	final int category;

}
