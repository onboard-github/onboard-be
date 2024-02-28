package com.yapp.bol.notify.discord

import com.yapp.bol.logging.utils.logger
import com.yapp.bol.notify.DeveloperNotifyService
import com.yapp.bol.notify.DeveloperNotifyType
import com.yapp.bol.notify.discord.configuration.DiscordProperties
import com.yapp.bol.notify.discord.dto.DiscordWebhookRequest
import org.springframework.stereotype.Component

@Component
class DiscordNotifyService(
    private val client: DiscordClient,
    private val discordProperties: DiscordProperties,
) : DeveloperNotifyService {
    private val logger = logger()

    override fun notify(developerNotifyType: DeveloperNotifyType) {
        try {
            callDiscord(developerNotifyType.toDiscordWebhookRequest())
        } catch (e: Exception) {
            logger.warn("디스코드 알림 실패", e)
        }
    }

    fun DeveloperNotifyType.toDiscordWebhookRequest(): DiscordWebhookRequest = when (this) {
        is DeveloperNotifyType.REQUEST_ADMIN_ROLE,
        is DeveloperNotifyType.CREATE_MATCH,
        -> DiscordWebhookRequest(this.message)
    }

    private fun callDiscord(request: DiscordWebhookRequest) {
        client.sendWebhook(discordProperties.webhook, request).block()
    }
}
