package com.yapp.bol.user

import com.yapp.bol.InvalidNicknameException
import com.yapp.bol.NotFoundMemberException
import com.yapp.bol.auth.AuthCommandRepository
import com.yapp.bol.auth.UserId
import com.yapp.bol.game.member.GameMemberService
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.member.MemberService
import com.yapp.bol.auth.token.TokenCommandRepository
import com.yapp.bol.validate.NicknameValidator
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userQueryRepository: UserQueryRepository,
    private val userCommandRepository: UserCommandRepository,
    private val authCommandRepository: AuthCommandRepository,
    private val tokenCommandRepository: TokenCommandRepository,
    private val memberService: MemberService,
    private val gameMemberService: GameMemberService,
) : UserService {

    override fun getUser(userId: UserId): User? {
        return userQueryRepository.getUser(userId)
    }

    override fun putUser(user: User) {
        if (NicknameValidator.validate(user.nickname ?: throw InvalidNicknameException(null)).not()) {
            throw InvalidNicknameException(user.nickname)
        }

        userCommandRepository.updateUser(user)
    }

    override fun getUserMatchCount(userId: UserId): Long {
        return userQueryRepository.getMatchCount(userId)
    }

    override fun deleteUser(userId: UserId) {
        userCommandRepository.deleteUser(userId)
        authCommandRepository.deleteUser(userId)
        tokenCommandRepository.deleteAllToken(userId)
    }

    override fun getMemberMatchCount(groupId: GroupId, userId: UserId): Long {
        val member = memberService.getMemberByGroupIdAndUserId(groupId = groupId, userId = userId)
            ?: throw NotFoundMemberException

        return gameMemberService.getMatchCountByMemberId(memberId = member.id)
    }
}
