package ru.codesh.shumlevel.feature.soundanalyzer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import ru.codesh.shumlevel.R
import ru.codesh.shumlevel.core.ui.base.BaseScreen

val digitalFont = FontFamily(
    Font(R.font.seven_segment)
)

class SoundAnalyzerScreen : BaseScreen() {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val viewModel: SoundAnalyzerViewModel = koinViewModel()

        val state by viewModel.state.collectAsState()

        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> viewModel.startAnalyzing()
                    Lifecycle.Event.ON_PAUSE -> viewModel.stopAnalyzing()
                    else -> {}
                }
            }

            val lifecycle = lifecycleOwner.lifecycle
            lifecycle.addObserver(observer)

            onDispose {
                lifecycle.removeObserver(observer)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.decibel.toInt().toString(),
                fontSize = 96.sp,
                fontFamily = digitalFont,
                color = Color.Black
            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            SoundIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                steps = 12,
                minValue = 10,
                maxValue = 130,
                currentValue = state.decibel.toInt()
            )
        }
    }
}

@Preview
@Composable
private fun SoundPrev() {
    SoundIndicator(
        modifier = Modifier.fillMaxWidth(),
        steps = 4,
        minValue = 100,
        maxValue = 200,
        currentValue = 175
    )
}

@Composable
private fun SoundIndicator(
    modifier: Modifier = Modifier,
    steps: Int,
    minValue: Int,
    maxValue: Int,
    currentValue: Int
) {

    val delta = maxValue - minValue
    val stepSize = delta / steps

    Row(modifier = modifier) {
        repeat(steps) { index ->
            val stepValue = (stepSize * index + minValue).toInt()
            val color = calculateStepColor(steps, index)

            SoundIndicatorStep(
                value = stepValue,
                currentValue = currentValue,
                color = color
            )
        }
    }
}

@Composable
private fun RowScope.SoundIndicatorStep(
    value: Int,
    currentValue: Int,
    color: Color,
    padding: PaddingValues = PaddingValues(horizontal = 2.dp)
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .padding(padding)
    ) {
        val isActive = value <= currentValue

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                )
                .background(
                    color = if (isActive) color else Color.White,
                    shape = RoundedCornerShape(4.dp)
                )
        )

        Text(
            text = value.toString(),
            fontFamily = digitalFont,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

private fun calculateStepColor(totalSteps: Int, index: Int): Color {
    require(totalSteps >= 2) { "totalSteps must be at least 2" }
    require(index in 0 until totalSteps) { "index out of bounds" }

    val t = index / (totalSteps - 1).toFloat()

    return when {
        t <= 0.5f -> lerp(Color.Blue, Color.Green, t / 0.5f)
        else -> lerp(Color.Green, Color.Red, (t - 0.5f) / 0.5f)
    }
}
