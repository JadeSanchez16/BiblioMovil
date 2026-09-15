package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        MaterialTheme {
            BiblioMobilPlaceholder()
        }
    }
}

@Composable
private fun BiblioMobilPlaceholder() {
    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
    ) {
        Text(
            text = "BiblioMobil",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
