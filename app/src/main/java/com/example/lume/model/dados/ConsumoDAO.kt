package com.example.lume.model.dados

import android.util.Log
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
        // Normaliza o nome inserido pelo usuário, removendo espaços e convertendo para minúsculas
        val nomeNormalizado = nome.trim().toLowerCase().replace("[^a-z0-9]".toRegex(), "")

        // Log para verificar o nome normalizado
        Log.d("ConsumoDAO", "Buscando consumo com nome normalizado: $nomeNormalizado")

        db.collection("consumos")
            .get()  // Busca todos os documentos da coleção
            .addOnSuccessListener { documents ->
                val consumoEncontrado = documents.firstOrNull { document ->
                    // Normaliza o nome do documento para comparar com o nome do usuário
                    val nomeDocumento = document.getString("nome")?.trim()?.toLowerCase()?.replace("[^a-z0-9]".toRegex(), "")
                    nomeDocumento == nomeNormalizado
                }

                if (consumoEncontrado != null) {
                    // Se encontrou um consumo com nome correspondente, retorne ele
                    val consumo = consumoEncontrado.toObject(Consumo::class.java)
                    callback(consumo)
                } else {
                    // Se nenhum documento foi encontrado, retorna null
                    callback(null)
                }
            }
            .addOnFailureListener {
                // Em caso de falha ao buscar os documentos
                callback(null)
            }
    }

    // Adiciona um novo consumo
    fun adicionar(consumo: Consumo, callback: (Boolean) -> Unit) {
        // Log para verificar o consumo que está sendo adicionado
        Log.d("ConsumoDAO", "Tentando adicionar novo consumo: $consumo")

        db.collection("consumos")
            .add(consumo)
            .addOnSuccessListener { documentReference ->
                // Log para confirmar que o consumo foi adicionado com sucesso
                Log.d("ConsumoDAO", "Consumo adicionado com sucesso! ID do documento: ${documentReference.id}")
                callback(true) // Sucesso ao adicionar
            }
            .addOnFailureListener { exception ->
                // Log de erro caso a adição falhe
                Log.e("ConsumoDAO", "Erro ao adicionar consumo: ${exception.message}", exception)
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
        // Log inicial para verificar o nome do consumo a ser atualizado
        Log.d("ConsumoDAO", "Iniciando atualização para o consumo: $consumo")

        db.collection("consumos")
            .whereEqualTo("nome", consumo.nome) // Busca o documento pelo nome
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents[0].id // Obtém o ID do documento encontrado
                    Log.d("ConsumoDAO", "Documento encontrado com ID: $documentId")

                    db.collection("consumos")
                        .document(documentId) // Atualiza usando o ID do documento encontrado
                        .set(consumo)
                        .addOnSuccessListener {
                            // Log para confirmar que o consumo foi atualizado com sucesso
                            Log.d("ConsumoDAO", "Consumo atualizado com sucesso no banco de dados.")
                            callback(true) // Sucesso
                        }
                        .addOnFailureListener { exception ->
                            // Log de erro caso a atualização falhe
                            Log.e("ConsumoDAO", "Erro ao atualizar consumo no banco de dados: ${exception.message}", exception)
                            callback(false) // Falha ao atualizar
                        }
                } else {
                    // Log quando nenhum documento for encontrado para o nome fornecido
                    Log.e("ConsumoDAO", "Nenhum documento encontrado para o nome: ${consumo.nome}")
                    callback(false) // Nenhum documento encontrado com o nome fornecido
                }
            }
            .addOnFailureListener { exception ->
                // Log de erro caso a busca falhe
                Log.e("ConsumoDAO", "Erro ao buscar documento para atualização: ${exception.message}", exception)
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





