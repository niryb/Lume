package com.example.lume.model.dados

data class Consumo(
    var id: String = "",
    val nome: String = "",
    val dataConsumo: String = "",
    val tipo: String = "",
    val genero: String = "",
    val descricao: String = "",
    val avaliacao: String = "", // Usando Float diretamente
    val comentarioPessoal: String = ""
)