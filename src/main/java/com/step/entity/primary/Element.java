package com.step.entity.primary;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/***
 * ldap资源
 */
@Data
public class Element {

    public enum ElementType{
        organization("公司"),
        organizationalUnit("部门"),
        organizationalPerson("人员");
        private String text;
        ElementType(String text) {
            this.text = text;
        }
    }
    private String method;//update 或者 create
    private Long id;
    private String name;
    private String modifyName; //修改name
    private Long parentId; //上一级id
    private String parentDn; //父级dn
    private HrmSubCompany hrmSubCompany;
    private HrmDepartment hrmDepartment;
    private String dn;
    @Override
    public boolean equals(Object o) {
        if (this == o){ return true;}

        if (o == null || getClass() != o.getClass()){ return false;
        }

        Element element = (Element) o;

        return new EqualsBuilder()
                .append(id, element.id)
                .append(name, element.name)
                .append(parentId,element.parentId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(parentId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Element{" +
                "method='" + method + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", parentId=" + parentId +
                ", parentDn='" + parentDn + '\'' +
                ", dn='" + dn + '\'' +
                '}';
    }
}
