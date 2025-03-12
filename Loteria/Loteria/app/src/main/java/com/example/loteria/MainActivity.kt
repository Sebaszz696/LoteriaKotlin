package com.example.loteria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loteria.ui.theme.LoteriaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoteriaTheme {
                NumberRollerApp()
            }
        }
    }
}

@Preview
@Composable
fun NumberRollerApp() {
    NumbersWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun NumbersWithButtonAndImage(modifier: Modifier = Modifier) {
    var results by remember { mutableStateOf(List(7) { (0..9).random() }) }
    var isRolling by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) } // Para mostrar el mensaje final
    val coroutineScope = rememberCoroutineScope()

    val numberImages = results.map { result ->
        when (result) {
            0 -> R.drawable.numero0
            1 -> R.drawable.numero1
            2 -> R.drawable.numero2
            3 -> R.drawable.numero3
            4 -> R.drawable.numero4
            5 -> R.drawable.numero5
            6 -> R.drawable.numero6
            7 -> R.drawable.numero7
            8 -> R.drawable.numero8
            else -> R.drawable.numero9
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título "NÚMERO"
            Text(
                text = "NÚMERO",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Fila con los primeros 4 números
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 0 until 4) {
                    Image(
                        painter = painterResource(numberImages[i]),
                        contentDescription = results[i].toString(),
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            // Texto "SERIE" en el centro
            Text(
                text = "SERIE",
                fontSize = 28.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Fila con los últimos 3 números
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 4 until 7) {
                    Image(
                        painter = painterResource(numberImages[i]),
                        contentDescription = results[i].toString(),
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "LANZAR"
            Button(
                onClick = {
                    if (!isRolling) {
                        isRolling = true
                        showMessage = false // Ocultar mensaje al iniciar
                        coroutineScope.launch {
                            val newResults = results.toMutableList()

                            for (i in newResults.indices) {
                                var tempResults = newResults.toList()

                                // Mientras no se detenga, sigue cambiando aleatoriamente
                                repeat(10) {
                                    tempResults = tempResults.mapIndexed { index, _ ->
                                        if (index >= i) (0..9).random() else newResults[index]
                                    }
                                    results = tempResults
                                    delay(20L)
                                }

                                // Fijamos el número final para este índice
                                newResults[i] = (0..9).random()
                                results = newResults.toList()

                                delay(0L)
                            }

                            // Mostrar mensaje con el resultado
                            showMessage = true
                            delay(5000L) // Mostrar el mensaje por 5 segundos
                            showMessage = false // Ocultar mensaje después de 5 segundos

                            isRolling = false
                        }
                    }
                }
            ) {
                Text(stringResource(R.string.Lanzar))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje con el resultado final
            if (showMessage) {
                val numero = results.take(4).joinToString("")
                val serie = results.takeLast(3).joinToString("")
                Text(
                    text = "¡Número: $numero - SERIE: $serie!",
                    fontSize = 20.sp,
                    color = androidx.compose.ui.graphics.Color.Red
                )
            }
        }
    }
}
