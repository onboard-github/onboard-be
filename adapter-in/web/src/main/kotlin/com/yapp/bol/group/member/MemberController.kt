package com.yapp.bol.group.member

import com.yapp.bol.EmptyResponse
import com.yapp.bol.auth.getSecurityUserIdOrThrow
import com.yapp.bol.game.member.GameMemberService
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.GroupService
import com.yapp.bol.group.dto.AddGuestDto
import com.yapp.bol.group.dto.JoinGroupDto
import com.yapp.bol.group.member.dto.AddGuestRequest
import com.yapp.bol.group.member.dto.GetMemberMatchCountResponse
import com.yapp.bol.group.member.dto.JoinGroupRequest
import com.yapp.bol.group.member.dto.MemberResponse
import com.yapp.bol.group.member.dto.NicknameValidationResponse
import com.yapp.bol.group.member.dto.PaginationCursorMemberRequest
import com.yapp.bol.group.member.dto.UpdateMemberInfoRequest
import com.yapp.bol.group.member.dto.toResponse
import com.yapp.bol.pagination.cursor.SimplePaginationCursorResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/group/{groupId}", "/api/v1/group/{groupId}")
class MemberController(
    private val groupService: GroupService,
    private val memberService: MemberService,
    private val gameMemberService: GameMemberService
) {
    @GetMapping("/member/validateNickname")
    fun validateMemberName(
        @PathVariable groupId: GroupId,
        @RequestParam nickname: String,
    ): NicknameValidationResponse {
        val result = memberService.validateMemberNickname(groupId, nickname)

        return result.toResponse()
    }

    @GetMapping("/member")
    fun getMemberList(
        @PathVariable groupId: GroupId,
        @RequestParam size: Int,
        @RequestParam nickname: String?,
        @RequestParam role: MemberRole?,
        @RequestParam cursor: String?,
    ): SimplePaginationCursorResponse<MemberResponse, String> {
        val result = memberService.getMembers(
            PaginationCursorMemberRequest(
                groupId = groupId,
                nickname = nickname,
                role = role,
                size = size,
                cursor = cursor,
            )
        )

        return result.mapContents { it.toResponse() }
    }

    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    fun deleteMe(
        @PathVariable groupId: GroupId,
    ): EmptyResponse {
        val userId = getSecurityUserIdOrThrow()

        memberService.deleteMyMember(groupId, userId)
        return EmptyResponse
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/host")
    fun joinHostMember(
        @PathVariable groupId: GroupId,
        @RequestBody request: JoinGroupRequest,
    ): EmptyResponse {
        val userId = getSecurityUserIdOrThrow()

        groupService.joinGroup(
            JoinGroupDto(
                groupId = groupId,
                userId = userId,
                nickname = request.nickname,
                accessCode = request.accessCode,
                guestId = if (request.guestId == null) null else MemberId(request.guestId),
            )
        )

        return EmptyResponse
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/guest")
    fun addGuestMember(
        @PathVariable groupId: GroupId,
        @RequestBody request: AddGuestRequest,
    ): EmptyResponse {
        val userId = getSecurityUserIdOrThrow()

        groupService.addGuest(
            AddGuestDto(
                groupId = groupId,
                requestUserId = userId,
                nickname = request.nickname,
            )
        )

        return EmptyResponse
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/member/{memberId}")
    fun updateMemberInfo(
        @PathVariable groupId: GroupId,
        @PathVariable memberId: MemberId,
        @RequestBody request: UpdateMemberInfoRequest,
    ): MemberResponse {
        val result = memberService.updateMemberInfo(groupId, memberId, request.nickname)

        return result.toResponse()
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/{memberId}/match/count")
    fun getMatchCount(
        @PathVariable groupId: GroupId,
        @PathVariable memberId: MemberId,
    ): GetMemberMatchCountResponse {
        val result = gameMemberService.getMatchCountByMemberId(memberId)

        return GetMemberMatchCountResponse(result)
    }
}
