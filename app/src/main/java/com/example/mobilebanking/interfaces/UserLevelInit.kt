package com.example.mobilebanking.interfaces

interface UserLevelInit {
    fun hasAccessLevel(levelType: String, levelCode: Int): Int
    fun getUserLevel(): String
}