package com.example.talleresjetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

data class Tarea(
    val id: Int,
    val titulo: String,
    val completada: Boolean = false
)

class TareasViewModel : ViewModel() {
    var tareas by mutableStateOf(emptyList<Tarea>())
    var idCounter by mutableIntStateOf(1)

    fun agregarTarea(titulo: String) {
        tareas = tareas + Tarea(idCounter, titulo)
        idCounter++
    }

    fun eliminarTarea(id: Int) {
        tareas = tareas.filter { it.id != id }
    }

    fun cambiarEstadoTarea(id: Int, completada: Boolean) {
        tareas = tareas.map { if (it.id == id) it.copy(completada = completada) else it }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Taller02Screen(navController: NavHostController, viewModel: TareasViewModel) {
    var mostrarDialogo by mutableStateOf(false)
    var nuevaTareaTitulo by mutableStateOf("")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tareas") },
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(viewModel.tareas, key = { it.id }) { tarea ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            viewModel.eliminarTarea(tarea.id)
                            true
                        } else false
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        }
                    }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { navController.navigate("detalle_tarea/${tarea.id}/${tarea.titulo}") }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tarea.titulo,
                                modifier = Modifier.weight(1f),
                                textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                            )
                            Checkbox(
                                checked = tarea.completada,
                                onCheckedChange = { checked ->
                                    viewModel.cambiarEstadoTarea(tarea.id, checked)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("Nueva Tarea") },
                text = {
                    TextField(
                        value = nuevaTareaTitulo,
                        onValueChange = { nuevaTareaTitulo = it },
                        isError = nuevaTareaTitulo.isBlank()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nuevaTareaTitulo.isNotBlank()) {
                                viewModel.agregarTarea(nuevaTareaTitulo)
                                nuevaTareaTitulo = ""
                                mostrarDialogo = false
                            }
                        }
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleTareaScreen(navController: NavHostController, id: Int, titulo: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Tarea") },
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "ID: $id", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = titulo, style = MaterialTheme.typography.bodyLarge)
        }
    }
}