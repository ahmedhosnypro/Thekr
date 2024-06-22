import androidx.compose.ui.window.ComposeUIViewController
import com.thekr.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
