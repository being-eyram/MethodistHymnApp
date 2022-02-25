package com.example.methodisthymnapp.ui.screens.hymns

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.methodisthymnapp.data.Hymn
import com.example.methodisthymnapp.reposiitory.MHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HymnContentViewModel @Inject constructor (private val repository: MHARepository) : ViewModel() {

    private val _result = MutableLiveData<Hymn>()
    val result: LiveData<Hymn>
        get() = _result

    fun getHymn(id: Int) {
        viewModelScope.launch {
            val hymn = repository.getHymn(id)
            withContext(Dispatchers.Main) {
                _result.value = hymn
            }
        }
    }
}
