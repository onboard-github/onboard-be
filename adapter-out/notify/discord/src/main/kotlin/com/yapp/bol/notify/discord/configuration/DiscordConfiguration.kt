package com.yapp.bol.notify.discord.configuration

import com.yapp.bol.notify.discord.DiscordClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@EnableConfigurationProperties(DiscordProperties::class)
@Configuration
class DiscordConfiguration {

    @Bean
    internal fun discordClient(): DiscordClient {
        val webClient = WebClient.builder().baseUrl(DISCORD_URL).build()
        val factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build()

        return factory.createClient(DiscordClient::class.java)
    }

    companion object {
        private const val DISCORD_URL = "https://discord.com"
    }
}
