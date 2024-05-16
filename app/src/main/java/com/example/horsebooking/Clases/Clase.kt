package com.example.horsebooking.Clases

import java.util.Date

class Clase {
    var id: String = ""
    var titulo: String = ""
    var descripcion: String = ""
    var precio: Long = 0
    var tipo: String = ""
    var fecha_inicio: String? = null
    var fecha_fin: String? = null
    var hora: Long = 0
    var minuto: Long = 0
    var booked: Boolean = false

    override fun toString(): String {
        return "Clase(id='$id', titulo='$titulo', descripcion='$descripcion', precio='$precio', fechaInicio=$fecha_inicio, fechaFin=$fecha_fin, hora=$hora, minutos=$minuto, booked=$booked)"
    }

    constructor() {
        this.id = ""
        this.titulo = ""
        this.fecha_inicio = null
        this.fecha_fin = null
        this.hora = 0
        this.minuto = 0
        this.precio = 0
        this.descripcion = ""
        this.tipo = ""
        this.booked = false
    }

    constructor(id: String, titulo: String, fechaInicio: String?, fechaFin: String?, hora: Long, minuto: Long, precio: Long, descripcion: String, tipo: String, booked: Boolean) {
        this.id = id
        this.titulo = titulo
        this.fecha_inicio = fecha_inicio
        this.fecha_fin = fecha_fin
        this.hora = hora
        this.minuto = minuto
        this.precio = precio
        this.descripcion = descripcion
        this.tipo = tipo
        this.booked = booked
    }
}
