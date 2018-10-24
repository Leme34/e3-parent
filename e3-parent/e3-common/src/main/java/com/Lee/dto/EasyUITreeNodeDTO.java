package com.Lee.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 分类前端树型对象结点
 */
@Data
public class EasyUITreeNodeDTO implements Serializable {

	private long id;
	private String text;
	private String state;

}
