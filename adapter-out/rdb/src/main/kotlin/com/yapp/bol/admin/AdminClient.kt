package com.yapp.bol.admin

import com.yapp.bol.auth.UserId
import org.springframework.stereotype.Component

@Component
internal class AdminClient(
    private val jpaRepository: AdminRoleRepository,
) : AdminQueryRepository {
    override fun getRoles(userId: UserId): List<AdminRole> {
        return jpaRepository.findAllByUserIdAndState(userId.value, AdminRoleState.ALLOW).map { it.role }
    }
}
