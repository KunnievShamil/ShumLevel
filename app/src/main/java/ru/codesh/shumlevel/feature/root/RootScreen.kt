package ru.codesh.shumlevel.feature.root

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import ru.codesh.shumlevel.core.permission.PermissionManager
import ru.codesh.shumlevel.core.permission.PermissionState
import ru.codesh.shumlevel.core.ui.base.BaseScreen

class RootScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: RootViewModel = koinViewModel()
        val permissionManager: PermissionManager = getKoin().get()

        val state by viewModel.state.collectAsState()

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                viewModel.setPermissionGranted(granted)
                if (!granted) {
                    Toast.makeText(
                        context,
                        "Доступ к микрофону не предоставлен",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        LaunchedEffect(Unit) {
            if (permissionManager.hasAudioPermission()) {
                viewModel.setPermissionGranted(true)
            } else {
                launcher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }

        when (state.permissionState) {
            PermissionState.Granted -> {
                // Здесь будет основной функционал
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Разрешение получено, скоро будет измерение шума")
                }
            }

            PermissionState.Denied -> {
                // Показываем сообщение и кнопку
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Для измерения уровня шума необходимо разрешение на доступ к микрофону.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        launcher.launch(Manifest.permission.RECORD_AUDIO)
                    }) {
                        Text("Предоставить доступ")
                    }
                }
            }

            PermissionState.Unknown -> {
                Box(modifier = Modifier.fillMaxSize())
            }
        }
    }
}