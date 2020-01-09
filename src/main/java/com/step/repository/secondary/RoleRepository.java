package com.step.repository.secondary;

import com.step.entity.secondary.Role;
import com.step.entity.secondary.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * shigz
 * 2019/10/25
 **/
@Repository
@Transactional("transactionManagerSecondary")
public interface RoleRepository extends JpaRepository<Role,Long> , JpaSpecificationExecutor<Role> {
    @Query(nativeQuery = true,value = "select m.* from t_role m where m.id in (select roles_id from t_admin_t_role t where t.admins_id = ?1)")
    List<Role> findAllByAdminId(Long id);

    @Query(nativeQuery = true,value = "with subqry(id,code,name,is_system,parent_id,description,createdDate,modifiedDate,roleType_id,tree_path,grade) as (\n" +
            "select id,code,name,is_system,parent_id,description,createdDate,modifiedDate,roleType_id,tree_path,grade from t_role  where roleType_id = ?1\n" +
            "union all \n" +
            "select t_role.id,t_role.code,t_role.name,t_role.is_system,t_role.parent_id,t_role.description,t_role.createdDate,t_role.modifiedDate,t_role.roleType_id,t_role.tree_path,t_role.grade from t_role,subqry where t_role.parent_id = subqry.id\n" +
            ")select * from subqry ")
    List<Role> findAllByRoleTypeId(Long id);

    Role findByCode(String code);

}
