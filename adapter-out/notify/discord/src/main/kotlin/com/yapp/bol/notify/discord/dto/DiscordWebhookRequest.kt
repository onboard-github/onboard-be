package com.yapp.bol.notify.discord.dto

// https://discord.com/developers/docs/resources/webhook#execute-webhook-jsonform-params
data class DiscordWebhookRequest(
    val content: String,
    val username: String? = null,
)
