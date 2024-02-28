package com.yapp.bol.admin

import com.yapp.bol.auth.UserId
import com.yapp.bol.notify.DeveloperNotifyService
import com.yapp.bol.notify.DeveloperNotifyType
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl(
    private val adminQueryRepository: AdminQueryRepository,
    private val adminCommandRepository: AdminCommandRepository,
    private val developerNotifyService: DeveloperNotifyService,
) : AdminService {
    override fun getAvailableRoleList(userId: UserId): Set<AdminRole> {
        return adminQueryRepository.getAdmin(userId).allowRoles
    }

    override fun requestRole(userId: UserId, role: List<AdminRole>, memo: String) {
        val admin = adminQueryRepository.getAdmin(userId)

        val requestRoles = role.filter { admin.allowRoles.contains(it).not() }

        if (requestRoles.isEmpty()) return

        adminCommandRepository.requestRole(userId, requestRoles, memo)
        developerNotifyService.notify(DeveloperNotifyType.REQUEST_ADMIN_ROLE(requestRoles))
    }
}
