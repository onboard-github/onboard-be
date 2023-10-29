package com.yapp.bol.group

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

internal interface GroupRepository : JpaRepository<GroupEntity, Long> {
    @Query("""
        SELECT g FROM GroupEntity g
        WHERE (g.name LIKE :name OR g.organization LIKE :organization)
        ORDER BY 
            CASE WHEN g.name = :exactName THEN 1 
                 WHEN g.organization = :exactOrganization THEN 2
                 ELSE 3 END,
            g.createdDate DESC
    """)
    fun findByNameOrOrganizationWithPriority(
        @Param("name") name: String,
        @Param("organization") organization: String,
        @Param("exactName") exactName: String,
        @Param("exactOrganization") exactOrganization: String,
        pageable: Pageable
    ): Slice<GroupEntity>

    @Query("SELECT g FROM MemberEntity m JOIN FETCH GroupEntity g ON m.groupId = g.id WHERE m.userId=:userId")
    fun findByUserId(userId: Long): List<GroupEntity>
}
