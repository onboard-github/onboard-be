package com.yapp.bol.base

import com.yapp.bol.ExceptionHandler

abstract class WebControllerTest : BaseControllerTest(
    controllerAdvices = listOf(ExceptionHandler()),
)
