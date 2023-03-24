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