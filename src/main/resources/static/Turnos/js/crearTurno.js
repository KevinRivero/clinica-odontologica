//! VALIDADOR DE CAMPOS DE LOS FORMULARIOS 
const validateForm = (...campos) => {
    for (let i = 0; i < campos.length; i++) {
        if (campos[i] === '') {
            alert('Por favor, el campo no puede estar vacío.')
            return false
        }
    }
    return true
}

// LLAMADOS A LA API
// MOSTRAR TURNO

const mostrarTurnos = async () => {
    try {
        const resp = await fetch("http://localhost:8080/turnos/listarturnos")
        const data = resp.json()
        return data
    } catch (e) {
        throw new Error(e)
    }
}

// REGISTRAR TURNO
const registrarTurno = async (data) => {
    try {
        await fetch("http://localhost:8080/turnos/crearturno", {
            method: 'POST',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(data)
        })
    } catch (e) {
        throw new Error(e)
    }

}

let pacienteIdInput = document.getElementById("pacienteId");
let apellidoInput = document.getElementById("apellidoPaciente");
let nombreInput = document.getElementById("nombrePaciente");
let dniInput = document.getElementById("dni");
let fechaIngresoPacienteInput = document.getElementById("fechaIngreso");
let odontologoIdInput = document.getElementById("odontologoId");
let nombreOdontologoInput = document.getElementById("nombreOdontologo");
let apellidoOdontologoInput = document.getElementById("apellidoOdontologo");
let matriculaInput = document.getElementById("matricula");
let fechaTurno = document.getElementById("fechaTurno")

document.addEventListener("DOMContentLoaded", () => {



    pacienteIdInput.addEventListener("input", () => {
        let pacienteId = pacienteIdInput.value

        // LLAMADO A LA API PARA BUSCAR PACIENTE POR ID
        fetch("http://localhost:8080/pacientes/buscarpaciente/" + pacienteId)
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    throw new Error("Error en la solicitud" + response.status)
                }
            })
            .then(paciente => {
                apellidoInput.value = paciente.apellido
                nombreInput.value = paciente.nombre
                dniInput.value = paciente.dni
                fechaIngresoPacienteInput.value = paciente.fechaIngreso
                apellidoInput.disabled = false
                nombreInput.disabled = false
                dniInput.disabled = false
                fechaIngresoPacienteInput.disabled = false
            })
            .catch(error => {
                console.error(error)
            })




        odontologoIdInput.addEventListener("input", () => {
            let odontologoId = odontologoIdInput.value

            // LLAMADO A LA API PARA BUSCAR ODONTOLOGO POR ID
            fetch("http://localhost:8080/odontologos/buscarodontologo/" + odontologoId)
                .then(response => {
                    if (response.ok) {
                        return response.json()
                    } else {
                        throw new Error("Error en la solicitud" + response.status)
                    }
                })
                .then(odontologo => {
                    nombreOdontologoInput.value = odontologo.nombre
                    apellidoOdontologoInput.value = odontologo.apellido
                    matriculaInput.value = odontologo.matricula
                    nombreOdontologoInput.disabled = false
                    apellidoOdontologoInput.disabled = false
                    matriculaInput.disabled = false
                })
                .catch(error => {
                    console.error(error)
                })
        })

    })

})


const renderizarDatos = async () => {
    const data = await mostrarTurnos()
    const table = document.getElementById("table")
    table.innerHTML = data.map((data) => {
        return (`<tr><td scope="row">${data.id}</td>
        <td>${data.paciente.nombre} ${data.paciente.apellido}</td>
        <td>${data.odontologo.apellido} ${data.odontologo.nombre}</td>
        <td>${data.fecha}</td>
        </tr>`)
    }).join("")


}

// para que, cuando recargue la página muestre los turnos que ya están cargados
window.addEventListener("load", () => {
    
    renderizarDatos()
})

const buscarPacientePorId = async (id) => {
    try {
        const response = await fetch("http://localhost:8080/pacientes/buscarpaciente/" + id)
        if (response.ok) {
            const paciente = await response.json()
            let data = JSON.stringify(paciente)

            return data
        }
    } catch (error) {
        console.error(error)
    }

}



// REGISTRAR TURNO

form.addEventListener("submit", e => {

    e.preventDefault()

    let pacienteId = pacienteIdInput.value

    buscarPacientePorId(pacienteId)
        .then(data => {
            data = JSON.parse(data)


            console.log("data " + data);

            let paciente = {
                id: data.id,
                apellido: data.apellido,
                nombre: data.nombre,
                dni: data.dni,
                fechaIngreso: data.fechaIngreso,
                domicilio: data.domicilio
            }



            let odontologo = {
                id: odontologoIdInput.value,
                nombre: nombreOdontologoInput.value,
                apellido: apellidoOdontologoInput.value,
                matricula: matriculaInput.value
            }

            turno = {
                paciente: paciente,
                odontologo: odontologo,
                fecha: fechaTurno.value
            }

            console.log(turno);
            registrarTurno(turno)
        })
        .catch(e => {
            console.error(e)
        })
        renderizarDatos()
})

