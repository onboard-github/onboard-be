package com.yapp.bol.user

import com.yapp.bol.NotFoundUserException
import com.yapp.bol.auth.UserId
import com.yapp.bol.game.member.GameMemberQueryRepository
import com.yapp.bol.group.member.MemberQueryRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
internal class UserClient(
    private val userRepository: UserRepository,
    private val memberQueryRepository: MemberQueryRepository,
    private val gameMemberQueryRepository: GameMemberQueryRepository
) : UserQueryRepository, UserCommandRepository {
    override fun getUser(userId: UserId): User? {
        val userEntity = userRepository.findByIdOrNull(userId.value) ?: throw NotFoundUserException

        return userEntity.toDomain()
    }

    @Transactional
    override fun updateUser(user: User) {
        val entity = userRepository.findByIdOrNull(user.id.value) ?: throw NotFoundUserException

        entity.name = user.nickname

        userRepository.save(entity)
    }

    override fun getMatchCount(userId: UserId): Long {
        return memberQueryRepository.findMembersIdsByUserId(userId = userId).sumOf {
            gameMemberQueryRepository.getMatchCount(memberId = it)
        }
    }

    override fun deleteUser(userId: UserId) {
        userRepository.deleteById(userId.value)
    }
}

private fun UserEntity.toDomain(): User = User(
    id = UserId(this.id),
    nickname = this.name
)
