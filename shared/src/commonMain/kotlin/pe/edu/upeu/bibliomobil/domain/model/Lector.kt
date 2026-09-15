package pe.edu.upeu.bibliomobil.domain.model

data class Lector(
    val id: Long,
    val nombre: String,
    val correo: String,
    val telefono: String?
) {
    init {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacio" }
        require(correo.isNotBlank()) { "El correo no puede estar vacio" }
        require(telefono == null || telefono.isNotBlank()) { "El telefono debe ser nulo o no vacio" }
    }
}
