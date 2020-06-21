const list=document.getElementById('displaystores');

function setItem1(id)
{
	console.log("called");
	window.localStorage.setItem("id",id);
}

const addShop=(store,doc)=>{
	let html=`
    <div class="p-4 md:w-1/3">
    <div class="h-full border-2 border-gray-200 rounded-lg overflow-hidden">
      <img class="lg:h-48 md:h-36 w-full object-cover object-center" src="https://dummyimage.com/720x400" alt="blog">
      <div class="p-6">
        <h2 class="tracking-widest text-xs title-font font-medium text-gray-500 mb-1">${store.type}</h2>
        <h1 class="title-font text-lg font-medium text-gray-900 mb-3">${store.name}</h1>
        <p class="leading-relaxed mb-3">${store.description}</p>
        <div class="flex items-center flex-wrap ">
          <a class="text-indigo-500 inline-flex items-center md:mb-2 lg:mb-0" href='pro.html?name=${doc}')>Explore this store 
            <svg class="w-4 h-4 ml-2" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
              <path d="M5 12h14"></path>
              <path d="M12 5l7 7-7 7"></path>
            </svg>
          </a>
  
        </div>
      </div>
    </div>
  </div>
	`;
	console.log(doc);
	
	// console.log(html);
	list.innerHTML +=html;
}



db.collection('shop').get().then((snapshot) =>{
	snapshot.docs.forEach(doc=>{
		addShop(doc.data(),doc.id);
	})

}).catch(err=>{
	console.log(err);
})