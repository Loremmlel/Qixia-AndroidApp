package org.hinanawiyuzu.qixia.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.CommonInputFieldWithoutLeadingIcon
import org.hinanawiyuzu.qixia.data.BackgroundColor.themeHorizontalGradient
import org.hinanawiyuzu.qixia.data.FontSize
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.theme.neutral_color
import org.hinanawiyuzu.qixia.ui.theme.secondary_color
import org.hinanawiyuzu.qixia.ui.viewmodel.FillPersonalInformationViewModel
import org.hinanawiyuzu.qixia.ui.viewmodel.Illness

@Composable
fun FillPersonalInformationScreen(
    modifier: Modifier = Modifier,
    fillPersonalInformationViewModel: FillPersonalInformationViewModel = viewModel()
) {
    if (fillPersonalInformationViewModel.screenHeight == 0)
        fillPersonalInformationViewModel.screenHeight = LocalConfiguration.current.screenHeightDp
    val circleOffsetX = (-20).dp
    val circleOffsetY = 50.dp
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = themeHorizontalGradient)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = circleOffsetX, y = circleOffsetY),
                painter = painterResource(id = R.drawable.background_circle),
                contentDescription = null
            )
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = circleOffsetX + 30.dp, y = circleOffsetY + 45.dp),
                painter = painterResource(id = R.drawable.background_circle),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStartPercent = 10, topEndPercent = 10))
                .background(Color.White)
                .padding(start = 15.dp, end = 15.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .requiredHeight((fillPersonalInformationViewModel.screenHeight * 0.8).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBackIcon(
                modifier = Modifier
                    .align(Alignment.Start)
                    .offset(y = 15.dp)
            ) {
                //TODO: 跳转到哪？
            }
            InputArea(
                modifier = Modifier,
                maleSelected = fillPersonalInformationViewModel.maleSelected,
                femaleSelected = fillPersonalInformationViewModel.femaleSelected,
                age = fillPersonalInformationViewModel.age,
                serialNumber = fillPersonalInformationViewModel.serialNumber,
                onMaleClicked = { fillPersonalInformationViewModel.onMaleSelected() },
                onFemaleClicked = { fillPersonalInformationViewModel.onFemaleSelected() },
                onAgeChanged = { fillPersonalInformationViewModel.onAgeChanged(it) },
                onSerialNumberChanged = { fillPersonalInformationViewModel.onSerialNumberChanged(it) }
            )
            MedicalHistory(
                modifier = Modifier.requiredHeight(200.dp),
                isIllnessCardClicked = fillPersonalInformationViewModel.illnessCardsClicked,
                onIllnessCardClicked = {fillPersonalInformationViewModel.onIllnessCardClicked(it)}
            )
        }
    }
}

@Composable
private fun TopBackIcon(
    modifier: Modifier = Modifier,
    onBackIconClicked: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onBackIconClicked
    ) {
        Icon(
            painter = painterResource(id = R.drawable.left_arrow),
            contentDescription = stringResource(R.string.back)
        )
    }
}

@Composable
private fun InputArea(
    modifier: Modifier = Modifier,
    maleSelected: Boolean,
    femaleSelected: Boolean,
    age: String,
    serialNumber: String,
    onMaleClicked: () -> Unit,
    onFemaleClicked: () -> Unit,
    onAgeChanged: (String) -> Unit,
    onSerialNumberChanged: (String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(start = 20.dp, top = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "个人信息",
            style = TextStyle(fontSize = 30.sp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "性别",
                style = TextStyle(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Text(text = "男")
            RadioButton(selected = maleSelected, onClick = onMaleClicked)
            Text(text = "女")
            RadioButton(selected = femaleSelected, onClick = onFemaleClicked)
        }
        CommonInputFieldWithoutLeadingIcon(
            modifier = Modifier.fillMaxWidth(),
            value = age,
            placeholderTextRes = R.string.fill_personal_information_screen_age,
            onValueChanged = { onAgeChanged(it) }
        )
        CommonInputFieldWithoutLeadingIcon(
            modifier = Modifier.fillMaxWidth(),
            value = serialNumber,
            placeholderTextRes = R.string.fill_personal_information_serial_number,
            onValueChanged = { onSerialNumberChanged(it) }
        )
    }
}

@Composable
private fun MedicalHistory(
    modifier: Modifier = Modifier,
    isIllnessCardClicked: MutableList<Boolean>,
    onIllnessCardClicked: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 40.dp, end = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "病史",
            style = TextStyle(
                fontSize = FontSize.bigSize,
                fontWeight = FontWeight.SemiBold
            )
        )
        Column(
            modifier = Modifier
                .verticalScroll(
                    state = rememberScrollState()
                )
        ) {
            val size = Illness.illnesses.size
            repeat(size / 3) {
                Row(
                    modifier = Modifier.padding(2.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IllnessCard(
                        id = it * 3,
                        isClicked = isIllnessCardClicked[it * 3],
                        onCardClicked = onIllnessCardClicked,
                        illness = Illness.illnesses[it * 3]
                    )
                    IllnessCard(
                        id = it * 3 + 1,
                        isClicked = isIllnessCardClicked[it * 3 + 1],
                        onCardClicked = onIllnessCardClicked,
                        illness = Illness.illnesses[it * 3 + 1]
                    )
                    IllnessCard(
                        id = it * 3 + 2,
                        isClicked = isIllnessCardClicked[it * 3 + 2],
                        onCardClicked = onIllnessCardClicked,
                        illness = Illness.illnesses[it * 3 + 2]
                    )
                }
            }
            if (size.toDouble() / 3 != (size / 3).toDouble()) {
                Row(
                    modifier = Modifier.padding(2.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IllnessCard(
                        id = (size - 2) * 3,
                        isClicked = isIllnessCardClicked[(size - 2) * 3],
                        onCardClicked = onIllnessCardClicked,
                        illness = Illness.illnesses[size - 2]
                    )
                    IllnessCard(
                        id = (size - 1) * 3,
                        isClicked = isIllnessCardClicked[(size - 1) * 3],
                        onCardClicked = onIllnessCardClicked,
                        illness = Illness.illnesses[size - 1]
                    )
                }
            }
        }
    }
}

@Composable
private fun IllnessCard(
    modifier: Modifier = Modifier,
    id:Int,
    isClicked: Boolean = false,
    onCardClicked: (Int) -> Unit,
    illness: String
) {
    OutlinedCard(
        modifier = modifier
            .clickable {onCardClicked(id)},
        colors = CardDefaults.cardColors(
            containerColor = if (!isClicked) Color.White else secondary_color
        ),
        border = BorderStroke(2.dp, if(!isClicked) neutral_color else Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                text = illness,
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = TextStyle(color = if(!isClicked) Color.Black else Color.White)
            )
        }
    }
}

@Preview
@Composable
private fun FillPersonalInformationScreenPreview() {
    QixiaTheme {
        FillPersonalInformationScreen()
    }
}