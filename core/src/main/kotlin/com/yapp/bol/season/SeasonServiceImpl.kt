package com.yapp.bol.season

import com.yapp.bol.group.GroupId
import com.yapp.bol.transaction.MyTransactional
import org.springframework.stereotype.Service

@Service
class SeasonServiceImpl(
    private val seasonQueryRepository: SeasonQueryRepository,
    private val seasonCommandRepository: SeasonCommandRepository,
) : SeasonService {

    @MyTransactional
    override fun getOrCreateSeason(groupId: GroupId): Season {
        return seasonQueryRepository.getSeason(groupId) ?: seasonCommandRepository.createSeason(Season(groupId = groupId))
    }
}
