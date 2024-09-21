package com.example.prova


import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }
}

class Inventario {
    companion object {
        val produtos = mutableListOf<Produto>()

        fun adicionaProduto(produto: Produto) {
            produtos.add(produto)
        }

        fun valorTotalEstoque(): Double {
            return produtos.sumOf { it.preco * it.quantidade }
        }

        fun quantidadeTotal(): Int {
            return produtos.sumOf { it.quantidade }
        }
    }
}

@Composable
fun telaCadastro(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {

        Text(text = "Cadastro de Produtos", fontSize = 30.sp)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do Produto") })

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoria") })

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") })

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = quantidade, onValueChange = { quantidade = it }, label = { Text("Quantidade em Estoque") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (nome.isBlank() || categoria.isBlank() || preco.isBlank() || quantidade.isBlank()) {
                Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
            } else {
                val valorPreco = preco.toDoubleOrNull()
                val valorQuantidade = quantidade.toIntOrNull()
                if (valorPreco == null || valorPreco < 0) {
                    Toast.makeText(context, "Preço deve ser maior ou igual a 0", Toast.LENGTH_SHORT).show()
                } else if (valorQuantidade == null || valorQuantidade < 1) {
                    Toast.makeText(context, "Quantidade deve ser maior que 0", Toast.LENGTH_SHORT).show()
                } else {
                    val produto = Produto(nome, categoria, valorPreco, valorQuantidade)
                    Inventario.adicionaProduto(produto)
                    navController.navigate("listaProdutos")
                }
            }
        }) {
            Text("Cadastrar")
        }
    }
}

@Composable
fun listaProdutos(navController: NavController) {
    val produtos = Inventario.produtos

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Produtos Cadastrados", fontSize = 30.sp)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(produtos) { produto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${produto.nome} (${produto.quantidade} Unidades)")
                    Button(onClick = {
                        navController.navigate("detalhesProduto/${produto.nome}")
                    }) {
                        Text("Detalhes")
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("estatisticas") }) {
            Text("Estatisticas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("cadastroProduto") }) {
            Text("Cadastrar Novo Produto")
        }
    }
}

@Composable
fun listagemProdutos(produto: Produto, navController: NavController) {
    Row(modifier = Modifier
        .padding(16.dp)
        .clickable {
            navController.navigate("detalhesProduto/${produto.nome}")
        }) {
        Text("${produto.nome} (${produto.quantidade} Unidades)")
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { navController.navigate("detalhesProduto/${produto.nome}") }) {
            Text("Detalhes")
        }
    }
}


@Composable
fun telaDetalhesProduto(navController: NavController, nomeProduto: String) {
    val produto = Inventario.produtos.find { it.nome == nomeProduto }

    produto?.let {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text("Nome: ${it.nome}")
            Text("Categoria: ${it.categoria}")
            Text("Preço: R$ ${it.preco}")
            Text("Quantidade: ${it.quantidade} Unidades")
            Button(onClick = { navController.popBackStack() }) {
                Text("Voltar")
            }
        }
    } ?: run {
        Text("Produto não encontrado")
    }
}


@Composable
fun Main() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "cadastroProduto") {
        composable("cadastroProduto") { telaCadastro(navController) }
        composable("listaProdutos") { listaProdutos(navController) }
        composable("detalhesProduto/{nomeProduto}") { backStackEntry ->
            val nomeProduto = backStackEntry.arguments?.getString("nomeProduto") ?: ""
            telaDetalhesProduto(navController, nomeProduto)
        }
        composable("estatisticas") { telaEstatisticas(navController) }
    }
}

@Composable
fun telaEstatisticas(navController: NavController) {
    val valorTotal = Inventario.valorTotalEstoque()
    val quantidadeTotal = Inventario.quantidadeTotal()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Valor Total do Estoque: R$ $valorTotal")
        Text("Quantidade Total de Produtos: $quantidadeTotal")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    telaCadastro(navController = rememberNavController())
}
