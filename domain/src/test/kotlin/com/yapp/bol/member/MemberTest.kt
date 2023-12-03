package com.yapp.bol.member

import com.yapp.bol.InvalidNicknameException
import com.yapp.bol.auth.UserId
import com.yapp.bol.group.member.GuestMember
import com.yapp.bol.group.member.HostMember
import com.yapp.bol.group.member.Member
import com.yapp.bol.group.member.OwnerMember
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

val MEMBER_OWNER = OwnerMember(userId = UserId(0), nickname = "nick")

class MemberTest : FunSpec() {
    init {
        test("멤버 생성") {
            MEMBER_OWNER.shouldBeInstanceOf<Member>()
        }

        test("멤버 닉네임 최대 길이 제한") {
            val nickname = "x".repeat(11)

            shouldThrow<InvalidNicknameException> {
                OwnerMember(userId = UserId(0), nickname = nickname)
            }
            shouldThrow<InvalidNicknameException> {
                HostMember(userId = UserId(0), nickname = nickname)
            }
            shouldThrow<InvalidNicknameException> {
                GuestMember(nickname = nickname)
            }
        }

        test("멤버 닉네임 최소 길이 제한") {
            val nickname = "x".repeat(0)

            shouldThrow<InvalidNicknameException> {
                OwnerMember(userId = UserId(0), nickname = nickname)
            }
            shouldThrow<InvalidNicknameException> {
                HostMember(userId = UserId(0), nickname = nickname)
            }
            shouldThrow<InvalidNicknameException> {
                GuestMember(nickname = nickname)
            }
        }

        test("멤버 닉네임 변경 성공") {
            val owner = OwnerMember(userId = UserId(0), nickname = "holden")
            val host = HostMember(userId = UserId(0), nickname = "holden")
            val guest = GuestMember(nickname = "holden")

            val newMembers = listOf(owner, host, guest).map {
                it.changeNickname("holden2")
            }

            newMembers.forEach {
                it.nickname shouldBe "holden2"
            }
        }
    }
}
