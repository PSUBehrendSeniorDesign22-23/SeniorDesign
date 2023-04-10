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
        finalizeMatch()
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
        finalizeMatch()
    }
}

function isMatchComplete() {
    let maxRoundWins = Math.ceil(rules.BestOf)
    if (activeMatch.defenderScore >= maxRoundWins || activeMatch.challengerScore >= maxRoundWins) {
        return true
    }

    if (defender.stones == 0 || challenger.stones == 0) { // At least one player out of stones, and the match is incomplete
        stoneConflictFlag = true
    }

    return false
}

function finalizeMatch() {

}

function startNextMatch() {

}

function rotatePlayers() {

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
    
}

async function retrieveTournamentInformation() {
    // Get tournament
    const tournamentSearchParams = new URLSearchParams()
    tournamentSearchParams.append("searchType", searchType)
    tournamentSearchParams.append("searchFilter", searchFilter)
    const tournamentRequest = new Request("/tournament/search?" + tournamentSearchParams.toString())
    const tournament = await fetch(tournamentRequest).json()
    
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
    // TODO: Get tournament selection from user

    // Get all required information from database
    retrieveTournamentInformation().then((tournamentInfo) => {
            tournament = tournamentInfo.tournament
            players = tournamentInfo.players
            ruleset = tournamentInfo.ruleset
            rules = tournamentInfo.rules
            playerOrder = generatePlayerOrder(tournamentInfo.players) // List of players
            
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
            
            // do {

            //     // EDGE CASE: If defender has already faced challenger
            //     let duplicateMatch = false

            //     // Iterate through matches and check if the upcoming players already had a match
            //     for (match in matches) {
            //         if (match.defender == defender && match.challenger == challenger) {
            //             duplicateMatch = true
            //         }
            //         if (match.challenger == defender && match.defender == challenger) {
            //             duplicateMatch = true
            //         }
            //     }

            //     // When the match is a duplicate
            //     if (duplicateMatch) {

            //     }

            //     // Run a match, returns a completed match object
            //     let match = createMatch(defender, challenger, tournamentInfo.rules)
            //     matches.append(match)

            //     // Update overall tournament status

            //     // Check for challenger knockout
            //     if (challenger.stones == 0 && challenger.chips == 0) {
            //         knockoutList.append(challenger)
            //         // Do not return challenger to turn rotation
            //     }
            //     else {
            //         // Return challenger to turn order
            //         playerOrder.unshift(challenger)
            //     }
                
            //     // Check for defender knockout
            //     if (defender.stones == 0 && defender.chips == 0) {
            //         knockoutList.append(defender)
            //         // Do not return defender to turn rotation
            //     }
            //     else {
            //         // Return defender to turn order
            //         playerOrder.unshift(defender)
            //     }

            //     // Turn order should now be ready for next match

            //     // TODO: Update UI

            // } while(playerOrder.length > 1) // Do until 1 or fewer players remain to run more matches
    })
}

function initializeDisplay() {
    // Reconstruct DOM for running tournament
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
    // rules from the ruleset (e.g. is conversion from 1 chip allowed?)
    // etc.

    // THIS FUNCTION IS FOR CONSTRUCTING OPTIONS TO DISPLAY TO THE USER ONLY
}

function resolveStoneConflict() {
    // Resolve based on selection from alert
    // This function can do anything, as long as it resolves conflict.
    // It also could be broken into multiple functions for different resolutions
    // It may edit stone counts, perform chip conversions, edit and finalize a match, etc.
}