(function () {
    "use strict";
    
    navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia;
    window.URL = window.URL || window.webkitURL;
    
    window.onload = function () {
	
        if (!navigator.getUserMedia) {
            console.error('getUserMedia not supported');
        }

        console.log('Page loaded');
	console.log('  testing...');
    };
})();

$(document).ready(function() {
    $(window).load(function() {
         $('#loader').hide();
         $('#pagecontent').show();
    });
});