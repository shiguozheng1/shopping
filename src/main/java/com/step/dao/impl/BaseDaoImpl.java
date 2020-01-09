package com.step.dao.impl;/*
 * Copyright 2005-2017 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */

import com.step.dao.BaseDao;
import com.step.entity.BaseEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ResolvableType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Dao - 基类
 *
 *Created by Administrator on 2016/9/10 0010.
 */
public abstract class BaseDaoImpl<T extends BaseEntity<ID>, ID extends Serializable> implements BaseDao<T, ID> {

	/**
	 * 属性分隔符
	 */
	private static final String ATTRIBUTE_SEPARATOR = ".";

	/**
	 * 别名前缀
	 */
	private static final String ALIAS_PREFIX = "shopmoreGeneratedAlias";

	/**
	 * 别名数
	 */
	private static volatile long aliasCount = 0L;

	/**
	 * 实体类类型
	 */
	private Class<T> entityClass;

    public abstract EntityManager getEntityManager();
	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		ResolvableType resolvableType = ResolvableType.forClass(getClass());
		entityClass = (Class<T>) resolvableType.as(BaseDaoImpl.class).getGeneric().resolve();
	}
	@Override
	public boolean exists(String attributeName, Object attributeValue) {
		Assert.hasText(attributeName);
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(criteriaBuilder.count(root));
		criteriaQuery.where(criteriaBuilder.equal(root.get(attributeName), attributeValue));
		TypedQuery<Long> query = getEntityManager().createQuery(criteriaQuery);
		return query.getSingleResult() > 0;
	}
	@Override
	public boolean exists(String attributeName, String attributeValue, boolean ignoreCase) {
		Assert.hasText(attributeName);

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(criteriaBuilder.count(root));
		if (ignoreCase) {
		criteriaQuery.where(criteriaBuilder.equal(criteriaBuilder.lower(root.<String>get(attributeName)), StringUtils.lowerCase(attributeValue)));
	} else {
		criteriaQuery.where(criteriaBuilder.equal(root.get(attributeName), attributeValue));
	}
	TypedQuery<Long> query = getEntityManager().createQuery(criteriaQuery);
		return query.getSingleResult() > 0;
}
	/**
	 * 合并实体对象
	 *
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	@Override
	public T merge(T entity) {
		Assert.notNull(entity);

		return getEntityManager().merge(entity);
	}


}