package com.yapp.bol.group.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

internal interface MemberRepository : JpaRepository<MemberEntity, Long>, CustomMemberRepository {
    fun findByNickname(nickname: String): MemberEntity?

    fun findByGroupId(groupId: Long): List<MemberEntity>

    fun findByNicknameAndGroupId(nickname: String, groupId: Long): MemberEntity?

    fun findByGroupIdAndUserId(groupId: Long, userId: Long): MemberEntity?

    @Query(
        "FROM MemberEntity m " +
            "LEFT JOIN FETCH m.gameMembers gm " +
            "WHERE m.groupId = :groupId " +
            "AND m.deleted = false "
    )
    fun findWithGameMember(groupId: Long): List<MemberEntity>

    fun findByGroupIdAndRole(groupId: Long, role: MemberRole): List<MemberEntity>

    fun countByGroupId(groupId: Long): Long

    fun findAllByUserId(userId: Long): List<MemberEntity>

    fun countByGroupIdAndRoleIn(groupId: Long, roles: List<MemberRole>): Long
}
