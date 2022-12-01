document.getElementById("playerAddForm").addEventListener('submit', handleForm)
document.getElementById("tournamentAddForm").addEventListener('submit', handleForm)
document.getElementById("rulesetAddForm").addEventListener('submit', handleForm)

function handleForm(event) {
  event.preventDefault();
}

function toggleAddView(clicked_id) {
  var clickedButton = document.getElementById(clicked_id);
  var clickedButtonState = clickedButton.dataset.state;

  if (clickedButtonState == "INACTIVE") {
    var retiredButton = document.querySelector('[data-state="ACTIVE"]');
    if (retiredButton != null) {
      retiredButton.style.opacity = "0.6";
      retiredButton.dataset.state = "INACTIVE";
      document.getElementById(retiredButton.id.slice(0, -6)).style.display = 'none';
    }
    clickedButton.dataset.state = "ACTIVE";
    clickedButton.style.opacity = "1";
    document.getElementById(clicked_id.slice(0, -6)).style.display = 'block';
  }
  else if (clickedButtonState == "ACTIVE") {
  clickedButton.dataset.state = "INACTIVE";
    clickedButton.style.opacity = "0.6";
    document.getElementById(clicked_id.slice(0, -6)).style.display = 'none';
  }
}

function tournamentSearchInputChange() {
  var dropdown = document.getElementById("tournament-search");
  var value = dropdown.value;
  var text = dropdown.options[dropdown.selectedIndex].text;
  console.log(value, text);

  var input = document.getElementById("tournamentSearchFilter");

  if (text == "Tournament Date") {
    input.type = "date";
  } else {
    input.type = "text";
  }
}

function addPlayer() {

  var form = document.getElementById("playerAddForm")

  const formData = new FormData(form)

  fetch("/players/create", {
      method: "POST",
      body:   formData
  })
  .then(res => res.json()).then(data => {
    var para = document.createElement('p')
    var addDiv = document.getElementById("playerAdd")

    para.innerText = data["operation"]

    addDiv.appendChild(para)
  })
}

function addTournament() {

  var form = document.getElementById("tournamentAddForm")

  const formData = new FormData(form)

  fetch("/tournament/create", {
      method: "POST",
      body:   formData
  })
  .then(res => res.json()).then(data => {
    var para = document.createElement('p')
    var addDiv = document.getElementById("tournamentAdd")

    para.innerText = data["operation"]

    addDiv.appendChild(para)
  })
}

function addRuleset() {

  var form = document.getElementById("rulesetAddForm")

  const formData = new FormData(form)

  fetch("/ruleset/create", {
      method: "POST",
      body:   formData
  })
  .then(res => res.json()).then(data => {
    var para = document.createElement('p')
    var addDiv = document.getElementById("rulesetAdd")

    para.innerText = data["operation"]

    addDiv.appendChild(para)
  })
}

function playerSearch() {

  const searchType = document.getElementById("player-search").value;
  const searchFilter = document.getElementById("playerSearchFilter").value;
  
  const resultsContainer = document.getElementById("results-container")

  const searchParams = new URLSearchParams();
  
  searchParams.append("searchType", searchType)
  searchParams.append("searchFilter", searchFilter)


  const request = new Request("/players/search?" + searchParams.toString())

  fetch(request).then((response) => response.json())
    .then((data) => {
      for (var i = 0; i < data.length; i++) {
        if (data[i] != null)
        {
          child = document.createElement('p')
          child.innerText = JSON.stringify(data[i])
          resultsContainer.appendChild(child)
        }
      }
    })
}

function tournamentSearch() {

  const searchType = document.getElementById("tournament-search").value;
  const searchFilter = document.getElementById("tournamentSearchFilter").value;
  
  const resultsContainer = document.getElementById("results-container")

  const searchParams = new URLSearchParams();
  
  searchParams.append("searchType", searchType)
  searchParams.append("searchFilter", searchFilter)


  const request = new Request("/tournament/search?" + searchParams.toString())

  fetch(request).then((response) => response.json())
    .then((data) => {
      for (var i = 0; i < data.length; i++) {
        if (data[i] != null)
        {
          child = document.createElement('p')
          child.innerText = JSON.stringify(data[i])
          resultsContainer.appendChild(child)
        }
      }
    })
}


function rulesetSearch() {

  const searchType = document.getElementById("ruleset-search").value;
  const searchFilter = document.getElementById("rulesetSearchFilter").value;
  
  const resultsContainer = document.getElementById("results-container")

  const searchParams = new URLSearchParams();
  
  searchParams.append("searchType", searchType)
  searchParams.append("searchFilter", searchFilter)


  const request = new Request("/ruleset/search?" + searchParams.toString())

  fetch(request).then((response) => response.json())
    .then((data) => {
      for (var i = 0; i < data.length; i++) {
        if (data[i] != null)
        {
          child = document.createElement('p')
          child.innerText = JSON.stringify(data[i])
          resultsContainer.appendChild(child)
        }
      }
    })
}