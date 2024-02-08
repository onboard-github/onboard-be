package com.yapp.bol.match

import com.yapp.bol.NotFoundGroupException
import com.yapp.bol.game.member.GameMemberService
import com.yapp.bol.group.GroupQueryRepository
import com.yapp.bol.match.dto.CreateMatchDto
import com.yapp.bol.match.dto.toDomain
import com.yapp.bol.notify.DeveloperNotifyService
import com.yapp.bol.notify.DeveloperNotifyType
import com.yapp.bol.season.SeasonService
import org.springframework.stereotype.Service

@Service
class MatchServiceImpl(
    private val matchCommandRepository: MatchCommandRepository,
    private val groupQueryRepository: GroupQueryRepository,
    private val gameMemberService: GameMemberService,
    private val seasonService: SeasonService,
    private val developerNotifyService: DeveloperNotifyService,
) : MatchService {
    // TODO: @Transactional
    override fun createMatch(createMatchDto: CreateMatchDto): Match {
        // TODO: 검증 로직 추가, member ID 랑 groupId
        // TODO: game id 검증
        val group = groupQueryRepository.findById(createMatchDto.groupId) ?: throw NotFoundGroupException

        val gameMembers = gameMemberService.processScore(createMatchDto = createMatchDto)

        val season = seasonService.getOrCreateSeason(groupId = createMatchDto.groupId)

        val match = createMatchDto.toDomain(season = season)

        val savedMatch = matchCommandRepository.createMatch(match = match, gameMembers = gameMembers)

        developerNotifyService.notify(DeveloperNotifyType.CREATE_MATCH(savedMatch, group))

        return savedMatch
    }
}
