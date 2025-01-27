document.addEventListener("DOMContentLoaded", () => {
    const btnAgregar = document.getElementById("btn-agregar");
    const tablaContactos = document.getElementById("tabla-contactos").querySelector("tbody");

    btnAgregar.addEventListener("click", () => {
        const tipo = document.getElementById("tipo").value;
        const tipoText = document.getElementById("tipo").options[document.getElementById("tipo").selectedIndex].text;
        const contacto = document.getElementById("contacto").value;

        if (!tipo || !contacto) {
            alert("Debe completar todos los campos.");
            return;
        }

        // Crear fila en la tabla
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${tipoText}</td>
            <td>${contacto}</td>
            <td>
                <button type="button" class="btn btn-danger btn-eliminar">Eliminar</button>
                <input type="hidden" name="contactos[${tablaContactos.children.length}].medio" value="${tipo}">
                <input type="hidden" name="contactos[${tablaContactos.children.length}].contacto" value="${contacto}">
            </td>
        `;
        tablaContactos.appendChild(fila);

        // Limpiar el campo de contacto
        document.getElementById("contacto").value = "";

        // Agregar funcionalidad al botón eliminar
        fila.querySelector(".btn-eliminar").addEventListener("click", () => {
            fila.remove();
        });
    });
});