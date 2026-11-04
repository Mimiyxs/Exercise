package ca.qc.labo2formatif2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.labo2formatif2.Data.Livre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LivreViewModel : ViewModel() {
    val livres = MutableStateFlow<List<Livre>>(emptyList())
    fun loadLivres(title: String) {
        viewModelScope.launch {
            try {
                livres.value = (ApiClient.apiService.getLivres(title)).livres
            } catch (e: Exception) {
                Log.e("LivreVM"
                    , "Error: ${e.message}")
            }
        }
    }
}