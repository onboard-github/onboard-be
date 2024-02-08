package com.yapp.bol.notify.discord

import com.yapp.bol.notify.discord.dto.DiscordWebhookRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.PostExchange
import reactor.core.publisher.Mono

interface DiscordClient {
    @PostExchange("/api/webhooks{path}")
    fun sendWebhook(
        @PathVariable path: String,
        @RequestBody request: DiscordWebhookRequest,
    ): Mono<Void>
}
