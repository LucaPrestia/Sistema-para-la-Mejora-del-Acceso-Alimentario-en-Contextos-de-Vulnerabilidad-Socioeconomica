function agregarContacto() {
    const tipoSelect = document.getElementById("tipo");
    const tipo = tipoSelect.value;
    const tipoText = tipoSelect.options[tipoSelect.selectedIndex].text;
    const contacto = document.getElementById("contacto").value;
    const tablaContactos = document.getElementById("tabla-contactos").querySelector("tbody");

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
    const fila = document.createElement("tr");
    fila.innerHTML = `
        <td>${tipoText}</td>
        <td>${contacto}</td>
        <td>
            <button type="button" class="btn btn-danger" onclick="eliminarFila(this)">Eliminar</button>
            <input type="hidden" name="contactos[${index}].medio" value="${tipo}">
            <input type="hidden" name="contactos[${index}].contacto" value="${contacto}">
        </td>
    `;
    tablaContactos.appendChild(fila);

    document.getElementById("contacto").value = "";
}

function eliminarFila(btn) {
    const fila = btn.closest("tr");
    const tablaContactos = fila.closest("tbody");
    fila.remove();
    [...tablaContactos.children].forEach((row, newIndex) => {
        const inputs = row.querySelectorAll("input[type='hidden']");
        inputs[0].setAttribute("name", `contactos.medio`);
        inputs[1].setAttribute("name", `contactos.contacto`);
    });
}

function agregarColaboracion(tipo) {
    const selectId = tipo === "humano" ? "tipoColaboracionHumano" : "tipoColaboracionJuridico";
    const tablaId = tipo === "humano" ? "tabla-colaboraciones-humano" : "tabla-colaboraciones-juridico";

    const select = document.getElementById(selectId);
    const tabla = document.getElementById(tablaId).querySelector("tbody");

    const colaboracionCodigo = select.value;
    const colaboracionNombre = select.options[select.selectedIndex].text;

    if (!colaboracionCodigo) {
        alert("Seleccione un tipo de colaboración.");
        return;
    }

    // Verificar si ya existe en la tabla
    const filas = tabla.querySelectorAll("tr");
    for (const fila of filas) {
        const textoFila = fila.querySelector("td").innerText;
        if (textoFila === colaboracionNombre) {
            alert("Esta colaboración ya fue agregada.");
            return;
        }
    }

    // Obtener el índice para el nuevo input
    const index = tabla.children.length;

    // Crear una nueva fila en la tabla
    const fila = document.createElement("tr");
    fila.innerHTML = `
        <td>${colaboracionNombre}</td>
        <td>
            <button type="button" class="btn btn-danger" onclick="eliminarColaboracion(this, '${tipo}')">Eliminar</button>
            <input type="hidden" name="colaboraciones${tipo.charAt(0).toUpperCase() + tipo.slice(1)}[${index}]" value="${colaboracionCodigo}">
        </td>
    `;

    tabla.appendChild(fila);
}

function eliminarColaboracion(boton, tipo) {
    const fila = boton.closest("tr");
    const tabla = fila.closest("tbody");
    fila.remove();

    // Actualizar los índices de todos los inputs restantes
    [...tabla.children].forEach((row, newIndex) => {
        const input = row.querySelector("input[type='hidden']");
        input.setAttribute("name", `colaboraciones${tipo.charAt(0).toUpperCase() + tipo.slice(1)}[${newIndex}]`);
    });
}

// suscripciones:
function validarSuscripcion(event) {
    let checkboxes = document.querySelectorAll("input[type='checkbox'][name='preferencias']");
    let valid = true;
    let mensaje = "Debes completar los siguientes campos:\n";

    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            let inputNumber = document.getElementById("cantidad_" + checkbox.value);
            if (inputNumber && inputNumber.value.trim() === "") {
                mensaje += `- Ingresar cantidad para ${checkbox.nextElementSibling.innerText}\n`;
                valid = false;
            }
        }
    });

    if (!valid) {
        alert(mensaje);
        event.preventDefault(); // Evita el envío del formulario
    }
}