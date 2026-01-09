package com.jan.composeset.detailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlantDetailViewModelFactory(
    private val onNavigateBack: () -> Unit
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantDetailViewModel::class.java)) {
            return PlantDetailViewModel(onNavigateBack) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
