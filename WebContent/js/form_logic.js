// These are ordered in a specific order so that next--> works

var form_divs = new Array();

form_divs[0] = "form_help";
form_divs[1] = "group_career";
form_divs[2] = "group_grad";
form_divs[3] = "group_leisure";
form_divs[4] = "group_class";
form_divs[5] = "group_grades";
form_divs[6] = "group_homeworks";
form_divs[7] = "group_coursework";


function show_group(group) {
    var group_index = null;
    for(var i=0; i<form_divs.length; i++) {
        $("#" + form_divs[i]).hide();
        if (form_divs[i] === group) {
            group_index = i;
        }
        if ($("#" + "nav_" + form_divs[i] + " a")) {
            $("#" + "nav_" + form_divs[i] + " a").css("color", "");
        }
    }
    $("#" + group).show();
    if ($("#" + "nav_" + group + " a")) {
        $("#" + "nav_" + group + " a").css("color", "#ef4136");
    }
    
    // Show/Hide next--> and <--prev in links bar
    if (group_index === 0) {
        $("#prev_q").hide();
        $("#q_sep").hide();
    } else if (group_index === form_divs.length-1) {
        $("#next_q").hide();
        $("#q_sep").hide();
    } else {
        $("#prev_q").show();
        $("#next_q").show();
        $("#q_sep").show();
    }
}

function next_group() {
    for(var i=0; i<form_divs.length; i++) {
        if ($("#" + form_divs[i]).is(":visible")) {
            show_group(form_divs[i+1]);
            return true;
        }
    }
}

function prev_group() {
    for(var i=0; i<form_divs.length; i++) {
        if ($("#" + form_divs[i]).is(":visible")) {
            show_group(form_divs[i-1]);
            return true;
        }
    }
}

function resetFun() {
    // The following doesn't work for hidden fields
    // so you also reset everything manually afterwards...
 
    if (confirm("This will clear all your input.")) {
        
        document.mh_form.reset();
    
        for(var i=0; i<document.mh_form.elements.length; i++) {
            var curElement = document.mh_form.elements[i];
            var curName = curElement.name;
            var curId = curElement.id;
            var curType = curId.substring(0,3);
            
            if (curType === "_r_") {
                curElement.checked = false;
            }
            
            if (curType === "_a_") {
                curElement.value = "1";
                // Calling this jQuery function will trigger the event handler to detect
                // change so the sliders can adjust accordingly
                $('#' + curId).trigger('change');
            }
            
            if (curType === "_v_") {
                curElement.value = "0";
                slidersArray[curName].setValue(0);
            }
        }
    }
}

// MAIN (ran upon site loaded...)
show_group("form_help");




