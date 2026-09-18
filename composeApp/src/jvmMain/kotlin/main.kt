import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.thekr.App
import com.thekr.JvmApplication
import com.thekr.resources.Res
import com.thekr.resources.app_name
import com.thekr.resources.icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    JvmApplication // static initialization for dataStores
    Window(
        title = stringResource(Res.string.app_name),
        icon = painterResource(Res.drawable.icon),
        state = rememberWindowState(
            width = 1920.dp,
            height = 1080.dp,
            position = WindowPosition.Aligned(Alignment.Center),
        ),
        onCloseRequest = ::exitApplication,
    ) {
        App()
    }
}
