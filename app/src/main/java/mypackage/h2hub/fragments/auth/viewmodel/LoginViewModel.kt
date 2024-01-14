package mypackage.h2hub.fragments.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import mypackage.h2hub.repository.AuthRepository
import mypackage.h2hub.repository.MainRepository
import mypackage.h2hub.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject
//import mypackage.h2hub.repository.MainRepository

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val mainRepository: MainRepository
): ViewModel() {
    private val _loginRequest = MutableLiveData<Resource<String>>()
    val loginRequest = _loginRequest as LiveData<Resource<String>>
    //private val _userType = MutableLiveData<Resource<String>>()
    //val userType = _userType as LiveData<Resource<String>>

    fun login(email: String, password: String){
        viewModelScope.launch {
            _loginRequest.value = Resource.Loading
            try {
                repository.login(email, password){
                    _loginRequest.value = it
                }
            }catch (e: Exception){
                _loginRequest.value = Resource.Error(e.message.toString())
            }
        }
    }
    /*fun getUserType(uid: String){
        viewModelScope.launch {
            _userType.value = Resource.Loading
            try {
                mainRepository.getUserType(uid){
                    _userType.value = it
                }
            }catch (e: Exception){
                _userType.value = Resource.Error(e.message.toString())
            }
        }
    }*/
}