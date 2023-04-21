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
      resultsContainer.innerHTML = ''

      let tableHeaders = document.createElement("tr")

      let header = document.createElement("td")
      header.setAttribute("class", "primaryTextCenter")
      header.setAttribute("style", "text-decoration: underline;")
      header.innerText = "Player First Name"
      tableHeaders.appendChild(header)

      header = document.createElement("td")
      header.setAttribute("class", "primaryTextCenter")
      header.setAttribute("style", "text-decoration: underline;")
      header.innerText = "Player Last Name"
      tableHeaders.appendChild(header)

      header = document.createElement("td")
      header.setAttribute("class", "primaryTextCenter")
      header.setAttribute("style", "text-decoration: underline;")
      header.innerText = "Skipper Name"
      tableHeaders.appendChild(header)

      resultsContainer.appendChild(tableHeaders)

      for (var i = 0; i < data.length; i++) {
        if (data[i] != null)
        {
          let newSearchRow = document.createElement("tr")

          let newPlayerfName = document.createElement("td")
          newPlayerfName.innerText = data[i].user.firstName

          let newPlayerlName = document.createElement("td")
          newPlayerlName.innerText = data[i].user.lastName

          let newPlayersName = document.createElement("td")
          newPlayersName.innerText = data[i].skipperName

          newSearchRow.appendChild(newPlayerfName)
          newSearchRow.appendChild(newPlayerlName)
          newSearchRow.appendChild(newPlayersName)
        
          resultsContainer.appendChild(newSearchRow)
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
          resultsContainer.innerHTML = ''

          let tableHeaders = document.createElement("tr")

          let header = document.createElement("td")
          header.setAttribute("class", "primaryTextCenter")
          header.setAttribute("style", "text-decoration: underline;")
          header.innerText = "Tournament Name"
          tableHeaders.appendChild(header)

          header = document.createElement("td")
          header.setAttribute("class", "primaryTextCenter")
          header.setAttribute("style", "text-decoration: underline;")
          header.innerText = "Location"
          tableHeaders.appendChild(header)

          header = document.createElement("td")
          header.setAttribute("class", "primaryTextCenter")
          header.setAttribute("style", "text-decoration: underline;")
          header.innerText = "Ruleset"
          tableHeaders.appendChild(header)

          header = document.createElement("td")
          header.setAttribute("class", "primaryTextCenter")
          header.setAttribute("style", "text-decoration: underline;")
          header.innerText = "Players"
          tableHeaders.appendChild(header)

          resultsContainer.appendChild(tableHeaders)

          for (var i = 0; i < data.length; i++) {
            if (data[i] != null)
            {
              let newSearchRow = document.createElement("tr")

              let TournamentName = document.createElement("td")
              TournamentName.innerText = data[i].name
              newSearchRow.appendChild(TournamentName)

              let TournamentLocation = document.createElement("td")
              TournamentLocation.innerText = data[i].location
              newSearchRow.appendChild(TournamentLocation)

              let TournamentRuleset = document.createElement("td")
              TournamentRuleset.innerText = data[i].ruleset.name
              newSearchRow.appendChild(TournamentRuleset)

              let newPlayer = document.createElement("td")
              if (data[i].players.length == 0)
              {
                newPlayer.innerText = data[i].player.length
              }
              else
              {
                for(let x = 0; x < data[i].name.length; x++)
                {
                  if(data[i].name[x] == "'")
                  {
                    data[i].name = setCharAt(data[i].name, x, '_')
                  }

                  if(data[i].name[x] == '"')
                  {
                    data[i].name = setCharAt(data[i].name, x, '_')
                  }
                }
                newPlayer.innerHTML = data[i].players.length + "   " + "<button id='" + data[i].name + "'" + " onclick=\"expandTable('"+ data[i].name + "')\">↓</button>"
              }

              newSearchRow.appendChild(newPlayer)
        
              resultsContainer.appendChild(newSearchRow)

              for (var j = 0; j < data[i].players.length; j++)
              {
                if (j == 0)
                {
                  let labelRow = document.createElement("tr")

                  let label = document.createElement("td")
                  label.innerText = "First Name"
                  label.setAttribute("style", "text-decoration: underline;")
                  labelRow.appendChild(label)
    
                  label = document.createElement("td")
                  label.innerText = "Last Name"
                  label.setAttribute("style", "text-decoration: underline;")
                  labelRow.appendChild(label)
    
                  label = document.createElement("td")
                  label.innerText = "Skipper Name"
                  label.setAttribute("style", "text-decoration: underline;")
                  labelRow.appendChild(label)

                  labelRow.setAttribute("id", data[i].name + "List")
                  labelRow.setAttribute("style", "display: none;")

                  resultsContainer.appendChild(labelRow)
                }

                let childPlayer = document.createElement("tr")

                let newPlayerfName = document.createElement("td")
                newPlayerfName.innerText = data[i].players[j].user.firstName

                let newPlayerlName = document.createElement("td")
                newPlayerlName.innerText = data[i].players[j].user.lastName

                let newPlayersName = document.createElement("td")
                newPlayersName.innerText = data[i].players[j].skipperName

                childPlayer.appendChild(newPlayerfName)
                childPlayer.appendChild(newPlayerlName)
                childPlayer.appendChild(newPlayersName)

                childPlayer.setAttribute("id", data[i].name + "List")
                childPlayer.setAttribute("style", "display: none;")
        
                resultsContainer.appendChild(childPlayer)
              }
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
      resultsContainer.innerHTML = ''

      let tableHeaders = document.createElement("tr")

      let header = document.createElement("td")
      header.setAttribute("class", "primaryTextCenter")
      header.setAttribute("style", "text-decoration: underline;")
      header.innerText = "Ruleset Name"
      tableHeaders.appendChild(header)

      header = document.createElement("td")
      header.setAttribute("class", "primaryTextCenter")
      header.setAttribute("style", "text-decoration: underline;")
      header.innerText = "Ruleset Origin"
      tableHeaders.appendChild(header)

      header = document.createElement("td")
      header.setAttribute("class", "primaryTextCenter")
      header.setAttribute("style", "text-decoration: underline;")
      header.innerText = "Number of Rules"
      tableHeaders.appendChild(header)

      resultsContainer.appendChild(tableHeaders)

      for (var i = 0; i < data.length; i++) {
        if (data[i] != null)
        {
          let newSearchRow = document.createElement("tr")

          let RulesetName = document.createElement("td")
          RulesetName.innerText = data[i].name

          let RulesetOrigin = document.createElement("td")
          RulesetOrigin.innerText = data[i].origin

          let newRuleAttribute = document.createElement("td")
          if (data[i].rules.length == 0)
          {
            newRuleAttribute.innerText = data[i].rules.length
          }
          else
          {
            for(let x = 0; x < data[i].name.length; x++)
            {
              if(data[i].name[x] == "'")
              {
                data[i].name = setCharAt(data[i].name, x, '_')
              }

              if(data[i].name[x] == '"')
              {
                data[i].name = setCharAt(data[i].name, x, '_')
              }
            }

            newRuleAttribute.innerHTML = data[i].rules.length + "   " + "<button id='" + data[i].name + "'" + " onclick=\"expandTable('"+ data[i].name + "')\">↓</button>"
          }

          newSearchRow.appendChild(RulesetName)
          newSearchRow.appendChild(RulesetOrigin)
          newSearchRow.appendChild(newRuleAttribute)
        
          resultsContainer.appendChild(newSearchRow)

          for (var j = 0; j < data[i].rules.length; j++)
          {
            if (j == 0)
            {
              let labelRow = document.createElement("tr")

              let label = document.createElement("td")
              label.innerText = "Rule Name"
              label.setAttribute("style", "text-decoration: underline;")
              labelRow.appendChild(label)
    
              label = document.createElement("td")
              label.innerText = "The Rule"
              label.setAttribute("style", "text-decoration: underline;")
              labelRow.appendChild(label)

              labelRow.setAttribute("id", data[i].name + "List")
              labelRow.setAttribute("style", "display: none;")

              resultsContainer.appendChild(labelRow)
            }

            let childRule = document.createElement("tr")

            let newRuleName = document.createElement("td")
            newRuleName.innerText = data[i].rules[j].name

            let newRuleAttribute = document.createElement("td")
            newRuleAttribute.innerText = data[i].rules[j].attribute

            childRule.appendChild(newRuleName)
            childRule.appendChild(newRuleAttribute)

            childRule.setAttribute("id", data[i].name + "List")
            childRule.setAttribute("style", "display: none;")
        
            resultsContainer.appendChild(childRule)
          }
        }
      }
    })
}

function setCharAt(str,index,chr) {
  if(index > str.length-1) return str;
  return str.substring(0,index) + chr + str.substring(index+1);
}

function expandTable(calledBy)
{
  let caller = document.getElementById(calledBy)
  
  let tableElements = document.querySelectorAll('[id="' + calledBy + 'List"]')

  for (var j = 0; j < tableElements.length; j++)
  {
    if (caller.innerText == "↓")
    {
      tableElements[j].setAttribute("style", "display: table-row;")
    }
    else
    {
      tableElements[j].setAttribute("style", "display: none;")
    }
  }

  if (caller.innerText == "↓")
    {
      caller.innerText = "↑"
    }
    else
    {
      caller.innerText = "↓"
    }
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
    const searchParams = new URLSearchParams()
    searchParams.append("searchType", "all")
    searchParams.append("searchFilter", "")
    loadPlayerSelections(searchParams)
  })
}

