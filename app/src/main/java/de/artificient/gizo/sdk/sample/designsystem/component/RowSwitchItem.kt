package de.artificient.gizo.sdk.sample.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.artificient.gizo.sdk.sample.designsystem.theme.urbanistFontFamily

@Composable
fun RowSwitchItem(
    text: String,
    switchOn: Boolean,
    onSwitchChange: (Boolean) -> Unit,
) {

    Row(
        modifier = Modifier,
    ) {
        Card(
            modifier = Modifier
                .height(50.dp)
                .clickable { },
            shape = RectangleShape,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = switchOn,
                    onCheckedChange = { switchOn_ ->
                        onSwitchChange(switchOn_)
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = text,
                    modifier = Modifier,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = urbanistFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
fun RowSwitchItemPreview(modifier: Modifier = Modifier) {
    RowSwitchItem(text = "Driving assistance",
        switchOn = true,
        onSwitchChange = { _ -> }
    )
}
