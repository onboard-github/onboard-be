package com.yapp.bol.admin

import com.yapp.bol.auth.UserId
import org.springframework.stereotype.Component

@Component
internal class AdminClient(
    private val jpaRepository: AdminRoleRepository,
) : AdminQueryRepository, AdminCommandRepository {
    override fun getAdmin(userId: UserId): Admin {
        val entity = jpaRepository.findAllByUserId(userId.value)

        return Admin(
            userId = userId,
            allowRoles = entity.filter { it.isAllow() }.map { it.role }.toSet(),
            requestRoles = entity.filter { it.isAllow().not() }.map { it.role }.toSet(),
        )
    }

    override fun requestRole(userId: UserId, roleList: List<AdminRole>, memo: String) {
        val entityList = roleList.map {
            AdminRoleEntity.of(
                userId,
                it,
                state = AdminRoleState.REQUEST,
                memo,
                true,
            )
        }
        jpaRepository.saveAll(entityList)
    }
}
