package com.example.talleresjetpack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Taller04Screen(navController: NavHostController, viewModel: FinanzasViewModel) {
    val transacciones by viewModel.transacciones.collectAsState()
    var descripcion by rememberSaveable { mutableStateOf("") }
    var monto by rememberSaveable { mutableStateOf("") }

    val totalIngresos = transacciones.filter { it.tipo == "INGRESO" }.sumOf { it.monto }
    val totalGastos = transacciones.filter { it.tipo == "GASTO" }.sumOf { it.monto }
    val balance = totalIngresos - totalGastos

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App de Finanzas") },
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
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Balance Total", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(
                        text = "$${balance}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ingresos: $${totalIngresos}", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                        Text("Gastos: $${totalGastos}", color = Color(0xFFF44336), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        val montoDouble = monto.toDoubleOrNull()
                        if (descripcion.isNotBlank() && montoDouble != null) {
                            viewModel.agregar(descripcion, montoDouble, "INGRESO")
                            descripcion = ""
                            monto = ""
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Ingreso")
                }
                Button(
                    onClick = {
                        val montoDouble = monto.toDoubleOrNull()
                        if (descripcion.isNotBlank() && montoDouble != null) {
                            viewModel.agregar(descripcion, montoDouble, "GASTO")
                            descripcion = ""
                            monto = ""
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("Gasto")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Movimientos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(transacciones, key = { it.id }) { transaccion ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(transaccion.descripcion, fontWeight = FontWeight.Bold)
                                Text(transaccion.tipo, fontSize = 12.sp, color = Color.Gray)
                            }
                            Text(
                                text = "$${transaccion.monto}",
                                fontWeight = FontWeight.Bold,
                                color = if (transaccion.tipo == "INGRESO") Color(0xFF4CAF50) else Color(0xFFF44336)
                            )
                            IconButton(onClick = { viewModel.borrar(transaccion) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}