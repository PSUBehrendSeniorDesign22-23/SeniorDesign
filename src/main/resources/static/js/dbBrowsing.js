document.getElementById("rulesetAddForm").addEventListener("submit", handleForm)
document.getElementById("tournamentAddForm").addEventListener("submit", handleForm)
document.getElementById("playerAddForm").addEventListener("submit", handleForm)
document.getElementById("playerEditForm").addEventListener("submit", handleForm)
document.getElementById("tournamentEditForm").addEventListener("submit", handleForm)
document.getElementById("rulesetEditForm").addEventListener("submit", handleForm)

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

  showSearchResults()

  const request = new Request("/player/search?" + searchParams.toString())

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

    showSearchResults()

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

  showSearchResults()

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

  fetch("/player/create", {
      method: "POST",
      body:   formData
  })
  .then(res => res.json()).then(data => {
    //var para = document.createElement('p')
    //var addDiv = document.getElementById("playerAdd")

    let message = "Success"
    let operationResult = data["operation"]

    if (operationResult == "failure") {
      message = "Failure: " + data["message"]
    }

    //para.innerText = message

    //addDiv.appendChild(para)
    showSnackbar(message)
  })
}

function addTournament() {

  let tournamentAttributes = {
    "tournamentName": document.getElementById("addtname").value,
    "tournamentLocation": document.getElementById("addtloc").value,
    "tournamentDate": document.getElementById("addtdate").value,
    "tournamentRulesetName": document.getElementById("addtrule").value
  }

  for (let i = 1; i <= playerCount; i++) {
    let playerKey = "player" + i + "Select";
    let playerId = document.getElementById(playerKey).value
    tournamentAttributes[playerKey] = playerId
  }

  fetch("/tournament/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body:  JSON.stringify(tournamentAttributes)
  })
  .then(res => res.json()).then(data => {
    //var para = document.createElement('p')
    //var addDiv = document.getElementById("tournamentAdd")

    //para.innerText = data["operation"]

    //addDiv.appendChild(para)
    showSnackbar(data["operation"])
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
    //var para = document.createElement('p')
    //var addDiv = document.getElementById("rulesetAdd")

    //para.innerText = data["operation"]

    //addDiv.appendChild(para)
    showSnackbar(data["operation"])
  })
}

function editPlayer() {

}

function editRuleset() {

}

function editTournament() {

}

// These functions are for adding or removing rule inputs
let ruleInputCount = 1
let lastRuleInput = document.getElementById("rule1Value")
function addRuleInput() {
  ruleInputCount++

  let newRuleKeyLabel = document.createElement("label")
  newRuleKeyLabel.setAttribute("for", "rule" + ruleInputCount + "Key")
  newRuleKeyLabel.innerText = "Rule " + ruleInputCount + " Name:"
  newRuleKeyLabel.appendChild( document.createTextNode( '\u00A0' ) );

  let newRuleKeyInput = document.createElement("input")
  newRuleKeyInput.id = "rule" + ruleInputCount + "Key"
  newRuleKeyInput.type = "text"
  newRuleKeyInput.name = "rule" + ruleInputCount 

  let newRuleValueLabel = document.createElement("label")
  newRuleValueLabel.setAttribute("for", "rule" + ruleInputCount + "Key")
  newRuleValueLabel.innerText = "Rule " + ruleInputCount + " Value:"
  newRuleValueLabel.appendChild( document.createTextNode( '\u00A0' ) );

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

function showSnackbar(text) {
  // Get the snackbar DIV
  var x = document.getElementById("snackbar");

  // Add the "show" class to DIV
  x.className = "show";
  x.innerText = text;

  // After 3 seconds, remove the show class from DIV
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}

let playerOptions = []
let playerCount = 2
let lastPlayerInput = document.getElementById("player2Select")
function addPlayerSelector() {
  playerCount++

  let nameTag = "player" + playerCount + "Select"
  // Create label
  let playerLabel = document.createElement("label")
  playerLabel.setAttribute("for", nameTag)
  playerLabel.innerText = "Player " + playerCount + ": "
  // Create selector
  let playerSelect = document.createElement("select")
  playerSelect.setAttribute("name", nameTag)
  playerSelect.setAttribute("id", nameTag)
  // populate options
  populatePlayerOptions(playerSelect)
  // Add to DOM
  lastPlayerInput.insertAdjacentElement("afterend", playerLabel)
  lastPlayerInput = lastPlayerInput.nextElementSibling
  lastPlayerInput.insertAdjacentElement("afterend", playerSelect)
  lastPlayerInput = lastPlayerInput.nextElementSibling
}

function removePlayerSelector() {
  if (playerCount > 2) {
    for (let i = 0; i < 2; i++) {
      let elementToRemove = lastPlayerInput
      lastPlayerInput = lastPlayerInput.previousElementSibling
      elementToRemove.remove()
    }
    playerCount--
  }
}

function loadPlayerSelections()
{
  const searchParams = new URLSearchParams();
  
  searchParams.append("searchType", "all")
  searchParams.append("searchFilter", "")

  const request = new Request("/player/search?" + searchParams.toString())

  fetch(request).then((response) => response.json())
    .then((data) => {
      playerOptions = data
      let player1Select = document.getElementById("player1Select")
      let player2Select = document.getElementById("player2Select")
      populatePlayerOptions(player1Select)
      populatePlayerOptions(player2Select)
    })
}

function populatePlayerOptions(playerSelector) {
  let emptyOption = document.createElement("option")
  emptyOption.setAttribute("value", "none")
  emptyOption.innerText = "--"
  playerSelector.appendChild(emptyOption)

  for (var i = 0; i < playerOptions.length; i++) {
    if (playerOptions[i] != null)
    {
        let playerOption = document.createElement("option")
        playerOption.setAttribute("value", playerOptions[i].playerId)
        playerOption.innerText = playerOptions[i].skipperName
        playerSelector.appendChild(playerOption);
    }
  }
}