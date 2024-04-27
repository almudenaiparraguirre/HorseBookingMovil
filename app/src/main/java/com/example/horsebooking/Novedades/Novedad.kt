package com.example.horsebooking.Novedades

data class Novedad(
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = ""
) {
    constructor() : this("", "", "")
}