package com.ril
import kotlinx.serialization.Serializable


@Serializable
data class User(val id: Int, val name: String, val email: String)