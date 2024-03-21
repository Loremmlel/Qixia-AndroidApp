package org.hinanawiyuzu.qixia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.hinanawiyuzu.qixia.data.BackgroundColor.themeHorizontalGradient
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme

@Composable
fun FillPersonalInformationScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(brush = themeHorizontalGradient)
    ) {

    }
}

@Preview
@Composable
fun FillPersonalInformationScreenPreview() {
    QixiaTheme {
        FillPersonalInformationScreen()
    }
}