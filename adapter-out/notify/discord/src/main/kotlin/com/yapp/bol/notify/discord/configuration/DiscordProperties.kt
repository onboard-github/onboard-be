package com.yapp.bol.notify.discord.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "discord")
data class DiscordProperties(
    val webhook: String,
)
