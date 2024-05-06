package com.example.horsebooking.Clases

import java.util.Date

class Clase {
    var codigo: String = ""
    var titulo: String = ""
    var descripcion: String = ""
    var precio: String = ""
    var tipo: String = ""
    var fecha_inicio: String? = null
    var fecha_fin: String? = null
    override fun toString(): String {
        return "Clase(codigo='$codigo', titulo='$titulo', descripcion='$descripcion', precio='$precio', fechaInicio=$fecha_inicio, fechaFin=$fecha_fin)"
    }

    constructor() {
        this.codigo = ""
        this.titulo = ""
        this.fecha_inicio = null
        this.fecha_fin = null
        this.precio = ""
        this.descripcion = ""
        this.tipo = ""
    }

    constructor(codigo: String, titulo: String, fechaInicio: String?, fechaFin: String?, precio: String, descripcion: String, tipo: String) {
        this.codigo = codigo
        this.titulo = titulo
        this.fecha_inicio = fecha_inicio
        this.fecha_fin = fecha_fin
        this.precio = precio
        this.descripcion = descripcion
        this.tipo = tipo
    }
}
