package com.zia.tugas5_pam

data class Note(
    val id: Int = 0,
    val title: String,
    val description: String,
    val date: Long = System.currentTimeMillis()
)
