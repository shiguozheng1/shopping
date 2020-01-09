/*
 * Copyright 2005-2017 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.step.entity.secondary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.BaseEntity;
import com.step.entity.OrderedEntity;
import com.step.utils.wrapper.Wrapper;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.*;

/**
 * Entity - 角色
 *
 */
@Entity
@Table(name="t_role_type")
@Data

public class RoleType extends OrderedEntity<Long> {
    public interface  RoleTypeView extends Wrapper.WrapperView{}
	private static final long serialVersionUID = -6614052029623997372L;

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	protected Long id;

	/**
	 * 编码
	 */
	@JsonView(RoleTypeView.class)
	@Length(max = 200)
	@Column(nullable = false)
	private String code;

	/**
	 * 名称
	 */
	@JsonView(RoleTypeView.class)
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/**
	 *修改人
	 */
	@JsonView(RoleTypeView.class)
	@Length(max = 20)
	@Column(nullable = false)
	private String modifiedUser;


	/**
	 * 描述
	 */
	@Length(max = 200)
	private String description;

	/**
	 * 是否内置
	 */
	@JsonView(RoleTypeView.class)
	@Column(name="is_system")
	private Boolean isSystem = false;

	/**
	 * 下级角色
	 */
	@OneToMany(mappedBy = "roleType",fetch = FetchType.LAZY)
	@JsonIgnoreProperties
	private Set<Role> roles = new HashSet<>();


	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o){ return true;}

		if (o == null || getClass() != o.getClass()){ return false;
		}

		RoleType element = (RoleType) o;

		return new EqualsBuilder()
				.append(id, element.id)
				.append(name,element.name)
				.append(code, element.code)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(id)
				.append(name)
				.append(code)
				.toHashCode();
	}

	@Override
	public String toString() {
		return "RoleType{" +
				"code='" + code + '\'' +
				", name='" + name + '\'' +
				", modifiedUser='" + modifiedUser + '\'' +
				", description='" + description + '\'' +
				", isSystem=" + isSystem +
				'}';
	}
}