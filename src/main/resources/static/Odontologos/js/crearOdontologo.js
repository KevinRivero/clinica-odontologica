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
    
    // MOSTRAR ODONTOLOGO

const mostrarOdontologos = async() => {
    try{
        const resp = await fetch("http://localhost:8080/odontologos/listarodontologos")
        const data = resp.json()
        return data
    }catch(e){
        throw new Error(e)
    }
}

// RENDERIZAR LISTA DE ODONTOLOGOS
    
const renderizarDatos = async ()  => {
    const data = await mostrarOdontologos()
    const table = document.getElementById("table")
        table.innerHTML= data.map((data)=>{
        return (`<tr><td scope="row">${data.id}</td>
        <td>${data.nombre}</td>
        <td>${data.apellido}</td>
        <td>${data.matricula}</td>
        </tr>`)
        }).join("")

    
}

// para que, cuando recargue la página muestre los odontologos que ya están cargados
window.addEventListener("load", ()=>{
    renderizarDatos()
})
    
    // REGISTRAR ODONTOLOGO
    
    const form = document.getElementById("form")

    const registrarOdontologo = async (data) => {
    try{
        await fetch("http://localhost:8080/odontologos/crearodontologo",{
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


    // BOTON SUBMIT
        
    form.addEventListener("submit", e=>{
                        
        const formData = new FormData(form)
        const objData = Object.fromEntries(formData)

        let odontologo

        if(validateForm(objData.nombre, objData.apellido, objData.matricula)){

            odontologo = {
                nombre: objData.nombre,
                apellido: objData.apellido,
                matricula: objData.matricula,
            }

            console.log(odontologo);
    registrarOdontologo(odontologo)
    
        
    }  
    renderizarDatos()
    
    })
