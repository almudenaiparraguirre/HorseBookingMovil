package com.example.horsebooking.Novedades

data class Novedad(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = ""
) {
    constructor() : this("", "", "", "")
}