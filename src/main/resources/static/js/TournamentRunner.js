/* 
Steps to run a tournament

1. Create tournament (or select precreated tournament that has not been completed)
2. Add (and/or review) players
3. Generate an initial match order
4. Initialize frontend display for tournament start
5. Begin running matches
6. Run matches until all players are out of stones (dependent on ruleset)
7. Display tournament summary for review
8. Store all results into the database

*/

// Tournament information
let tournamentOptions = []
let tournament
let players
let ruleset
let rules

// Generated
let completeMatches = []
let activeMatch

// Runtime variables
let playerOrder = []
let knockoutList = []
let defender
let challenger

// DOM variables
let matchNumber = document.getElementById("matchNum")

let ruleInfo = document.getElementById("ruleInfo")

let challengerName = document.getElementById("challengerName")
let defenderName = document.getElementById("defenderName")
let challengerRatio = document.getElementById("challengerRatio")
let defenderRatio = document.getElementById("defenderRatio")
let challengerStones = document.getElementById("challengerStones")
let defenderStones = document.getElementById("defenderStones")
let challengerChips = document.getElementById("challengerChips")
let defenderChips = document.getElementById("defenderChips")

let challengerRecord = document.getElementById("challengerRecord")
let challengerRoundScore = document.getElementById("p1Score")
let defenderRecord = document.getElementById("defenderRecord")
let defenderRoundScore = document.getElementById("p2Score")

function defenderRoundWin() {
    activeMatch.defenderScore++
    // Decrement stones
    defender.stones--
    challenger.stones--
    // Award chip for winning throw
    defender.chips++

    processRound()
    updateDisplay()
}

function challengerRoundWin() {
    activeMatch.challengerScore++
    // Decrement stones
    defender.stones--
    challenger.stones--
    // Award chip for winning throw
    challenger.chips++   

    processRound()
    updateDisplay()
}

function processRound() {
    if (isMatchComplete()) {
        establishWinner()
        completeMatches.push(activeMatch)
        rotatePlayers()
        if (playerOrder.length < 2) {
            finalizeTournament()
        }
        else {
            prepareNextMatch()
        }
        updateDisplay()
    }
}

function isMatchComplete() {
    if(checkStoneCount()) {
        return true
    }

    let maxRoundWins = Math.ceil(rules.BestOf / 2)
    if (activeMatch.defenderScore >= maxRoundWins || activeMatch.challengerScore >= maxRoundWins) {
        return true
    }

    return false
}

function establishWinner() {
    if (activeMatch.defenderScore > activeMatch.challengerScore) {
        defender.matchWins++
        challenger.matchLosses++
    }
    else if (activeMatch.defenderScore < activeMatch.challengerScore) {
        defender.matchLosses++
        challenger.matchWins++
    }
    // Ties are not tracked
}

function rotatePlayers() {
    let defenderKnockoutFlag = false
    let challengerKnockoutFlag = false

    // Check for challenger knockout
    if (challenger.stones == 0 && challenger.chips < rules.ChipsPerStone) {
        knockoutList.push(challenger)
        challengerKnockoutFlag = true
    }
    else {
        // Return challenger to turn order
        playerOrder.unshift(challenger)
    }
    // Check for defender knockout
    if (defender.stones == 0 && defender.chips == 0) {
        knockoutList.push(defender)
        defenderKnockoutFlag = true
    }
    else {
        // Return defender to turn order
        playerOrder.unshift(defender)
    }

    if (defenderKnockoutFlag != challengerKnockoutFlag) { // One and only one player was knocked out (XOR)
        // Turn order should already be correct
        return
    }

    if (defenderKnockoutFlag == challengerKnockoutFlag && defenderKnockoutFlag == true) { // Both defender and challenger were knocked out
        // Turn order should already be correct
        return
    }

    // Neither player was knocked out
    // Rotate based on match winner
    let playerHolder
    if (activeMatch.defenderScore < activeMatch.challengerScore) { // If challenger wins
        // Shift defender out of list
        playerHolder = playerOrder.shift()
    }
    else { // defender wins
        // Remove challenger from list
        playerHolder = playerOrder.splice(1,1)
    }
    if (playerHolder.length == undefined)
    {
        playerOrder.push(playerHolder)
    }
    else
    {
        playerOrder.push(playerHolder[0])
    }
    
}

function prepareNextMatch() {
    // Grab next defender and next challenger
    defender = playerOrder.shift()
    challenger = playerOrder.shift()
    
    // Duplicate match checks should generally go here

    // Create a new match
    activeMatch = createMatch()
}

