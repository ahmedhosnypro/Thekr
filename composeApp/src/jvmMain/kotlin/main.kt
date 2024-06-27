import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import com.thekr.App
import com.thekr.JvmApplication

fun main() = application {
    JvmApplication // static initialization for dataStores
    Window(
        title = "Desktop",
        state = rememberWindowState(
            width = 900.dp,
            height = 900.dp,
            position = WindowPosition.Aligned(Alignment.Center)
        ),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(900, 900)
        App()
    }
}