package com.example.lume.Util



import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DataUtil {
    fun formatarData(timestamp: Timestamp?): String {
        if (timestamp == null) return "Sem data"
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formato.format(timestamp.toDate()) //converte Timestamp para String
        }
}
