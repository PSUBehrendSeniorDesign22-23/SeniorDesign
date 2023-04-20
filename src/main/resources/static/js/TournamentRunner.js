var p1Wins = 0;
var p2Wins = 0;

function roundWinChange(player, change)
{
	if (player == 1)
  {
  	if (change == "plus")
    {
    	p1Wins = p1Wins + 1;
    }
    if (change == "minus")
    {
    	p1Wins = p1Wins - 1;
    }
  	document.getElementById("p1Score").innerHTML = p1Wins;
  }
  if (player == 2)
  {
  	if (change == "plus")
    {
    	p2Wins = p2Wins + 1;
    }
    if (change == "minus")
    {
    	p2Wins = p2Wins - 1;
    }
  	document.getElementById("p2Score").innerHTML = p2Wins;
  }
}
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

let stoneConflictFlag = false // Used to indicate and force resolution of stone count
let tournamentCompleteFlag = false

// DOM variables
let matchNumber = document.getElementById("matchNum")

let ruleInfo = document.getElementById("ruleInfo")

let challengerName = document.getElementById("challengerName")
let defenderName = document.getElementById("defenderName")

let challengerRecord = document.getElementById("challengerRecord")
let defenderRecord = document.getElementById("defenderRecord")

let p1Winner = document.getElementById("p1Label")
let p2Winner = document.getElementById("p2Label")

function defenderRoundWin() {
    if (stoneConflictFlag) {
        alertStoneConflict()
        return
    }
    activeMatch.defenderScore++
    // Decrement stones
    defender.stones--
    challenger.stones--
    // Award chip for winning throw
    defender.chips++   
    
    if (isMatchComplete()) {
        completeMatches.push(activeMatch)
        rotatePlayers()
        startNextMatch()
        if (tournamentCompleteFlag) {
            finalizeTournament()
        }
    }
    updateDisplay()
}

function challengerRoundWin() {
    if (stoneConflictFlag) {
        alertStoneConflict()
        return
    }
    activeMatch.challengerScore++
    // Decrement stones
    defender.stones--
    challenger.stones--
    // Award chip for winning throw
    challenger.chips++   

    if (isMatchComplete()) {
        completeMatches.push(activeMatch)
        rotatePlayers()
        startNextMatch()
        if (tournamentCompleteFlag) {
            finalizeTournament()
        }
    }
    updateDisplay()
}

function isMatchComplete() {
    let maxRoundWins = Math.ceil(rules.BestOf / 2)
    if (activeMatch.defenderScore >= maxRoundWins || activeMatch.challengerScore >= maxRoundWins) {
        return true
    }

    if (defender.stones == 0 || challenger.stones == 0) { // At least one player out of stones, and the match is incomplete
        stoneConflictFlag = true
    }

    return false
}

function rotatePlayers() {
    let defenderKnockoutFlag = false
    let challengerKnockoutFlag = false

    // Check for challenger knockout
    if (challenger.stones == 0 && challenger.chips == 0) {
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
    playerOrder.push(playerHolder)
}

function startNextMatch() {
    // Check for end of tournament
    if (playerOrder.length < 2) {
        tournamentCompleteFlag = true
        return
    }

    // Grab defender and challenger
    defender = playerOrder.shift()
    challenger = playerOrder.shift()
    
    // Duplicate match checks should generally go here

    // Create a new match
    activeMatch = createMatch()
}

function finalizeTournament() {
    // Create all matches in database
    // Redirect to a summary page
    // Summary page
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
        console.log("invalid")
        window.location='/CoordinatorTools'
    }
    
    tournament = tournamentOptions[value]
    document.getElementById("idSelect").style.display = "none"

    players = tournament.players
    ruleset = tournament.ruleset
    rules = ruleset.rules
    playerOrder = generatePlayerOrder()

    // Append runtime attributes to each player
    for (let player in playerOrder) {
        player.stones = tournamentInfo.rules.StartingStones
        player.chips = 0
    }

    // Set initial defender and challenger
    // Pull first player as defender
    //defender = playerOrder.shift()
    challenger = "Ypu"
    // Pull second player as challenger
    //challenger = playerOrder.shift()
    defender = "Me"

    // Create first match
    activeMatch = createMatch()
    // Initialize display to user
    initializeDisplay()

    // Get all required information from database
    /*retrieveTournamentInformation().then((tournamentInfo) => {
            tournament = tournamentInfo.tournament
            players = tournamentInfo.players
            ruleset = tournamentInfo.ruleset
            rules = tournamentInfo.rules
            playerOrder = generatePlayerOrder() // List of players
            
            // Append runtime attributes to each player
            for (let player in playerOrder) {
                player.stones = tournamentInfo.rules.StartingStones
                player.chips = 0
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
    })*/
}

function initializeDisplay() {
    // Reconstruct DOM for running tournament
    matchNumber.innerText = tournament.name + " - Match " + (completeMatches.length + 1);
    
    defenderName.innerText = defender
    challengerName.innerText = challenger

    for (let rule in rules) {
        let newRuleRow = document.createElement("tr")

        let newRuleName = document.createElement("td")
        newRuleName.innerText = rules[rule].name

        let newRuleAttribute = document.createElement("td")
        newRuleAttribute.innerText = rules[rule].attribute

        newRuleRow.appendChild(newRuleName)
        newRuleRow.appendChild(newRuleAttribute)
        
        ruleInfo.appendChild(newRuleRow)
    }

    challengerRecord.innerText = challenger + " Match Record"
    defenderRecord.innerText = defender + " Match Record"

    p1Winner.innerText = challenger
    p2Winner.innerText = defender
}

function updateDisplay() {
    // Update DOM to reflect tournament state
}

function alertStoneConflict() {
    // Display an alert to the user
    // Prompt for resolution by user 

    // Will have to determine options based on:
    // who is out of stones
    // if chips are available for conversion
    // what the current match score is
    // rules from the ruleset
    // etc.

    // THIS FUNCTION IS FOR CONSTRUCTING OPTIONS TO DISPLAY TO THE USER ONLY
}

function resolveStoneConflict() {
    // Resolve based on selection from alert
    // This function can do anything, as long as it resolves conflict.
    // It also could be broken into multiple functions for different resolutions
    // It may edit stone counts, perform chip conversions, edit and finalize a match, etc.
}

function convertChips(player) {
    let chipsPerStone = rules.ChipsPerStone
    if (player.chips >= chipsPerStone) {
        player.chips -= chipsPerStone
        player.stones++
    }
    else {
        alertNotEnoughChips()
    }
}

function alertNotEnoughChips() {
    // Visual alert when there are not enough chips to convert to a stone
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
            let newRuleKeyLabel = document.createElement("option")
            newRuleKeyLabel.setAttribute("value", i)
            newRuleKeyLabel.innerText = data[i].name
            document.getElementById("tournaments").appendChild(newRuleKeyLabel)

            tournamentOptions.push(data[i])
        }
      }
    })
}