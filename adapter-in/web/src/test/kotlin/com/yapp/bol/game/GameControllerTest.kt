package com.yapp.bol.game

import com.yapp.bol.base.ARRAY
import com.yapp.bol.base.ENUM
import com.yapp.bol.base.NUMBER
import com.yapp.bol.base.OpenApiTag
import com.yapp.bol.base.STRING
import com.yapp.bol.base.WebControllerTest
import com.yapp.bol.group.GroupId
import io.mockk.every
import io.mockk.mockk

class GameControllerTest : WebControllerTest() {
    private val gameService: GameService = mockk()
    override val controller = GameController(gameService)

    init {
        test("게임 목록 가져오기") {
            val groupId = GroupId(1)
            val games = listOf(
                GameWithMatchCount(Game(GameId(0), "게임 1", 2, 4, GameRankType.SCORE_HIGH, "ImgUrl"), 3),
                GameWithMatchCount(Game(GameId(1), "게임 2", 2, 5, GameRankType.SCORE_HIGH, "ImgUrl"), 2),
                GameWithMatchCount(Game(GameId(2), "게임 3", 1, 4, GameRankType.SCORE_HIGH, "ImgUrl"), 0),
            )
            val sortType = GameListSortType.FIXED
            every { gameService.getGameList(groupId, sortType) } returns games

            get("/api/v1/group/{groupId}/game", arrayOf(groupId.value)) {
                queryParam("sort", sortType.name)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "game/{method-name}",
                        tag = OpenApiTag.GAME,
                        description = "게임 목록 가져오기",
                    ),
                    pathParameters(
                        "groupId" type NUMBER means "게임 목록을 조회하고자 하는 groupId, 현재는 아무값이나 넣어도 검증 없이 동일하게 내려감",
                    ),
                    queryParameters(
                        "sort" type ENUM(GameListSortType::class) means "게임 순서 정렬 기준, 기본값 MATCH_COUNT" isOptional true,
                    ),
                    responseFields(
                        "list" type ARRAY means "게임 목록",
                        "list[].id" type NUMBER means "게임 ID",
                        "list[].name" type STRING means "게임 이름",
                        "list[].minMember" type NUMBER means "게임 최소 인원수",
                        "list[].maxMember" type NUMBER means "게임 최대 인원수",
                        "list[].img" type STRING means "게임 Img Url",
                        "list[].matchCount" type NUMBER means "해당 그룹의 Match Count",
                    ),
                )
        }
    }
}