function addTournament() {

  let tournamentAttributes = {
    "tournamentName": document.getElementById("addtname").value,
    "tournamentLocation": document.getElementById("addtloc").value,
    "tournamentDate": document.getElementById("addtdate").value,
    "tournamentRulesetId": document.getElementById("addtrule").value
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
    const searchParams = new URLSearchParams()
    searchParams.append("searchType", "all")
    searchParams.append("searchFilter", "")
    loadTournamentSelections(searchParams)
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
    const searchParams = new URLSearchParams()
    searchParams.append("searchType", "all")
    searchParams.append("searchFilter", "")
    loadRulesetSelections(searchParams)
  })
}

function editPlayer() {
  let playerAttributes = {
    "playerId": document.getElementById("playerEditSelect").value,
    "skipperName": document.getElementById("editssname").value,
    "rank": document.getElementById("editrank").value
  }

  fetch("/player/update", {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(playerAttributes)
  })
  .then(res => res.json()).then(data => {
    showSnackbar(data["operation"])
    const searchParams = new URLSearchParams()
    searchParams.append("searchType", "all")
    searchParams.append("searchFilter", "")
    loadPlayerSelections(searchParams)
  })
}

function editRuleset() {
  let rulesetAttributes = {
    "rulesetId": document.getElementById("rulesetEditRulesetSelector").value,
    "rulesetName": document.getElementById("editrname").value,
    "rulesetOrigin": document.getElementById("editrorigin").value
  }

  for (let i = 1; i <= ruleEditInputCount; i++) {
    let ruleKey = document.getElementById("rule" + i + "EditKey").value
    let ruleValue = document.getElementById("rule" + i + "EditValue").value
    rulesetAttributes[ruleKey] = ruleValue
  }

  fetch("/ruleset/update", {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(rulesetAttributes)
  })
  .then(res => res.json()).then(data => {
    showSnackbar(data["operation"])
    const searchParams = new URLSearchParams()
    searchParams.append("searchType", "all")
    searchParams.append("searchFilter", "")
    loadRulesetSelections(searchParams)
  })
}

