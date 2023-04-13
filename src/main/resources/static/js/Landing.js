let logModal = document.getElementById("idLog")
let signModal = document.getElementById("idSign")

logModal.addEventListener('submit', handleForm)
signModal.addEventListener('submit', handleForm)

window.onclick = function(event) {
    if (event.target == logModal) {
      logModal.style.display = "none";
    } else if (event.target == signModal)
      signModal.style.display = "none";
}

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

function signUp() {

    var form = document.getElementById("signUpForm")

    const formData = new FormData(form)


    fetch("/registerUser", {
        method: "POST",
        body:   formData
    })
        .then(res => res.json()).then(data => {
        var para = document.createElement('newUser')
        var addDiv = document.getElementById("idSign")

        para.innerText = data["operation"]

        addDiv.appendChild(para)
    })
}