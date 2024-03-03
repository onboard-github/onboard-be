package com.yapp.bol.group

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

internal interface GroupRepository : JpaRepository<GroupEntity, Long> {

    @Query(
        """
        SELECT g FROM GroupEntity g
        WHERE (g.name LIKE :keyword OR g.organization LIKE :keyword)
        ORDER BY
            CASE WHEN g.name = :exactKeyword THEN 1
                 WHEN g.organization = :exactKeyword THEN 2
                 ELSE 3 END,
            g.createdDate DESC
    """,
    )
    fun findByNameOrOrganizationWithPriority(
        @Param("keyword") keyword: String,
        @Param("exactKeyword") exactKeyword: String,
        pageable: Pageable,
    ): Slice<GroupEntity>

    @Query(
        """
        SELECT Count(g) FROM GroupEntity g
        WHERE (g.name LIKE :keyword OR g.organization LIKE :keyword)
    """,
    )
    fun countByNameOrOrganization(
        @Param("keyword") keyword: String,
    ): Long

    @Query("SELECT g FROM MemberEntity m JOIN FETCH GroupEntity g ON m.groupId = g.id WHERE m.userId=:userId")
    fun findByUserId(userId: Long): List<GroupEntity>
}
