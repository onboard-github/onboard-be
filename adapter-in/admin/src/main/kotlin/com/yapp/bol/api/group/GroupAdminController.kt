package com.yapp.bol.api.group

import com.yapp.bol.api.group.dto.GroupListResponse
import com.yapp.bol.api.group.dto.toResponse
import com.yapp.bol.group.GroupService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
class GroupAdminController(
    private val groupService: GroupService,
) {

    @GetMapping("/admin/v1/group-list")
    fun getGroupList(): GroupListResponse {
        val list = groupService.searchGroup(null, 0, 100)
        return GroupListResponse(
            list = list.content.map { it.toResponse() },
        )
    }
}
