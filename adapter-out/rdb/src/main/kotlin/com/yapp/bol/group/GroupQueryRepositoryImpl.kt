package com.yapp.bol.group

import com.yapp.bol.auth.UserId
import com.yapp.bol.game.GameId
import com.yapp.bol.group.member.MemberEntity
import com.yapp.bol.group.member.MemberRepository
import com.yapp.bol.group.member.toDomain
import com.yapp.bol.pagination.offset.PaginationOffsetResponse
import java.time.LocalDateTime
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
internal class GroupQueryRepositoryImpl(
    private val groupRepository: GroupRepository,
    private val memberRepository: MemberRepository,
) : GroupQueryRepository {
    override fun findById(id: GroupId): Group? {
        return groupRepository.findByIdOrNull(id.value)?.toDomain()
    }

    override fun search(keyword: String?, pageNumber: Int, pageSize: Int): PaginationOffsetResponse<Group> {
        val pageable = PageRequest.of(
            pageNumber,
            pageSize,
            Sort.by("createdDate").descending(),
        )

        if (keyword.isNullOrEmpty()) {
            val groups: Page<GroupEntity> = groupRepository.findAll(pageable)

            return toCursor(groups, groups.totalElements)
        }

        val groups: Slice<GroupEntity> = groupRepository.findByNameOrOrganizationWithPriority(
            "%$keyword%",
            keyword,
            pageable,
        )

        val totalCount = groupRepository.countByNameOrOrganization("%$keyword%")

        return toCursor(groups, totalCount)
    }

    private fun toCursor(slice: Slice<GroupEntity>, totalCount: Long): PaginationOffsetResponse<Group> {
        val content: List<Group> = slice.content.map(GroupEntity::toDomain)

        return PaginationOffsetResponse(content, totalCount, slice.hasNext())
    }

    override fun getLeaderBoardList(groupId: GroupId, gameId: GameId): List<LeaderBoardMember> {
        // TODO: 이쁜 쿼리로
        return memberRepository.findWithGameMember(groupId.value)
            .map {
                it.toLeaderBoardDomain(gameId)
            }
            .sortedByDescending {
                it.score
            }
    }

    private fun MemberEntity.toLeaderBoardDomain(gameId: GameId): LeaderBoardMember {
        val gameMember = this.gameMembers.firstOrNull { it.gameId == gameId.value }
        val recentStandardTime = LocalDateTime.now().minusHours(1)

        return LeaderBoardMember(
            member = this.toDomain(),
            score = gameMember?.finalScore,
            matchCount = gameMember?.matchCount,
            isChangeRecent = gameMember?.updatedDate?.isAfter(recentStandardTime) ?: false,
        )
    }

    override fun getGroupsByUserId(userId: UserId): List<Group> =
        groupRepository.findByUserId(userId.value).map { it.toDomain() }
}