function finalizeTournament() {
    // Create all matches in database
    for (let i = 0; i < completeMatches.length; i++) {
        saveMatchToDatabase(completeMatches[i])
    }
    //Show a summary modal
    let summary = document.getElementById("idSummary")
    let summaryTable = document.getElementById("idSummaryTable")

    let tableHeaders = document.createElement("tr")

    let header = document.createElement("td")
    header.setAttribute("class", "primaryTextCenter")
    header.setAttribute("style", "text-decoration: underline;")
    header.innerText = "Skipper Name"
    tableHeaders.appendChild(header)

    header = document.createElement("td")
    header.setAttribute("class", "primaryTextCenter")
    header.setAttribute("style", "text-decoration: underline;")
    header.innerText = "Tournament W-L Record"
    tableHeaders.appendChild(header)

    summaryTable.appendChild(tableHeaders)

    for (var x = 0; x < tournament.players.length; x++)
    {
        let newSummaryRow = document.createElement("tr")

        let SkipperName = document.createElement("td")
        SkipperName.innerText = tournament.players[x][0].skipperName
        newSummaryRow.appendChild(SkipperName)

        let SkipperRecord = document.createElement("td")
        SkipperRecord.innerText = tournament.players[x][0].matchWins + " - " + tournament.players[x][0].matchLosses
        newSummaryRow.appendChild(SkipperRecord)

        summaryTable.appendChild(newSummaryRow)
    }

    summary.setAttribute("style", "display:block;")
}

function createMatch() {
    return {
        defender : defender,
        challenger : challenger,
        defenderScore : 0,
        challengerScore : 0,
        tournament : tournament
    }
}

function saveMatchToDatabase(match) {
    
    matchAttributes = {
        "defenderId": match.defender.playerId,
        "challengerId": match.challenger.playerId,
        "tournamentId": match.tournament.tournamentId,
        "defenderScore": match.defenderScore,
        "challengerScore": match.challengerScore
    }

    fetch("/match/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(matchAttributes)
    })
    .then((response) => response.json())
}

function generatePlayerOrder() {
    /* 
    Decrementing condition ensures that the range of possible indices for the selected player
    decreases by 1 with each iteration
    Moving the selected player to the end of the general list then removes the possibility of
    the same player being selected twice, and the order of the general list does not matter.
    */
    let playerCount = players.length
    for (let unplacedPlayers = playerCount; unplacedPlayers > 0; unplacedPlayers--) {
        selectedPlayerIndex = Math.floor(Math.random() * unplacedPlayers) // returns a random integer from 0 to unplacedPlayers - 1
        playerOrder.push(players[selectedPlayerIndex]) // Add selected player into the player turn order
        // Move placed player to the end of the general player list
        let movingPlayer = players.splice(selectedPlayerIndex, 1)
        players.push(movingPlayer)
    }
}

async function retrieveTournamentInformation() {
    // Get tournament
    //const tournamentSearchParams = new URLSearchParams()
    //tournamentSearchParams.append("searchType", searchType)
    //tournamentSearchParams.append("searchFilter", searchFilter)
    //const tournamentRequest = new Request("/tournament/search?" + tournamentSearchParams.toString())
    //const tournament = await fetch(tournamentRequest).json()
    //const tournament = 
    
    // Get players associated with tournament
    const playerSearchParams = new URLSearchParams()
    playerSearchParams.append("searchType", "tournamentId")
    playerSearchParams.append("searchFilter", tournament.id)
    const playerRequest = new Request("/player/search?" + playerSearchParams.toString())
    const players = fetch(playerRequest).json()

    // Get ruleset from tournament
    const rulesetSearchParams = new URLSearchParams()
    rulesetSearchParams.append("searchType", "id")
    rulesetSearchParams.append("searchFilter", tournament.ruleset.id)
    const rulesetRequest = new Request("/ruleset/search?" + rulesetSearchParams.toString())
    const ruleset = await fetch(rulesetRequest).json()

    // Get rules from ruleset
    const rulesSearchParams = new URLSearchParams()
    rulesSearchParams.append("searchType", "rulesetId")
    rulesSearchParams.append("searchType", ruleset.id)
    const rulesSearchRequest = new Request("/rule/search?" + rulesSearchParams.toString())
    const rules = fetch(rulesSearchRequest).json()

    // Encapsulate data
    return {tournament: tournament, players: await players, ruleset: ruleset, rules: await rules}
}

function initializeTournament() {
    // Get tournament selection from user
    var tournamentSelect = document.getElementById("tournaments")
	var value = tournamentSelect.value
	if (value == "none")
    {
        window.location='/CoordinatorTools'
    }
    
    tournament = tournamentOptions[value]
    document.getElementById("idSelect").style.display = "none"

    // Get all required information from database
    players = tournament.players
    ruleset = tournament.ruleset
    rules = ruleset.rules

    rules.BestOf = 3
    rules.StartingStones = 3
    rules.ChipsPerStone = 2

    for (let rule in rules) {
        if (rules[rule].name == "BestOf")
        {
            rules.BestOf = rules[rule].attribute
        }
        if (rules[rule].name == "StartingStones")
        {
            rules.StartingStones = rules[rule].attribute
        }
        if (rules[rule].name == "ChipsPerStone")
        {
            rules.ChipsPerStone = rules[rule].attribute
        }
    }

    generatePlayerOrder()

    // Append runtime attributes to each player
    for (let player in playerOrder) {
        playerOrder[player].stones = rules.StartingStones
        playerOrder[player].chips = 0
        playerOrder[player].matchWins = 0
        playerOrder[player].matchLosses = 0
    }

    // Set initial defender and challenger
    // Pull first player as defender
    defender = playerOrder.shift()
    // Pull second player as challenger
    challenger = playerOrder.shift()

    // Create first match
    activeMatch = createMatch()
    // Initialize display to user
    initializeDisplay()
}