function editTournament() {

  let tournamentAttributes = {
    "tournamentId": document.getElementById("tournamentSelector").value,
    "tournamentName": document.getElementById("edittname").value,
    "tournamentLocation": document.getElementById("edittloc").value,
    "tournamentDate": document.getElementById("edittdate").value,
    "tournamentRulesetId": document.getElementById("tournamentEditRulesetSelector").value
  }

  for (let i = 1; i <= playerCount; i++) {
    let playerKey = "player" + i + "EditSelect";
    let playerId = document.getElementById(playerKey).value
    tournamentAttributes[playerKey] = playerId
  }

  fetch("/tournament/update", {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(tournamentAttributes)
  })
  .then(res => res.json()).then(data => {
    showSnackbar(data["operation"])
    const searchParams = new URLSearchParams()
    searchParams.append("searchType", "all")
    searchParams.append("searchFilter", "")
    loadTournamentSelections(searchParams)
  })
}

// These functions are for adding or removing rule inputs
let ruleInputCount = 1
let lastRuleInput = document.getElementById("rule1Value").nextElementSibling
function addRuleInput() {
  ruleInputCount++

  let newRuleKeyLabel = document.createElement("label")
  newRuleKeyLabel.setAttribute("for", "rule" + ruleInputCount + "Key")
  newRuleKeyLabel.innerHTML = "Rule " + ruleInputCount + " Name:"
  newRuleKeyLabel.appendChild( document.createTextNode( '\u00A0' ) );

  let newRuleKeyInput = document.createElement("input")
  newRuleKeyInput.id = "rule" + ruleInputCount + "Key"
  newRuleKeyInput.type = "text"
  newRuleKeyInput.name = "rule" + ruleInputCount 

  let newRuleValueLabel = document.createElement("label")
  newRuleValueLabel.setAttribute("for", "rule" + ruleInputCount + "Key")
  newRuleValueLabel.innerHTML = "Rule " + ruleInputCount + " Value:"
  newRuleValueLabel.appendChild( document.createTextNode( '\u00A0' ) );

  let newRuleValueInput = document.createElement("input")
  newRuleValueInput.id = "rule" + ruleInputCount + "Value"
  newRuleValueInput.type = "text"
  newRuleValueInput.name = "rule" + ruleInputCount

  let breakElement = document.createElement("br")

  lastRuleInput.insertAdjacentElement("afterend", newRuleKeyLabel)
  lastRuleInput = lastRuleInput.nextElementSibling
  lastRuleInput.insertAdjacentElement("afterend", newRuleKeyInput)
  lastRuleInput = lastRuleInput.nextElementSibling
  lastRuleInput.insertAdjacentElement("afterend", newRuleValueLabel)
  lastRuleInput = lastRuleInput.nextElementSibling
  lastRuleInput.insertAdjacentElement("afterend", newRuleValueInput)
  lastRuleInput = lastRuleInput.nextElementSibling
  lastRuleInput.insertAdjacentElement("afterend", breakElement)
  lastRuleInput = lastRuleInput.nextElementSibling
}

