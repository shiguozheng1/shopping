package com.step.dao;




import com.step.entity.BaseEntity;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.List;

/**
 * Dao - 基类
 *
 *Created by Administrator on 2016/9/10 0010.
 */
public interface BaseDao<T extends BaseEntity<ID>, ID extends Serializable> {

	/**
	 * 判断是否存在
	 *
	 * @param attributeName
	 *            属性名称
	 * @param attributeValue
	 *            属性值
	 * @return 是否存在
	 */
	boolean exists(String attributeName, Object attributeValue);

	/**
	 * 判断是否存在
	 *
	 * @param attributeName
	 *            属性名称
	 * @param attributeValue
	 *            属性值
	 * @param ignoreCase
	 *            忽略大小写
	 * @return 是否存在
	 */
	boolean exists(String attributeName, String attributeValue, boolean ignoreCase);


	public T merge(T entity) ;

}