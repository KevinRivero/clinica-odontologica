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
    
    // MOSTRAR PACIENTE

const mostrarPacientes = async() => {
    try{
        const resp = await fetch("http://localhost:8080/pacientes/listarpacientes")
        const data = resp.json()
        return data
    }catch(e){
        throw new Error(e)
    }
}

//RENDERIZAR LA LISTA DE PACIENTES
    
const renderizarDatos = async ()  => {
    const data = await mostrarPacientes()
    const table = document.getElementById("table")
        table.innerHTML= data.map((data)=>{
        return (`<tr><td scope="row">${data.id}</td>
        <td>${data.apellido}</td>
        <td>${data.nombre}</td>
        <td>${data.dni}</td>
        <td>${data.fechaIngreso}</td>
        <td>${data.domicilio.calle} ${data.domicilio.numero},${data.domicilio.provincia} ${data.domicilio.localidad} </td>
        </tr>`)
        }).join("")

    
}

// para que, cuando recargue la página muestre los pacientes que ya están cargados
window.addEventListener("load", ()=>{
    renderizarDatos()
})
    
    // REGISTRAR PACIENTE
    
    const form = document.getElementById("form")

    const registrarPaciente = async (data) => {
    try{
        await fetch("http://localhost:8080/pacientes/crearpaciente",{
            method: 'POST',
            headers: {
                "Content-type":"application/json"
            },
            body: JSON.stringify(data)
        })
    }catch(e){
        throw new Error(e)
    }
    
    }
        

    // Boton para el submit
    form.addEventListener("submit", e=>{
                        
        const formData = new FormData(form)
        const objData = Object.fromEntries(formData)

        let domicilio,paciente

        if(validateForm(objData.numero, objData.calle, objData.localidad, objData.provincia, objData.apellido,objData.nombre, objData.dni,objData.fechaIngreso, objData.provincia,objData.localidad)){


        domicilio = {
            numero:objData.numero,
            calle: objData.calle,
            localidad: objData.localidad,
            provincia: objData.provincia
        }

        paciente = {
            apellido: objData.apellido,
            nombre: objData.nombre,
            dni: objData.dni,
            fechaIngreso:objData.fechaIngreso,
            domicilio: domicilio
        }
        console.log(paciente);
    registrarPaciente(paciente)
    }  
    
    renderizarDatos()
    
    })



    


    
    




















