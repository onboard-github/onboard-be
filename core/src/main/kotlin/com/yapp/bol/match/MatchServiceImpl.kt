package com.yapp.bol.match

import com.yapp.bol.InvalidMatchMemberException
import com.yapp.bol.NotFoundGroupException
import com.yapp.bol.game.GameService
import com.yapp.bol.game.member.GameMemberService
import com.yapp.bol.game.rating.dto.MatchInput
import com.yapp.bol.game.rating.dto.MatchInputItem
import com.yapp.bol.game.rating.strategy.BloRatingStrategy
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
    private val gameService: GameService,
    private val developerNotifyService: DeveloperNotifyService,
) : MatchService {

    // TODO: @Transactional
    override fun createMatch(createMatchDto: CreateMatchDto): Match {
        validateMatch(createMatchDto)

        val group = groupQueryRepository.findById(createMatchDto.groupId) ?: throw NotFoundGroupException
        val matchInput = createMatchDto.toMatchInput()
        val resultGameMember = BloRatingStrategy.compute(matchInput)

        val season = seasonService.getOrCreateSeason(groupId = createMatchDto.groupId)

        val match = createMatchDto.toDomain(season = season)

        val savedMatch = matchCommandRepository.createMatch(match = match, gameMembers = resultGameMember)

        developerNotifyService.notify(DeveloperNotifyType.CREATE_MATCH(savedMatch, group))

        return savedMatch
    }

    private fun validateMatch(createMatchDto: CreateMatchDto) {
        val gameId = createMatchDto.gameId
        val memberCount = createMatchDto.createMatchMemberDtos.size

        if (!gameService.validateMemberSize(gameId = gameId, memberCount = memberCount)) {
            throw InvalidMatchMemberException
        }

        // TODO: 검증 로직 추가, member ID 랑 groupId
        // TODO: game id 검증
    }

    private fun CreateMatchDto.toMatchInput(): MatchInput {
        val items = this.createMatchMemberDtos

        return MatchInput(
            items.map {
                val gameMember = gameMemberService.getOrCreateGameMember(
                    this.groupId,
                    this.gameId,
                    it.memberId,
                )

                MatchInputItem(
                    gameMember = gameMember,
                    rank = it.ranking,
                    gameScore = it.score,
                )
            }.toSet()
        )
    }
}
