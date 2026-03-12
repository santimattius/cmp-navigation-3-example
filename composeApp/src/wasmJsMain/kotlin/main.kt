import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

/**
 * Entry point para Web (Wasm/JS).
 *
 * La arquitectura Nav3 en commonMain funciona sin cambios en Web.
 *
 * Diferencias específicas de plataforma en Web:
 * - Back gesture: Tecla Esc en navegadores desktop.
 * - Historial del navegador: Nav3 NO sincroniza con window.history en la versión actual.
 *   Para sincronización con el historial del navegador, ver:
 *   https://github.com/nicendev/navigation3-browser (experimental, Terrakok)
 *   Se espera soporte oficial en Nav3 1.1.0.
 * - Deep links: Implementar vía window.location en wasmJsMain/jsMain.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(content = {
        App()
    })
}
