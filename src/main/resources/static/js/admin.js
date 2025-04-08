console.log("admin user");

document.querySelector("#image_file_input").addEventListener("change", function(event) {
	
	let file =event.target.files[0];
	let reader = new FileReader();
	reader.onload = function() {
		
		document.querySelector("#upload_image_preview").setAttribute("src",reader.result);
		          // upr wali line ke code ko yese bhi likh sakte hai same hi bat hai
		//document.getElementById("upload_image_preview").src= reader.result;
	};
	reader.readAsDataURL(file);
	
});