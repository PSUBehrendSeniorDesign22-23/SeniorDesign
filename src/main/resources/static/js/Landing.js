let logModal = document.getElementById("idLog")
let signModal = document.getElementById("idSign")

window.onclick = function(event) {
    if (event.target == logModal) {
      logModal.style.display = "none";
    } else if (event.target == signModal)
      signModal.style.display = "none";
}