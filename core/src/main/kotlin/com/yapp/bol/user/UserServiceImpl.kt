package com.yapp.bol.user

import com.yapp.bol.InvalidNicknameException
import com.yapp.bol.auth.AuthCommandRepository
import com.yapp.bol.auth.UserId
import com.yapp.bol.auth.token.TokenCommandRepository
import com.yapp.bol.transaction.MyTransactional
import com.yapp.bol.validate.NicknameValidator
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userQueryRepository: UserQueryRepository,
    private val userCommandRepository: UserCommandRepository,
    private val authCommandRepository: AuthCommandRepository,
    private val tokenCommandRepository: TokenCommandRepository,
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

    @MyTransactional
    override fun deleteUser(userId: UserId) {
        userCommandRepository.deleteUser(userId)
        authCommandRepository.deleteUser(userId)
        tokenCommandRepository.deleteAllToken(userId)
    }
}
