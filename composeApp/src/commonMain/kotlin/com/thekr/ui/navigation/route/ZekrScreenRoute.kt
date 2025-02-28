package com.thekr.ui.navigation.route

import com.thekr.ui.navigation.NavigationRoute
import kotlinx.serialization.Serializable

object ThekrScreenRoute : NavigationRoute {
    override val route = "thekr_screen"
    override val titleRes = "Counter Details"
    const val CATEGORY_ID_ARG = "categoryId"
    const val ZEKR_ID_ARG = "itemId"
    const val INITIAL_PAGE_ARG = "initialPage"
    const val PAGE_COUNT_ARG = "pageCount"
    val routeWithArgs =
        "$route/{$CATEGORY_ID_ARG}/{$ZEKR_ID_ARG}/{$INITIAL_PAGE_ARG}/{$PAGE_COUNT_ARG}"
}

//@Serializable
//data class ThekrScreenRoute(
//    val categoryId: Long = 1,
//    val thekrId: Long = 1,
//    val initialPage: Int = 0,
//    val pageCount: Int = 1,
//) : NavigationRoute(
//    titleRes = "Counter Details",
//    allowSameDestination = true,
//)