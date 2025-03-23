package ru.codesh.shumlevel.feature.aboutapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.codesh.shumlevel.core.ui.base.BaseScreen
import ru.codesh.shumlevel.feature.soundanalyzer.SoundAnalyzerScreen

class AboutAppScreen : BaseScreen() {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionCard(title = "Общее описание") {
                    Text(
                        text = "Данное приложение разработано студентом 3 курса, группы 3-1 направления «Прикладная информатика» ДГУНХ. Работа выполнена в учебно-исследовательских целях в рамках проектного задания.",
                        fontSize = 16.sp
                    )
                }

                SectionCard(title = "Автор") {
                    Text(
                        text = "Кунниев Шамиль\nСтудент ДГУНХ, направление «Прикладная информатика» группа 3-1",
                        fontSize = 16.sp
                    )
                }

                SectionCard(title = "Научный руководитель") {
                    Text(
                        text = "Кулибеков Нурулла Асадуллаевич\nДоцент кафедры информационных технологий",
                        fontSize = 16.sp
                    )
                }

                SectionCard(title = "Цель проекта") {
                    Text(
                        text = "Создание мобильного приложения для измерения уровня шума с использованием микрофона устройства. Приложение демонстрирует базовые принципы работы с аудиосигналами и отображением данных в реальном времени.",
                        fontSize = 16.sp
                    )
                }

                Text(
                    text = "© 2025",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        navigator.replace(SoundAnalyzerScreen())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Приступить к проверке",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}
