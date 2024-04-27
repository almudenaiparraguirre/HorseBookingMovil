package com.example.horsebooking.Clases

import java.util.Date

class Clase {
    var codigo: String = ""
    var titulo: String = ""
    var descripcion: String = ""
    var precio: String = ""
    var tipo: String = ""
    var fechaInicio: Date? = null
    var fechaFin: Date? = null
    override fun toString(): String {
        return "Clase(codigo='$codigo', titulo='$titulo', descripcion='$descripcion', precio='$precio', fechaInicio=$fechaInicio, fechaFin=$fechaFin)"
    }

    constructor() {
        this.codigo = ""
        this.titulo = ""
        this.fechaInicio = null
        this.fechaFin = null
        this.precio = ""
        this.descripcion = ""
        this.tipo = ""
    }

    constructor(codigo: String, titulo: String, fechaInicio: Date?, fechaFin: Date?, precio: String, descripcion: String, tipo: String) {
        this.codigo = codigo
        this.titulo = titulo
        this.fechaInicio = fechaInicio
        this.fechaFin = fechaFin
        this.precio = precio
        this.descripcion = descripcion
        this.tipo = tipo
    }
}
