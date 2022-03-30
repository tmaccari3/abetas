$(document).ready(function() {
    var programs = document.getElementsByName("programs");	
    
    for(var i = 0; i < programs.length; i++) {
        disableChange(programs[i]);
    }
});

function addProgram(id, inputId) {
    var progInput = document.createElement("input");
    progInput.type = "hidden";

    var checkbox = document.getElementById(inputId);
    if (checkbox.checked) {
        progInput.name = "addProgram"
    } else {
        progInput.name = "removeProgram"
    }
    progInput.value = id;

    var form = document.getElementById("task");
    form.appendChild(progInput);
    form.submit();
}

function disableChange(input) {
    var clicked = document.getElementById(input.id);
    var outcomes = document.getElementsByName("outcome");	
    var checkedOutcomes = document.getElementsByName("outcomes");	
    
    if(clicked.checked) {
        for(var i = 0; i < outcomes.length; i++) {
            if(outcomes[i].parentNode.getAttribute("name") == clicked.id) {
                outcomes[i].style.display = 'block';
            }
        }
    }
    
    else {
        for(var i = 0; i < outcomes.length; i++) {
            if(outcomes[i].parentNode.getAttribute("name") == clicked.id) {
				checkedOutcomes[i].checked = false;
                outcomes[i].style.display = 'none';
            }
        }
    }
}

/*function disableChange(input) {
    var clicked = document.getElementById(input.id);
    var outcomes = document.getElementsByName("outcomes");	
    
    
    if(clicked.checked) {
        for(var i = 0; i < outcomes.length; i++) {
            if(outcomes[i].parentNode.getAttribute("name") == clicked.id) {
                outcomes[i].disabled = false;
                //outcomes[i].style.display = 'block';
            }
        }
    }
    
    else {
        for(var i = 0; i < outcomes.length; i++) {
            if(outcomes[i].parentNode.getAttribute("name") == clicked.id) {
                outcomes[i].disabled = true;
                outcomes[i].checked = false;
                //outcomes[i].style.display = 'none';
            }
        }
    }
}*/