function removeRuleInput() {
  if (ruleInputCount > 1) {
    for (let i = 0; i < 5; i++) {
      let elementToRemove = lastRuleInput
      lastRuleInput = lastRuleInput.previousElementSibling
      elementToRemove.remove()
    }
    ruleInputCount--
  }
}

let ruleEditInputCount = 1
let lastRuleEditInput = document.getElementById("rule1EditValue").nextElementSibling
function addRuleEditInput() {
  ruleEditInputCount++

  let newRuleKeyLabel = document.createElement("label")
  newRuleKeyLabel.setAttribute("for", "rule" + ruleEditInputCount + "EditKey")
  newRuleKeyLabel.innerHTML = "Rule " + ruleEditInputCount + " Name:"
  newRuleKeyLabel.appendChild( document.createTextNode( '\u00A0' ) );

  let newRuleKeyInput = document.createElement("input")
  newRuleKeyInput.id = "rule" + ruleEditInputCount + "EditKey"
  newRuleKeyInput.type = "text"
  newRuleKeyInput.name = "rule" + ruleEditInputCount 

  let newRuleValueLabel = document.createElement("label")
  newRuleValueLabel.setAttribute("for", "rule" + ruleEditInputCount + "EditKey")
  newRuleValueLabel.innerHTML = "Rule " + ruleEditInputCount + " Value:"
  newRuleValueLabel.appendChild( document.createTextNode( '\u00A0' ) );

  let newRuleValueInput = document.createElement("input")
  newRuleValueInput.id = "rule" + ruleEditInputCount + "EditValue"
  newRuleValueInput.type = "text"
  newRuleValueInput.name = "rule" + ruleEditInputCount 

  let breakElement = document.createElement("br")

  lastRuleEditInput.insertAdjacentElement("afterend", newRuleKeyLabel)
  lastRuleEditInput = lastRuleEditInput.nextElementSibling
  lastRuleEditInput.insertAdjacentElement("afterend", newRuleKeyInput)
  lastRuleEditInput = lastRuleEditInput.nextElementSibling
  lastRuleEditInput.insertAdjacentElement("afterend", newRuleValueLabel)
  lastRuleEditInput = lastRuleEditInput.nextElementSibling
  lastRuleEditInput.insertAdjacentElement("afterend", newRuleValueInput)
  lastRuleEditInput = lastRuleEditInput.nextElementSibling
  lastRuleEditInput.insertAdjacentElement("afterend", breakElement)
  lastRuleEditInput = lastRuleEditInput.nextElementSibling
}

