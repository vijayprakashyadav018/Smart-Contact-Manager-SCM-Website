console.log("Contacts.js");

//ye isliye bnaya BCZ project ko deploy krte time baseURL change hoga (server host)
const baseURL = "http://localhost:8081";
const viewContactModal=document.getElementById('view_contact_modal');


// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal = new Modal(viewContactModal,options,instanceOptions);

function openContactModal() {
	contactModal.show();
}

function closeContactModal() {
	contactModal.hide();
}

async function loadContactdata(id){
	
	
	//function call to load data 
	console.log(id);
  
	try{
		const data =await(await fetch(`${baseURL}/api/contacts/${id}`)
		).json();
		console.log(data);
		document.querySelector("#contact_name").innerHTML=data.name;
		document.querySelector("#contact_number").innerHTML=data.phoneNumber;
		document.getElementById("contact_image").src=data.picture;
		document.querySelector("#contact_email").innerHTML=data.email;
		document.querySelector("#contact_address").innerHTML=data.address;
		document.querySelector("#contact_about").innerHTML=data.description;
		
		//for website !!
	//	document.querySelector("#website_link").innerHTML=data.websiteLink;
	 	document.getElementById("website_link").innerHTML = `<a href="${data.websiteLink}" target="_blank" class="text-blue-600 hover:underline"> ${data.websiteLink} </a>`;
	
		
	  // for linkedin !!	
      //  document.querySelector("#linkedin_link").innerHTML=data.linkedInLink;
	  document.getElementById("linkedin_link").innerHTML = `<a href="${data.linkedInLink}" target="_blank" class="text-blue-600 hover:underline"> ${data.linkedInLink} </a>`;
	  	 
	  
	  
	  //document.querySelector("#contact_favorite").innerHTML=data.favorite;
		// Handling Favorite Condition
		       const favoriteIconSpan = document.getElementById("favorite_icon");
		       const favoriteTextSpan = document.getElementById("contact_favorite");

		       if (data.favorite === true || data.favorite === "true") {
		           favoriteIconSpan.innerHTML = `
		               <a class=" flex justify-center">
		                   <img src="/images/star (2).png" class="h-5 w-5" alt="Star Icon">
		               </a> 
		           `;
		         //  favoriteTextSpan.innerHTML = "Favorite User"; // Ensuring text is correct
		       } else {
		           favoriteIconSpan.innerHTML = "Not Favorite"; // Removing icon if not favorite
		          // favoriteTextSpan.innerHTML = "Not Favorite"; // Changing text when not favorite
		       }
		openContactModal();
	}catch(error){
		console.log("Error", error);
	}
	
	
		
}
	
//delete contact

async function deleteContact(id) {
	Swal.fire({
	  title: "Do you want to delete the contact ?",
	  icon:"warning",
	  showCancelButton: true,
	  confirmButtonText: "Delete",
	 
	}).then((result) => {
	  /* Read more about isConfirmed, isDenied below */
	  if (result.isConfirmed) {
		//Swal.fire("Delete!", "", "success");
		const url = `${baseURL}/user/contacts/delete/` +id;
		window.location.replace(url);
		
	  } 
	});
}


