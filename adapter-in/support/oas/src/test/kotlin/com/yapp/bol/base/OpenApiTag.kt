package com.yapp.bol.base

enum class OpenApiTag(
    val value: String,
) {
    TEST("test"),
    AUTH("Auth"),
    TERMS("Terms"),
    SETTING("Setting"),
    FILE("File"),
    GAME("Game"),
    GROUP("Group"),
    MEMBER("Member"),
    USER("User"),
    MATCH("Match"),
    ADMIN_BASE("Admin 기본 API"),
    ADMIN_GROUP("Admin Group"),
}
