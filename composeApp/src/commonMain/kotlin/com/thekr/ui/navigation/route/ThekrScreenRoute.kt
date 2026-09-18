package com.thekr.ui.navigation.route

import com.thekr.ui.navigation.NavigationRoute

object ThekrScreenRoute : NavigationRoute {
    override val route = "thekr_screen"
    const val CATEGORY_ID_ARG = "categoryId"
    const val ZEKR_ID_ARG = "itemId"
    const val INITIAL_PAGE_ARG = "initialPage"
    const val PAGE_COUNT_ARG = "pageCount"
    val routeWithArgs =
        "$route/{$CATEGORY_ID_ARG}/{$ZEKR_ID_ARG}/{$INITIAL_PAGE_ARG}/{$PAGE_COUNT_ARG}"
}
