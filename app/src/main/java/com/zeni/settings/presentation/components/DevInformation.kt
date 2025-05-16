package com.zeni.settings.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeni.R
import com.zeni.settings.domain.model.DevsInfo

@Composable
fun DevInformation(
    dev: DevsInfo,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.large)
            .clipToBounds()
            .background(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
            .clickable(enabled = dev.github != Uri.EMPTY) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = dev.github
                }
                context.startActivity(intent)
            },
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.Start
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = dev.image),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp)
                .padding(vertical = 8.dp)
                .size(size = 48.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(
            text = dev.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun DevInformationPreview() {
    DevInformation(
        dev = DevsInfo(
            name = "Berto puto",
            image = R.raw.alberto_image,
            github = "https://github.com/xBlanco"
        )
    )
}