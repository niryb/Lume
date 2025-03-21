package com.example.lume.model.dados

data class Consumo(
    var id: String = "",
    val nome: String = "",
    val dataConsumo: String = null.toString(),
    val tipo: String = "",
    val genero: String = "",
    val descricao: String = "",
    val avaliacao: String = "",
    val comentarioPessoal: String = "",
    val capaUrl: String? = null,
    val imagemUri: String? = null
)