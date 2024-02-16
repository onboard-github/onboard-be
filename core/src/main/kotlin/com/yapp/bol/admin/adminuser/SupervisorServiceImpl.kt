package com.yapp.bol.admin.adminuser

import com.yapp.bol.admin.supervisor.AdminRole
import com.yapp.bol.admin.supervisor.SupervisorService
import com.yapp.bol.auth.UserId
import org.springframework.stereotype.Service

@Service
class SupervisorServiceImpl : SupervisorService {
    override fun getRoleList(userId: UserId): List<AdminRole> {
        return listOf(AdminRole.GROUP_LIST_ALL)
    }
}
