package com.example.lume.model.dados

import androidx.compose.runtime.Composable
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class ConsumoDAO {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Busca todos os consumos
    fun buscar(callback: (List<Consumo>) -> Unit) {
        db.collection("consumos").get()
            .addOnSuccessListener { documents ->
                val consumos = documents.map { document ->
                    // Converte o documento em um objeto Consumo e define o ID
                    val consumo = document.toObject(Consumo::class.java)
                    consumo.id = document.id // Adiciona o ID do documento ao objeto
                    consumo
                }
                callback(consumos)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    // Busca um consumo pelo ID
    fun buscarPorId(id: String, callback: (Consumo?) -> Unit) {
        db.collection("consumos")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val consumo = document.toObject(Consumo::class.java)
                    consumo?.id = document.id // Adiciona o ID do documento ao objeto
                    callback(consumo)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
    // Busca um consumo pelo nome
    fun buscarPorNome(nome: String, callback: (Consumo?) -> Unit) {
        db.collection("consumos")
            .whereEqualTo("nome", nome) // Filtra os documentos pelo campo "nome"
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Nenhum documento encontrado
                    callback(null)
                } else {
                    // Retorna o primeiro consumo encontrado (assumindo que o nome é único)
                    val consumo = documents.toObjects<Consumo>().firstOrNull()
                    callback(consumo)
                }
            }
            .addOnFailureListener {
                // Em caso de erro, retorna null
                callback(null)
            }
    }

    // Adiciona um novo consumo
    fun adicionar(consumo: Consumo, callback: (Boolean) -> Unit) {
        db.collection("consumos")
            .add(consumo)
            .addOnSuccessListener { documentReference ->
                callback(true) // Sucesso ao adicionar
            }
            .addOnFailureListener {
                callback(false) // Falha ao adicionar
            }
    }

    fun atualizar(consumo: Consumo, callback: (Boolean) -> Unit) {
        if (consumo.id.isEmpty()) {
            callback(false)
            return
        }

        db.collection("consumos")
            .document(consumo.id) // Usa o ID do documento para atualizar
            .set(consumo)
            .addOnSuccessListener {
                callback(true) // Sucesso
            }
            .addOnFailureListener {
                callback(false) // Falha
            }
    }

    fun atualizarPorNome(consumo: Consumo, callback: (Boolean) -> Unit) {
        db.collection("consumos")
            .whereEqualTo("nome", consumo.nome) // Busca o documento pelo nome
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents[0].id // Obtém o ID do documento encontrado
                    db.collection("consumos")
                        .document(documentId) // Atualiza usando o ID do documento encontrado
                        .set(consumo)
                        .addOnSuccessListener {
                            callback(true) // Sucesso
                        }
                        .addOnFailureListener {
                            callback(false) // Falha ao atualizar
                        }
                } else {
                    callback(false) // Nenhum documento encontrado com o nome fornecido
                }
            }
            .addOnFailureListener {
                callback(false) // Falha ao buscar o documento
            }
    }


    // Busca consumos em tempo real
    fun buscarEmTempoReal(callback: (List<Consumo>) -> Unit) { //usa lambda
        db.collection("consumos").addSnapshotListener(
            EventListener<QuerySnapshot> { snapshots, error ->
                if (error != null || snapshots == null) {
                    callback(emptyList())
                    return@EventListener
                }
                val consumos = snapshots.toObjects(Consumo::class.java)
                callback(consumos)
            }
        )
    }


    fun removerPorNome(nome: String, callback: (Boolean) -> Unit) {
        db.collection("consumos")
            .whereEqualTo("nome", nome) // Filtra pelo nome
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        db.collection("consumos").document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                callback(true) // Sucesso ao excluir
                            }
                            .addOnFailureListener { error ->
                                println("Erro ao excluir consumo: ${error.message}")
                                callback(false) // Falha ao excluir
                            }
                    }
                } else {
                    callback(false) // Nenhum item com esse nome encontrado
                }
            }
            .addOnFailureListener { error ->
                println("Erro ao buscar consumo: ${error.message}")
                callback(false) // Falha na busca
            }
    }
}





//TO DO: os buscar