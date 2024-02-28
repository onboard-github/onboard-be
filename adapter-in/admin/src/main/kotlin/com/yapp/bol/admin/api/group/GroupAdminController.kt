package com.yapp.bol.admin.api.group

import com.yapp.bol.admin.api.group.dto.GroupListResponse
import com.yapp.bol.admin.api.group.dto.toResponse
import com.yapp.bol.group.GroupService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupAdminController(
    private val groupService: GroupService,
) {

    @GetMapping("/admin/v1/group-list")
    fun getGroupList(
        @RequestParam keyword: String?,
        @RequestParam(defaultValue = "0") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
    ): GroupListResponse {
        val list = groupService.searchGroup(keyword, pageNumber, pageSize)
        return GroupListResponse(
            list = list.content.map { it.toResponse() },
            totalCount = list.totalCount,
        )
    }
}
