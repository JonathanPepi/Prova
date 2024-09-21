package com.example.prova;

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prova.Produtos


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Cadastro()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cadastro() {

    var nomeProduto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var qntdEstoque by remember { mutableStateOf("") }

    var produtos by remember { mutableStateOf(listOf<Produtos>()) }
    val context = LocalContext.current

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top)
    {
        Text(text = "Cadastro de Produto", fontSize = 30.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(10.dp))


        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            value = nomeProduto, onValueChange = { nomeProduto = it },
            label = { Text(text = "Nome do Produto")})

        Spacer(modifier = Modifier.height(10.dp))

        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            value = categoria, onValueChange = { categoria = it },
            label = { Text(text = "Categoria")})

        Spacer(modifier = Modifier.height(10.dp))

        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            value = preco, onValueChange = { preco = it },
            label = { Text(text = "Preço")})

        Spacer(modifier = Modifier.height(10.dp))

        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            value = qntdEstoque, onValueChange = { qntdEstoque = it },
            label = { Text(text = "Quantidade em Estoque")})

        Spacer(modifier = Modifier.height(10.dp))

        Button(modifier = Modifier.fillMaxWidth().height(70.dp),
            onClick =
            {

                if (nomeProduto.isNotBlank() && categoria.isNotBlank() && preco.isNotBlank() && qntdEstoque.isNotBlank()) {

                    produtos = produtos + Produtos(nomeProduto, categoria, preco, qntdEstoque)
                    // limpar os campos de form
                    nomeProduto = ""
                    categoria = ""
                    preco = ""
                    qntdEstoque = ""

                } else {

                    Toast.makeText(context,
                        "Preencha todos os campos",
                        Toast.LENGTH_SHORT).show()

                }

            }) {
            Text(text = "Salvar Contato")

        }



    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Cadastro()
}