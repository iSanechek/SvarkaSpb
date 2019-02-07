package com.isanechek.myapplication.data.models

sealed class AuthState {
    object NeedLogin : AuthState()
    object CloseScreen : AuthState()
    object LoadData : AuthState()
}