package com.example.lume.model.dados

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
            .addOnSuccessListener { document ->
                val consumos = document.toObjects<Consumo>()
                callback(consumos)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    // Adiciona um novo consumo
    fun adicionar(consumo: Consumo, callback: (Boolean) -> Unit) {
        db.collection("consumos").add(consumo)
            .addOnSuccessListener {
                callback(true) // Sucesso
            }
            .addOnFailureListener { error ->
                println("Erro ao adicionar consumo: ${error.message}")
                callback(false) // Falha
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
}



//TO DO: os buscar