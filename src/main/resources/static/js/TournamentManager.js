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

// Base variables
let tournament
let players
let ruleset
let rules

// Generated
let matches = []
let activeMatch

// Runtime variables
let playerOrder = []
let knockoutList = []
let defender
let challenger

function rotateTurnOrder() {

}

function createMatch() {
    
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
    // TODO: Pull tournament selection from user

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
            let defender = playerOrder.shift()
            // Pull second player as challenger
            let challenger = playerOrder.shift()

            // TODO: Render initial tournament state to user

            
            do {

                // EDGE CASE: If defender has already faced challenger
                let duplicateMatch = false

                // Iterate through matches and check if the upcoming players already had a match
                for (match in matches) {
                    if (match.defender == defender && match.challenger == challenger) {
                        duplicateMatch = true
                    }
                    if (match.challenger == defender && match.defender == challenger) {
                        duplicateMatch = true
                    }
                }

                // When the match is a duplicate
                if (duplicateMatch) {

                }

                // Run a match, returns a completed match object
                let match = createMatch(defender, challenger, tournamentInfo.rules)
                matches.append(match)

                // Update overall tournament status

                // Check for challenger knockout
                if (challenger.stones == 0 && challenger.chips == 0) {
                    knockoutList.append(challenger)
                    // Do not return challenger to turn rotation
                }
                else {
                    // Return challenger to turn order
                    playerOrder.unshift(challenger)
                }
                
                // Check for defender knockout
                if (defender.stones == 0 && defender.chips == 0) {
                    knockoutList.append(defender)
                    // Do not return defender to turn rotation
                }
                else {
                    // Return defender to turn order
                    playerOrder.unshift(defender)
                }

                // Turn order should now be ready for next match

                // TODO: Update UI

            } while(playerOrder.length > 1) // Do until 1 or fewer players remain to run more matches
    })
}
