package com.yapp.bol.group.member

import com.yapp.bol.auth.UserId
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.member.nickname.NicknameValidationReason
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class MemberServiceImplTest : FunSpec() {
    private val memberQueryRepository: MemberQueryRepository = mockk()
    private val memberCommandRepository: MemberCommandRepository = mockk()
    private val sut = MemberServiceImpl(memberQueryRepository, memberCommandRepository)

    init {
        context("validateMemberNickname") {
            val groupId = GroupId(0)
            val nickname = "닉네임"

            test("Success") {
                every { memberQueryRepository.findByNicknameAndGroupId(nickname, groupId) } returns null

                sut.validateMemberNickname(groupId, nickname).isAvailable shouldBe true
            }

            test("닉네임 중복") {
                val mockMember = HostMember(
                    userId = UserId(0),
                    nickname = nickname,
                )

                every { memberQueryRepository.findByNicknameAndGroupId(nickname, groupId) } returns mockMember

                sut.validateMemberNickname(groupId, nickname).isAvailable shouldBe false
                sut.validateMemberNickname(groupId, nickname).reason shouldBe NicknameValidationReason.DUPLICATED_NICKNAME
            }

            test("닉네임 길이 초과") {
                val longNickname = "x".repeat(Member.MAX_NICKNAME_LENGTH + 1)

                every { memberQueryRepository.findByNicknameAndGroupId(longNickname, groupId) } returns null

                sut.validateMemberNickname(groupId, longNickname).isAvailable shouldBe false
                sut.validateMemberNickname(groupId, longNickname).reason shouldBe NicknameValidationReason.INVALID_NICKNAME
            }

            test("닉네임 길이 부족") {
                val shortNickname = "x".repeat(Member.MIN_NICKNAME_LENGTH - 1)

                every { memberQueryRepository.findByNicknameAndGroupId(shortNickname, groupId) } returns null

                sut.validateMemberNickname(groupId, shortNickname).isAvailable shouldBe false
                sut.validateMemberNickname(groupId, shortNickname).reason shouldBe NicknameValidationReason.INVALID_NICKNAME
            }
        }
    }
}
