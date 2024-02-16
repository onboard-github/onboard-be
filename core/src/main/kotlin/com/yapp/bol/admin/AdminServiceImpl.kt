package com.yapp.bol.admin

import com.yapp.bol.auth.UserId
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl(
    private val adminQueryRepository: AdminQueryRepository,
) : AdminService {
    override fun getRoleList(userId: UserId): List<AdminRole> {
        return adminQueryRepository.getRoles(userId)
    }
}
