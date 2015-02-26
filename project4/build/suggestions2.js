
/**
 * Provides suggestions for state names (USA).
 * @class
 * @scope public
 */
function StateSuggestions() {
    this.states = [
        "Alabama", "Alaska", "Arizona", "Arkansas",
        "California", "Colorado", "Connecticut",
        "Delaware", "Florida", "Georgia", "Hawaii",
        "Idaho", "Illinois", "Indiana", "Iowa",
        "Kansas", "Kentucky", "Louisiana",
        "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", 
        "Mississippi", "Missouri", "Montana",
        "Nebraska", "Nevada", "New Hampshire", "New Mexico", "New York",
        "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", 
        "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
        "Tennessee", "Texas", "Utah", "Vermont", "Virginia", 
        "Washington", "West Virginia", "Wisconsin", "Wyoming"  
    ];
}

/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
    var aSuggestions = [];
    var sTextboxValue = oAutoSuggestControl.textbox.value;
    var parseXml;

    if (typeof window.DOMParser != "undefined") {
        parseXml = function(xmlStr) {
            return ( new window.DOMParser() ).parseFromString(xmlStr, "text/xml");
        };
    } else if (typeof window.ActiveXObject != "undefined" &&
           new window.ActiveXObject("Microsoft.XMLDOM")) {
        parseXml = function(xmlStr) {
            var xmlDoc = new window.ActiveXObject("Microsoft.XMLDOM");
            xmlDoc.async = "false";
            xmlDoc.loadXML(xmlStr);
            return xmlDoc;
        };
    } else {
        throw new Error("No XML parser found");
    }
    
    /*
    if (sTextboxValue.length > 0){
    
        //search for matching states
        for (var i=0; i < this.states.length; i++) { 
            if (this.states[i].indexOf(sTextboxValue) == 0) {
                aSuggestions.push(this.states[i]);
            } 
        }
    }
    */

    if( sTextboxValue.length > 0 ) {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4) {
                var data = xhr.responseText;
                var xml = parseXml( data );
                var suggests = xml.getElementsByTagName('CompleteSuggestion');
                for( var i = 0; i < suggests.length; i++ ) {
                    var completeSuggestion = suggests[ i ];
                    var suggestion = completeSuggestion.getElementsByTagName('suggestion')[0];
                    var val = suggestion.getAttribute('data');
                    aSuggestions.push( val );
                    //console.log( val );
                }

                //provide suggestions to the control
                oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
            }
        }
        var url = 'http://localhost:1448/eBay/suggest?query=' + sTextboxValue; 
        xhr.open('GET', url, true);
        xhr.send(null);
    }

};
