package com.thekr.ui.settings.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thekr.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GroupTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(title, style = MaterialTheme.typography.titleLarge, modifier = modifier)
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview
@Composable
fun CategoryTitlePreview() {
    AppTheme {
        Surface {
            GroupTitle(title = "Category Title")
        }
    }
}