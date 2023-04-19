document.getElementById("rulesetAddForm").addEventListener("submit", handleForm)

function handleForm(event) {
  event.preventDefault()
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
  
  var input = document.getElementById("tournamentSearchFilter");
  
  if (text == "Tournament Date") {
  	input.type = "date";
	} else {
 		input.type = "text";
	}
}

function showSearchResults() {
  var searchResults = document.getElementById("searchResults");
  searchResults.style.display = 'block'
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
  
  let rulesetAttributes = {
    "rulesetName": document.getElementById("addrname").value,
    "rulesetOrigin": document.getElementById("addrorigin").value
  }

  for (let i = 1; i <= ruleInputCount; i++) {
    let ruleKey = document.getElementById("rule" + i + "Key").value
    let ruleValue = document.getElementById("rule" + i + "Value").value
    rulesetAttributes[ruleKey] = ruleValue
  }

  fetch("/ruleset/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body:   JSON.stringify(rulesetAttributes)
  })
  .then(res => res.json()).then(data => {
    var para = document.createElement('p')
    var addDiv = document.getElementById("rulesetAdd")

    para.innerText = data["operation"]

    addDiv.appendChild(para)
  })
}

// These functions are for adding or removing rule inputs
let ruleInputCount = 1
let lastRuleInput = document.getElementById("rule1Value")
function addRuleInput() {
  ruleInputCount++

  let newRuleKeyLabel = document.createElement("label")
  newRuleKeyLabel.setAttribute("for", "rule" + ruleInputCount + "Key")
  newRuleKeyLabel.innerText = "Rule " + ruleInputCount + " Name: "

  let newRuleKeyInput = document.createElement("input")
  newRuleKeyInput.id = "rule" + ruleInputCount + "Key"
  newRuleKeyInput.type = "text"
  newRuleKeyInput.name = "rule" + ruleInputCount 

  let newRuleValueLabel = document.createElement("label")
  newRuleValueLabel.setAttribute("for", "rule" + ruleInputCount + "Key")
  newRuleValueLabel.innerText = "Rule " + ruleInputCount + " Value: "

  let newRuleValueInput = document.createElement("input")
  newRuleValueInput.id = "rule" + ruleInputCount + "Value"
  newRuleValueInput.type = "text"
  newRuleValueInput.name = "rule" + ruleInputCount 

  lastRuleInput.insertAdjacentElement("afterend", newRuleKeyLabel)
  lastRuleInput = lastRuleInput.nextElementSibling
  lastRuleInput.insertAdjacentElement("afterend", newRuleKeyInput)
  lastRuleInput = lastRuleInput.nextElementSibling
  lastRuleInput.insertAdjacentElement("afterend", newRuleValueLabel)
  lastRuleInput = lastRuleInput.nextElementSibling
  lastRuleInput.insertAdjacentElement("afterend", newRuleValueInput)
  lastRuleInput = lastRuleInput.nextElementSibling
}

function removeRuleInput() {
  if (ruleInputCount > 1) {
    for (let i = 0; i < 4; i++) {
      let elementToRemove = lastRuleInput
      lastRuleInput = lastRuleInput.previousElementSibling
      elementToRemove.remove()
    }
    ruleInputCount--
  }
}