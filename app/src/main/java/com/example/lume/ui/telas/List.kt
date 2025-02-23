import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.lume.ui.telas.CadastroConsumoScreen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lume.ui.telas.Consumo



@Composable
fun ListScreen(consumos: List<Consumo>) { // Recebe consumos como parâmetro
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Lista de Consumos", fontSize = 24.sp, modifier = Modifier.padding(16.dp))

        LazyColumn {
            items(consumos) { consumo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5E1BE))
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Column {
                            Text(consumo.nome, fontSize = 18.sp)
                            Text("Gênero: ${consumo.genero}")
                            Text("Avaliação: ${consumo.avaliacao.toInt()} estrelas")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    // Lista de exemplo para a pré-visualização
    val exemploConsumos = listOf(
        Consumo(nome = "Consumo 1", dataConsumo = "2025-02-23", genero = "Masculino", descricao = "Descrição 1", avaliacao = 4.0f),
        Consumo(nome = "Consumo 2", dataConsumo = "2025-02-22", genero = "Feminino", descricao = "Descrição 2", avaliacao = 3.0f),
        Consumo(nome = "Consumo 3", dataConsumo = "2025-02-21", genero = "Masculino", descricao = "Descrição 3", avaliacao = 5.0f)
    )
    ListScreen(consumos = exemploConsumos) // Passa a lista de exemplo para o ListScreen
}
