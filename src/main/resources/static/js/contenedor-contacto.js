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

        if (tipo === "EMAIL") {
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (!emailRegex.test(contacto)) {
                        alert("Ingrese un correo electrónico válido.");
                        return;
                    }
        }

        const index = tablaContactos.children.length;

        // Crear fila en la tabla
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${tipoText}</td>
            <td>${contacto}</td>
            <td>
                <button type="button" class="btn btn-danger btn-eliminar">Eliminar</button>
                <input type="hidden" name="contactos[${index}].medio" value="${tipo}">
                <input type="hidden" name="contactos[${index}].contacto" value="${contacto}">
            </td>
        `;
        tablaContactos.appendChild(fila);

        // Limpiar el campo de contacto
        document.getElementById("contacto").value = "";

        // Agregar funcionalidad al botón eliminar
        fila.querySelector(".btn-eliminar").addEventListener("click", () => {
            fila.remove();

             [...tablaContactos.children].forEach((row, newIndex) => {
                const inputs = row.querySelectorAll("input[type='hidden']");
                inputs[0].setAttribute("name", `contactos[${newIndex}].medio`);
                inputs[1].setAttribute("name", `contactos[${newIndex}].contacto`);
             });
        });
    });
});