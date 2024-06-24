import com.thekr.ui.unit.udp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import com.thekr.App
import com.thekr.JvmApplication

fun main() = application {
    JvmApplication
    Window(
        title = "Thekr",
        state = rememberWindowState(width = 800.udp, height = 600.udp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        App()
    }
}