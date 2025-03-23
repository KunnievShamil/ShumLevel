package ru.codesh.shumlevel.feature.root

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import ru.codesh.shumlevel.core.permission.PermissionManager
import ru.codesh.shumlevel.core.ui.base.BaseScreen
import ru.codesh.shumlevel.feature.aboutapp.AboutAppScreen

private const val FIST_OPEN_FLAG = "FIST_OPEN_FLAG"

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

        LaunchedEffect(state.permissionState) {
            if (permissionManager.hasAudioPermission()) {
                viewModel.setPermissionGranted(true)
                navigator.replace(AboutAppScreen())
            } else {
                launcher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Для измерения уровня шума необходимо разрешение на доступ к микрофону.\n\n" +
                        "Перейдите в настройки, предоставьте доступ к микрофону и вернитесь обратно, " +
                        "затем нажмите кнопку ниже для проверки доступа.",
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }) {
                Text("Открыть настройки")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                viewModel.setPermissionUnknown()
            }) {
                Text("Проверить доступ к микрофону")
            }
        }
    }
}
