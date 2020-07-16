const list=document.getElementById('displaystores');

function setItem1(id)
{
	console.log("called");
	window.localStorage.setItem("id",id);
}

const addShop=(store,doc)=>{

	let html=`
    <div class="p-4 md:w-1/3 shop-card relative overflow-hidden w-full">
    <div class="h-full border-2 border-gray-200 rounded-lg overflow-hidden">
      <canvas class="lg:h-48 md:h-36 w-full object-cover object-center image-shop"   width="400" height="200" name="${store.name}" alt="blog" ></canvas>
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
	
	// console.log(html);
	list.innerHTML +=html;


}



db.collection('shop').get().then((snapshot) =>{
	snapshot.docs.forEach(doc=>{
		addShop(doc.data(),doc.id);

	})

}).then(()=>{

         document.querySelectorAll('.image-shop').forEach((canvas)=>{

         var colours = ["#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e", "#16a085", "#27ae60", "#2980b9", "#8e44ad", "#2c3e50", "#f1c40f", "#e67e22", "#e74c3c", "#95a5a6", "#f39c12", "#d35400", "#c0392b", "#bdc3c7", "#7f8c8d"];


            var name = canvas.getAttribute("name");
            var nameSplit = name.split(" ");
//            console.log(nameSplit);
            if(nameSplit.length>=2) var initials = nameSplit[0][0].toUpperCase() + nameSplit[1][0].toUpperCase();
            else
            {
               if(nameSplit[0].length==1) var initials = nameSplit[0][0].toUpperCase();
               else var initials = nameSplit[0][0].toUpperCase()+nameSplit[0][1].toLowerCase();

            }



            var charIndex = initials.charCodeAt(0) - 65,
                colourIndex = charIndex % 19;

            var context = canvas.getContext("2d");

            var canvasWidth = document.querySelector('.shop-card').offsetWidth,
                canvasHeight = 200,
                canvasCssWidth = canvasWidth,
                canvasCssHeight = canvasHeight;

            if (window.devicePixelRatio) {
                $(canvas).attr("width", canvasWidth * window.devicePixelRatio);
                $(canvas).attr("height", canvasHeight * window.devicePixelRatio);
                $(canvas).css("width", canvasCssWidth);
                $(canvas).css("height", canvasCssHeight);
                context.scale(window.devicePixelRatio, window.devicePixelRatio);
            }

            context.fillStyle = colours[colourIndex];
            context.fillRect (0, 0, canvas.width, canvas.height);
            context.font = "128px Arial";
            context.textAlign = "center";
            context.fillStyle = "#FFF";
            context.fillText(initials, canvasCssWidth / 2, canvasCssHeight / 1.5);
            window.addEventListener('resize',init,false);
    })

}).catch(err=>{
	console.log(err);
})

function init()
{
    document.querySelectorAll('.image-shop').forEach((canvas)=>{
    var colours = ["#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e", "#16a085", "#27ae60", "#2980b9", "#8e44ad", "#2c3e50", "#f1c40f", "#e67e22", "#e74c3c", "#95a5a6", "#f39c12", "#d35400", "#c0392b", "#bdc3c7", "#7f8c8d"];
//            console.log(document.querySelector('.shop-card').offsetWidth);

            var name = canvas.getAttribute("name");
            var nameSplit = name.split(" ");
//            console.log(nameSplit);
            if(nameSplit.length>=2) var initials = nameSplit[0][0].toUpperCase() + nameSplit[1][0].toUpperCase();
            else
            {
               if(nameSplit[0].length==1) var initials = nameSplit[0][0].toUpperCase();
               else var initials = nameSplit[0][0].toUpperCase()+nameSplit[0][1].toLowerCase();

            }

            var charIndex = initials.charCodeAt(0) - 65,
                colourIndex = charIndex % 19;

            var context = canvas.getContext("2d");

            var canvasWidth = document.querySelector('.shop-card').offsetWidth,
                canvasHeight = 200,
                canvasCssWidth = canvasWidth,
                canvasCssHeight = canvasHeight;

            if (window.devicePixelRatio) {
                $(canvas).attr("width", canvasWidth * window.devicePixelRatio);
                $(canvas).attr("height", canvasHeight * window.devicePixelRatio);
                $(canvas).css("width", canvasCssWidth);
                $(canvas).css("height", canvasCssHeight);
                context.scale(window.devicePixelRatio, window.devicePixelRatio);
            }

            context.fillStyle = colours[colourIndex];
            context.fillRect (0, 0, canvas.width, canvas.height);
            context.font = "128px Arial";
            context.textAlign = "center";
            context.fillStyle = "#FFF";
            context.fillText(initials, canvasCssWidth / 2, canvasCssHeight / 1.5);
     })
}
