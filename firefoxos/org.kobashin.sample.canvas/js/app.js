
$(function() {
	$("#func-btn").click(function(){
		console.log("----- 2");
		alert("Hello Javascript");
	});


	canvas = $("#sample-canvas").get(0).getContext('2d');
	canvas.beginPath();
	canvas.moveTo(100,100);
	canvas.lineTo(150,50);
	canvas.lineTo(20,200);
	canvas.closePath();

	canvas.stroke();


});



var installBtn = document.getElementById('install-btn');

if(installBtn) {
    
	installBtn.style.display = 'none';
    
    // If you want an installation button, add this to your HTML:
    //
    // <button id="install-btn">Install</button>
    //
    // This code shows the button if the apps platform is available
    // and this app isn't already installed.
    if(navigator.mozApps) {

		console.log("---- 1");

        installBtn.addEventListener('click', function() {
			console.log("---- 1");
            navigator.mozApps.install(location.href + 'manifest.webapp');
        }, false);

        var req = navigator.mozApps.getSelf();
        req.onsuccess = function() {
            if(!req.result) {
                installBtn.style.display = 'block';
            }
        };

    }
}
