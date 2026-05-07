package com.example.talleresjetpack

import android.Manifest
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Taller05Screen(navController: NavHostController) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var ingredientesDetectados by remember { mutableStateOf(setOf<String>()) }
    var recetaGenerada by remember { mutableStateOf<String?>(null) }
    var cargandoReceta by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asistente de Recetas IA") },
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }) { Text("Volver") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (cameraPermissionState.status.isGranted) {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    CameraPreview { labels ->
                        ingredientesDetectados = ingredientesDetectados + labels
                    }
                    Box(
                        modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp)).padding(8.dp)
                    ) {
                        Text("Apunta a tus ingredientes", color = Color.White)
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Se requiere permiso de cámara")
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) { Text("Permitir") }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text("Ingredientes Detectados:", style = MaterialTheme.typography.titleMedium)
                FlowRow(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ingredientesDetectados.forEach { label ->
                        SuggestionChip(onClick = { }, label = { Text(label) })
                    }
                }

                Button(
                    onClick = {
                        cargandoReceta = true
                        recetaGenerada = "Ensalada de " + ingredientesDetectados.joinToString(" con ") +
                                "\n\n1. Lava bien los ingredientes.\n2. Corta en trozos pequeños.\n3. Mezcla en un bol.\n4. Agrega sal y limón al gusto."
                        cargandoReceta = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = ingredientesDetectados.isNotEmpty()
                ) {
                    if (cargandoReceta) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    else Text("Generar Receta con IA")
                }

                Spacer(modifier = Modifier.height(16.dp))

                recetaGenerada?.let { receta ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Receta Sugerida", fontWeight = FontWeight.Bold)
                            Text(receta)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CameraPreview(onLabelsDetected: (List<String>) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }
    val labeler = remember { ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(executor) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                        labeler.process(image)
                            .addOnSuccessListener { labels ->
                                val detected = labels.filter { it.confidence > 0.7f }.map { it.text }
                                if (detected.isNotEmpty()) onLabelsDetected(detected)
                            }
                            .addOnCompleteListener { imageProxy.close() }
                    }
                }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalysis)
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        Row(horizontalArrangement = horizontalArrangement) {
            content()
        }
    }
}