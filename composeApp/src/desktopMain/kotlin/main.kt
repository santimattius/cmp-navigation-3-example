import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

/**
 * Entry point para Desktop (JVM).
 *
 * La arquitectura Nav3 en commonMain funciona sin cambios en Desktop.
 *
 * Diferencias específicas de plataforma en Desktop:
 * - Back gesture: Tecla Esc (manejada por Nav3 automáticamente).
 * - Deep links: No hay equivalente nativo; implementar vía argumentos de línea de comandos.
 * - Historial del navegador: No aplica.
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CMP Navigation 3 Example",
    ) {
        App()
    }
}
