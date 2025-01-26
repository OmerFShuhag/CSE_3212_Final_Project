package com.example.traction.student

import java.util.UUID

data class Student(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val salary: String = "",
    val teachingTime: String = "",
    val teachingDays: List<String> = emptyList(),
    val attendance: List<String> = emptyList()
)
