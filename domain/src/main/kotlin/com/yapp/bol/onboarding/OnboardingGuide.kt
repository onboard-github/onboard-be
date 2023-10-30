package com.yapp.bol.onboarding

import com.yapp.bol.group.GroupId

data class OnboardingGuide(
    val list: List<OnboardingType>,
    val mainGroupId: GroupId?,
)
