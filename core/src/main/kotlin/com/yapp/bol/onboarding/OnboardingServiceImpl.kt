package com.yapp.bol.onboarding

import com.yapp.bol.NotFoundUserException
import com.yapp.bol.auth.UserId
import com.yapp.bol.group.GroupQueryRepository
import com.yapp.bol.terms.TermsQueryRepository
import com.yapp.bol.terms.existUpdatedTerms
import com.yapp.bol.transaction.MyTransactional
import com.yapp.bol.user.UserQueryRepository
import org.springframework.stereotype.Service

@Service
class OnboardingServiceImpl(
    private val termsQueryRepository: TermsQueryRepository,
    private val userQueryRepository: UserQueryRepository,
    private val groupQueryRepository: GroupQueryRepository,
) : OnboardingService {

    @MyTransactional
    override fun getRemainOnboarding(userId: UserId): OnboardingGuide {
        val onboardingList = mutableListOf<OnboardingType>()

        // 약관 관련
        val termsList = termsQueryRepository.getSavedTermsByUserId(userId)
        if (termsList.isEmpty()) {
            onboardingList.add(OnboardingType.TERMS)
        } else if (termsList.existUpdatedTerms()) {
            onboardingList.add(OnboardingType.UPDATE_TERMS)
        }

        // 닉네임 입력 관련
        val user = userQueryRepository.getUser(userId) ?: throw NotFoundUserException
        if (user.nickname == null) onboardingList.add(OnboardingType.NICKNAME)

        val groupList = groupQueryRepository.getGroupsByUserId(userId)

        val mainGroupId = if (groupList.isEmpty()) {
            onboardingList.add(OnboardingType.JOIN_GROUP)
            null
        } else {
            groupList.first().id
        }

        return OnboardingGuide(onboardingList, mainGroupId)
    }
}
