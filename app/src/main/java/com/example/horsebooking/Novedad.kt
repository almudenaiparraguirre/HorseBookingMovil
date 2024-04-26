data class Novedad(
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = "" // Agrega la propiedad fecha
) {
    // Constructor sin argumentos necesario para Firebase
    constructor() : this("", "", "")
}