import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.thekr.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("Thekr") {
        App()
    }
}
