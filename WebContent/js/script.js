function set_score_hide_type(id_type, hide_flag) {
    for(var i=0; i<document.mh_form.elements.length; i++) {
        var curElement = document.mh_form.elements[i];
        var curName = curElement.name;
        var curType = curName.substring(0,3);
        if (curType === id_type) {
            // You were originally hiding by changing type, but the browser wasn't caching hidden values for Back
            //curElement.setAttribute("type", hide_type);
            if (hide_flag) {
                curElement.style.display="none";
            }
            else {
                curElement.style.display="";
            }
        }
    }
}

var HIDE_VI_SCORES_FLAG = true;

function hide_vi_scores() {
    set_score_hide_type("_a_", true);
}

function unhide_vi_scores() {
    set_score_hide_type("_a_", false);
}

if (HIDE_VI_SCORES_FLAG) {
    hide_vi_scores();
}



var HIDE_OUTCOME_SCORES_FLAG = true;

function hide_outcome_scores() {
    set_score_hide_type("_v_", true);
}

function unhide_outcome_scores() {
    set_score_hide_type("_v_", false);
}

if (HIDE_OUTCOME_SCORES_FLAG) {
    hide_outcome_scores();
}

function showHide(shID) {
    if (document.getElementById(shID)) {
        if (document.getElementById(shID+'-show').style.display != 'none') {
            document.getElementById(shID+'-show').style.display = 'none';
            document.getElementById(shID).style.display = 'block';
        }
        else {
            document.getElementById(shID+'-show').style.display = 'inline';
            document.getElementById(shID).style.display = 'none';
        }
    }
}



