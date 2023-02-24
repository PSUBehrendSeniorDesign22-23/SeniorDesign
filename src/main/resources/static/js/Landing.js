document.getElementById("logIn").addEventListener('submit', handleForm)
document.getElementById("signUp").addEventListener('submit', handleForm)

function handleForm(event) {
    event.preventDefault();
}

function logIn() {

    var form = document.getElementById("logInForm")

    const formData = new FormData(form)

    fetch("/login", {
        method: "POST",
        body:   formData
    })
        .then(res => res.json()).then(data => {
        var para = document.createElement('appUser')
        var addDiv = document.getElementById("idLog")

        para.innerText = data["operation"]

        addDiv.appendChild(para)
    })
}