function initializeDisplay() {
    // Reconstruct DOM for running tournament
    matchNumber.innerText = tournament.name + " - Match " + (completeMatches.length + 1);
    
    defenderName.innerText = defender.skipperName
    challengerName.innerText = challenger.skipperName

    defenderRatio.innerText = defender.matchWins + " - " + defender.matchLosses
    challengerRatio.innerText = challenger.matchWins + " - " + challenger.matchLosses

    defenderStones.innerText = defender.stones
    challengerStones.innerText = challenger.stones

    defenderChips.innerText = defender.chips
    challengerChips.innerText = challenger.chips

    for (let rule in rules) {
        if (rules[rule].name == undefined)
        {
            continue
        }

        let newRuleRow = document.createElement("tr")

        let newRuleName = document.createElement("td")
        newRuleName.innerText = rules[rule].name

        let newRuleAttribute = document.createElement("td")
        newRuleAttribute.innerText = rules[rule].attribute

        newRuleRow.appendChild(newRuleName)
        newRuleRow.appendChild(newRuleAttribute)
        
        ruleInfo.appendChild(newRuleRow)
    }

    challengerRecord.innerText = challenger.skipperName + " Round Record"
    challengerRoundScore.innerText = activeMatch.challengerScore
    defenderRecord.innerText = defender.skipperName + " Round Record"
    defenderRoundScore.innerText = activeMatch.defenderScore
}

function updateDisplay() {
    // Update DOM to reflect tournament state
    matchNumber.innerText = tournament.name + " - Match " + (completeMatches.length + 1);
    
    defenderName.innerText = defender.skipperName
    challengerName.innerText = challenger.skipperName

    defenderRatio.innerText = defender.matchWins + " - " + defender.matchLosses
    challengerRatio.innerText = challenger.matchWins + " - " + challenger.matchLosses

    defenderStones.innerText = defender.stones
    challengerStones.innerText = challenger.stones

    defenderChips.innerText = defender.chips
    challengerChips.innerText = challenger.chips

    challengerRecord.innerText = challenger.skipperName + " Round Record"
    challengerRoundScore.innerText = activeMatch.challengerScore
    defenderRecord.innerText = defender.skipperName + " Round Record"
    defenderRoundScore.innerText = activeMatch.defenderScore
}

function checkStoneCount() {
    let matchIsOver = false

    let defenderKnockout = false
    let challengerKnockout = false

    if (defender.stones == 0 && defender.chips < rules.ChipsPerStone) { // defender is knocked out
        defenderKnockout = true
        matchIsOver = true
    }
    else if (challenger.stones == 0 && challenger.chips < rules.ChipsPerStone) { // challenger is knocked out
        challengerKnockout = true
        matchIsOver = true
    }

    // Chip Conversions
    if (!defenderKnockout && defender.stones == 0) { // defender in need of chip conversion
        defender.stones++
        defender.chips -= rules.ChipsPerStone
    }

    if (!challengerKnockout && challenger.stones == 0) { // challenger in need of chip conversion
        challenger.stones++
        challenger.chips -= rules.ChipsPerStone
    }

    return matchIsOver
}

function preload()
{
  const searchParams = new URLSearchParams();
  
  searchParams.append("searchType", "all")
  searchParams.append("searchFilter", "")

  const request = new Request("/tournament/search?" + searchParams.toString())

  fetch(request).then((response) => response.json())
    .then((data) => {
      for (var i = 0; i < data.length; i++) {
        if (data[i] != null)
        {
            let newTournamentOption = document.createElement("option")
            newTournamentOption.setAttribute("value", i)
            newTournamentOption.innerText = data[i].name
            document.getElementById("tournaments").appendChild(newTournamentOption)

            tournamentOptions.push(data[i])
        }
      }
    })
}

function verifyScore(caller)
{
    if(caller == "challenger")
    {
        if (confirm("Please confirm that the challenger (" + challenger.skipperName + ") won this round!\nPress OK or hit enter to confirm.\nPress Cancel or hit Esc to cancel.") == true) {
            challengerRoundWin()
        }
        else
        {
            return
        }
    }

    else if(caller == "defender")
    {
        if (confirm("Please confirm that the defender (" + defender.skipperName + ") won this round!\nPress OK or hit enter to confirm.\nPress Cancel or hit Esc to cancel.") == true) {
            defenderRoundWin()
        }
        else
        {
            return
        }
    }
}