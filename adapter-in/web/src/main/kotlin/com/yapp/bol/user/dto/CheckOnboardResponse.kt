package com.yapp.bol.user.dto

import com.yapp.bol.group.GroupId
import com.yapp.bol.onboarding.OnboardingGuide
import com.yapp.bol.onboarding.OnboardingType

data class CheckOnboardResponse(
    val onboarding: List<OnboardingType>,
    val mainGroupId: GroupId?,
)

fun OnboardingGuide.toResponse(): CheckOnboardResponse =
    CheckOnboardResponse(
        onboarding = this.list,
        mainGroupId = this.mainGroupId
    )