function removeRuleEditInput() {
  if (ruleEditInputCount > 1) {
    for (let i = 0; i < 5; i++) {
      let elementToRemove = lastRuleEditInput
      lastRuleEditInput = lastRuleEditInput.previousElementSibling
      elementToRemove.remove()
    }
    ruleEditInputCount--
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
let lastPlayerInput = document.getElementById("player2Select").nextElementSibling
function addPlayerSelector() {
  playerCount++

  let nameTag = "player" + playerCount + "Select"
  // Create label
  let playerLabel = document.createElement("label")
  playerLabel.setAttribute("for", nameTag)
  playerLabel.innerText = "Player " + playerCount + ": "
  playerLabel.appendChild( document.createTextNode( '\u00A0' ) );
  // Create selector
  let playerSelect = document.createElement("select")
  playerSelect.setAttribute("name", nameTag)
  playerSelect.setAttribute("id", nameTag)
  playerSelect.setAttribute("class", "playerSelector")
  // populate options
  populatePlayerOptions(playerSelect)

  let breakElement = document.createElement("br")

  // Add to DOM
  lastPlayerInput.insertAdjacentElement("afterend", playerLabel)
  lastPlayerInput = lastPlayerInput.nextElementSibling
  lastPlayerInput.insertAdjacentElement("afterend", playerSelect)
  lastPlayerInput = lastPlayerInput.nextElementSibling
  lastPlayerInput.insertAdjacentElement("afterend", breakElement)
  lastPlayerInput = lastPlayerInput.nextElementSibling
}

function removePlayerSelector() {
  if (playerCount > 2) {
    for (let i = 0; i < 3; i++) {
      let elementToRemove = lastPlayerInput
      lastPlayerInput = lastPlayerInput.previousElementSibling
      elementToRemove.remove()
    }
    playerCount--
  }
}

let playerEditCount = 2
let lastPlayerEditInput = document.getElementById("player2EditSelect").nextElementSibling
function addPlayerEditSelector() {
  playerEditCount++

  let nameTag = "player" + playerEditCount + "EditSelect"
  // Create label
  let playerLabel = document.createElement("label")
  playerLabel.setAttribute("for", nameTag)
  playerLabel.innerText = "Player " + playerEditCount + ": "
  playerLabel.appendChild( document.createTextNode( '\u00A0' ) );

  // Create selector
  let playerSelect = document.createElement("select")
  playerSelect.setAttribute("name", nameTag)
  playerSelect.setAttribute("id", nameTag)
  playerSelect.setAttribute("class", "playerSelector")
  // populate options
  populatePlayerOptions(playerSelect)

  let breakElement = document.createElement("br")
  // Add to DOM
  lastPlayerEditInput.insertAdjacentElement("afterend", playerLabel)
  lastPlayerEditInput = lastPlayerEditInput.nextElementSibling
  lastPlayerEditInput.insertAdjacentElement("afterend", playerSelect)
  lastPlayerEditInput = lastPlayerEditInput.nextElementSibling
  lastPlayerEditInput.insertAdjacentElement("afterend", breakElement)
  lastPlayerEditInput = lastPlayerEditInput.nextElementSibling
}

function removePlayerEditSelector() {
  if (playerEditCount > 2) {
    for (let i = 0; i < 3; i++) {
      let elementToRemove = lastPlayerEditInput
      lastPlayerEditInput = lastPlayerEditInput.previousElementSibling
      elementToRemove.remove()
    }
    playerEditCount--
  }
}


function populatePlayerOptions(playerSelector) {
  playerSelector.innerHTML =''
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

let rulesetOptions = []
function populateRulesetOptions(rulesetSelector) {
  rulesetSelector.innerHTML =''
  let emptyOption = document.createElement("option")
  emptyOption.setAttribute("value", "none")
  emptyOption.innerText = "--"
  rulesetSelector.appendChild(emptyOption)
  
  for (var i = 0; i < rulesetOptions.length; i++) {
    if (rulesetOptions[i] != null)
    {
      let rulesetOption = document.createElement("option")
      rulesetOption.setAttribute("value", rulesetOptions[i].rulesetId)
      rulesetOption.innerText = rulesetOptions[i].name
      rulesetSelector.appendChild(rulesetOption);
    }
  }
}

let tournamentOptions = []
function populateTournamentOptions(tournamentSelector) {
  tournamentSelector.innerHTML = ''
  let emptyOption = document.createElement("option")
  emptyOption.setAttribute("value", "none")
  emptyOption.innerText = "--"
  tournamentSelector.appendChild(emptyOption)

  for (var i = 0; i < tournamentOptions.length; i++) {
    if (tournamentOptions[i] != null)
    {
      let tournamentOption = document.createElement("option")
      tournamentOption.setAttribute("value", tournamentOptions[i].tournamentId)
      tournamentOption.innerText = tournamentOptions[i].name
      tournamentSelector.appendChild(tournamentOption);
    }
  }
}

function loadDropDownSelections()
{
  const searchParams = new URLSearchParams();
  
  searchParams.append("searchType", "all")
  searchParams.append("searchFilter", "")

  loadPlayerSelections(searchParams)
  loadRulesetSelections(searchParams)
  loadTournamentSelections(searchParams)
}

function loadPlayerSelections(searchParams) {
  const playerRequest = new Request("/player/search?" + searchParams.toString())

  fetch(playerRequest).then((response) => response.json())
    .then((data) => {
      playerOptions = data
      let playerSelectors = document.getElementsByClassName("playerSelectors")
      for (let i = 0; i < playerSelectors.length; i++) {
        populatePlayerOptions(playerSelectors[i])
      }
    })
}

function loadRulesetSelections(searchParams) {
  const rulesetRequest = new Request("/ruleset/search?" + searchParams.toString())

  fetch(rulesetRequest).then((response) => response.json())
    .then((data) => {
      rulesetOptions = data
      let rulesetSelectors = document.getElementsByClassName("rulesetSelector")
      for (let i = 0; i < rulesetSelectors.length; i++) {
          populateRulesetOptions(rulesetSelectors[i])
      }
    })
}

function loadTournamentSelections(searchParams) {
  const tournamentRequest = new Request("/tournament/search?" + searchParams.toString())
  
  fetch(tournamentRequest).then((response) => response.json())
    .then((data) => {
      tournamentOptions = data
      let tournamentSelector = document.getElementById("tournamentSelector")
      populateTournamentOptions(tournamentSelector)
    })
}