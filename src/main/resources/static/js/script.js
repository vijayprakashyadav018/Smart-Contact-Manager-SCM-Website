console.log("Script loaded");


//change theme work start :--

let currentTheme=getTheme();
//initial -->


document.addEventListener('DOMContentLoaded', () => {
	changeTheme();
});

// To Do 
function changeTheme() {
	
	//set to web page
	changePageTheme(currentTheme,currentTheme);
	//document.querySelector("html").classList.add(currentTheme);
	
	//set the listener to change theme button 
	const changeThemeButton = document.querySelector("#theme_change_button");
	
	
	
	
	
	
	changeThemeButton.addEventListener("click", () => {
	
		//get the current theme			
	let oldTheme =currentTheme;
	console.log("change theme button clicked");
	
	if (currentTheme == "dark") {
		//theme ko light
		currentTheme = "light";
	}
	else{
		//theme ko dark 
		currentTheme = "dark";
	}
	
	console.log(currentTheme);
	changePageTheme(currentTheme,oldTheme);
	
	});
}


// set theme to local storage
function setTheme(theme){
	localStorage.setItem("theme",theme);
}



//get theme from local storage
function getTheme(){
	let theme = localStorage.getItem("theme");
	return theme ? theme : "light";
}

//change curent page theme
function changePageTheme(theme, oldTheme){
	
	//local storage me update karenge 
		setTheme(currentTheme);
		//remove the current theme
		if(oldTheme){
		document.querySelector("html").classList.remove(oldTheme);
	    }
		//set the current theme
		document.querySelector("html").classList.add(theme);
		
		
		//change the text of button
		document.querySelector("#theme_change_button").querySelector("span").textContent = theme =="light"?"Dark" : "Light";
				
		
}

//change theme work End here :--



