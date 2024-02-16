package com.yapp.bol.admin

import org.springframework.data.jpa.repository.JpaRepository

internal interface AdminRoleRepository : JpaRepository<AdminRoleEntity, Long> {
    fun findAllByUserIdAndState(userId: Long, state: AdminRoleState): List<AdminRoleEntity>
}
