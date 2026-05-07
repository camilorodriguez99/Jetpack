package com.example.talleresjetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.talleresjetpack.ui.theme.TalleresJetpackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TalleresJetpackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val tareasViewModel: TareasViewModel = viewModel()
    val weatherViewModel: WeatherViewModel = viewModel()

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") { MenuScreen(navController) }
        composable("taller1") { Taller01Screen(navController) }
        composable("taller2") { Taller02Screen(navController, tareasViewModel) }
        composable("detalle_tarea/{id}/{titulo}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            val titulo = backStackEntry.arguments?.getString("titulo") ?: ""
            DetalleTareaScreen(navController, id, titulo)
        }
        composable("taller3") { Taller03Screen(navController, weatherViewModel) }
        composable("taller4") {
            val finanzasViewModel: FinanzasViewModel = hiltViewModel()
            Taller04Screen(navController, finanzasViewModel)
        }
        composable("taller5") { Taller05Screen(navController) }
    }
}

@Composable
fun MenuScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Portafolio de Talleres", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate("taller1") }, modifier = Modifier.fillMaxWidth()) {
            Text("Taller 01: Perfil Declarativo")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("taller2") }, modifier = Modifier.fillMaxWidth()) {
            Text("Taller 02: Lista de Tareas")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("taller3") }, modifier = Modifier.fillMaxWidth()) {
            Text("Taller 03: App del Clima")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("taller4") }, modifier = Modifier.fillMaxWidth()) {
            Text("Taller 04: Finanzas Room")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("taller5") }, modifier = Modifier.fillMaxWidth()) {
            Text("Taller 05: Asistente IA")
        }
    }
}