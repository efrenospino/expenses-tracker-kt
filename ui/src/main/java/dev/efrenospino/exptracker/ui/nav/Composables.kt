package dev.efrenospino.exptracker.ui.nav

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavGraphBuilder.expensesHomeRoute(
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(Destination.HomeDestination, content = content)
}

fun NavGraphBuilder.newExpenseRoute(
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(Destination.NewExpenseDestination, content = content)
}

fun NavGraphBuilder.viewAndEditExpenseRoute(
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        Destination.EditExpenseDestination,
        arguments = listOf(navArgument(Destination.ARG_EXPENSE_ID) { type = NavType.StringType }),
        content = {
            content(it.apply {
                savedStateHandle[Destination.ARG_EXPENSE_ID] =
                    it.arguments?.getString(Destination.ARG_EXPENSE_ID)
            })
        },
    )
}

@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.fullRoute,
        modifier = modifier,
        route = route,
        builder = builder
    )
}

fun NavGraphBuilder.composable(
    destination: Destination,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = destination.fullRoute,
        arguments = arguments,
        deepLinks = deepLinks,
        content = content
    )
}
