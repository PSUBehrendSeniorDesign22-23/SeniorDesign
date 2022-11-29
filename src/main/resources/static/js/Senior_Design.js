function toggleAddView(clicked_id) {
    // get the clock
    var playerAddField = document.getElementById('playerAdd');
    var tournamentAddField = document.getElementById('tournamentAdd');
    var rulesetAddField = document.getElementById('rulesetAdd');
    
    var activeField;

		switch(clicked_id) {
    	case "playerAddButton":
      	activeField = playerAddField
    		tournamentAddField.style.display = 'none';
    		rulesetAddField.style.display = 'none';
        break;
     	case "tournamentAddButton":
      	activeField = tournamentAddField
        playerAddField.style.display = 'none';
    		rulesetAddField.style.display = 'none';
        break;
      case "rulesetAddButton":
      	activeField = rulesetAddField
        playerAddField.style.display = 'none';
    		tournamentAddField.style.display = 'none';
        break;
    }
    
    document.getElementById('playerAddButton').style.opacity = "0.6";
    document.getElementById('tournamentAddButton').style.opacity = "0.6";
    document.getElementById('rulesetAddButton').style.opacity = "0.6";
    
    var activeButton = document.getElementById(clicked_id);

    if (activeField.style.display == 'block') {
      //View is visible, so hide it.
      activeField.style.display = 'none';
      //Change button opacity
      activeButton.style.opacity = "0.6";
    }
    else {
      //View is hidden, so show it.
      activeField.style.display = 'block';
      //Change button opacity
      activeButton.style.opacity = "1";
    }
  }