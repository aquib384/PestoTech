package com.example.pestotech.model

import java.util.Date

data class User(
    val chat: Chat?=null,
    val users: Users?=null,
    val email: String?=null,
    val id:String? = null,
    val message:String? = null,
    val time: Date? = null

){
    data class Users(
        val name:String?=null,
        val email:String?=null,
        val uid:String?=null,
        val imageUrl:String?=null
    )

    data class Chat(
        val message:String? = null,
        val email:String? = null,
        val time: Date? = null
    )
}
