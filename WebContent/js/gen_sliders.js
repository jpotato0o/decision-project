var slidersArray = new Array();

// Create a YUI instance and request the slider module and its dependencies
YUI().use("slider", function (Y) {
    // Function to pass input value back to the Slider
    function updateSlider( e ) {
        var data   = this.getData(),
            slider = data.slider,
            value  = parseInt( this.get( "value" ), 10 );
        if ( data.wait ) {
            data.wait.cancel();
        }
        // Update the Slider on a delay to allow time for typing
        data.wait = Y.later( 200, slider, function () {
            data.wait = null;
            this.set( "value", value );
        } );
    }
    // Function to update the input value from the Slider value
    function updateInput( e ) {
        this.set( "value", e.newVal );
    }
    
    function createSlider(name)
    {
        var xInput,  // input tied to xSlider
        xSlider; // horizontal Slider
        // This curVal setting is to make the slider reload correctly on page back
        curVal = document.getElementById(name).value;
        // Create a horizontal Slider
        slidersArray[name] = new Y.Slider({
                min   : 0,      // min is the value at the top
                max   : 100,     // max is the value at the bottom
                value : curVal,       // initial value
                length: '101px',  // rail extended to afford all values
            });

        // Link the input value to the Slider
        xInput = Y.one( "#" + name );
        xInput.setData( { slider: slidersArray[name]} );
        // Pass the input as the 'this' object inside updateInput
        slidersArray[name].after( "valueChange", updateInput, xInput );
        xInput.on( "keyup", updateSlider );
        // Render the Slider next to the input
        slidersArray[name].render('.' + name);
    }
    
    //createSlider('_v_salary_value-3');
    for(var i=0; i<document.mh_form.elements.length; i++) {
        var curElement = document.mh_form.elements[i];
        var curName = curElement.name;
        var curType = curName.substring(0,3);
        if (curType === "_v_") {
            createSlider(curName);
        }
    }
});

// Now make sliders for the Variable Importance scores

function roundTwo (val) {
    return Math.round(val*100)/100;
}

function getRatio (val) {
    var MAX_RATIO = 200;
    var num = val;
    var denom = 100 - num;
    if (denom === 0) {
        return MAX_RATIO;
    } else {
        return roundTwo(num / denom);
    }
}

// Implemented so that slider initialized correctly on browser back
function invertRatio (ratio) {
    var MAX_RATIO = 200;
    ratio=Number(ratio);
    if (ratio === MAX_RATIO) {
        return 100;
    } else {
        var inverted = (100*ratio)/(ratio+1);
        return inverted;
    }
}

function createViSlider(id) {
    $(function() {
        var ratio = $( "#" + id ).val();
        var num = invertRatio(ratio);
        var slider = $( "#slider-" + id ).slider({
            range: "min",
            value: num,
            animate: true,
            min: 0,
            max: 100,
            slide: function( event, ui ) {
                $( "#" + id ).val( getRatio(ui.value) );
            }
        });
        $( "#" + id ).change(function() {
            slider.slider( "value", invertRatio(this.value) );
        });
        $( "#" + id ).val( getRatio($( "#slider-" + id ).slider( "value" )) );
    });
}

//createViSlider('_a_employed_value');
for(var i=0; i<document.mh_form.elements.length; i++) {
    var curElement = document.mh_form.elements[i];
    var curName = curElement.name;
    var curType = curName.substring(0,3);
    // salary does not get a slider
    if (curType === "_a_" && curName != "_a_salary_value") {
        createViSlider(curName);
    }
}



