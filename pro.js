

var url = document.location.href;
var myParam = location.search.split('name=')[1];
const list=document.getElementById('displayproducts');



const lt=document.getElementById('storename');
db.collection('shop').doc(myParam).get().then(doc=>{
	let ht=`<h1 class="sm:text-4xl text-2xl font-medium title-font mb-2 text-gray-900">${doc.data().name}</h1>`;
	lt.innerHTML += ht;
});

console.log(myParam);
firebase.auth().onAuthStateChanged( user => {
  list.innerHTML='';
  db.collection('shop/'+myParam+'/product').get().then((snapshot) => {
    snapshot.docs.forEach(doc => {
        var ui=firebase.auth().currentUser,user_id;
        if(!ui)
        {
            user_id=null;
        }
        else
        {
            user_id=ui.uid;
        }
        dispProduct(doc.data(),doc.id,user_id);
    })
});
  if (user) {
    if (myParam == firebase.auth().currentUser.uid)
    {
      document.querySelector('#addbtn').style.display = 'block';
    } 
  }
});

function dispProduct(product,idpro,user)
{
  let html;
  console.log(myParam,user);
  if (myParam == user){
    html=`
    <div class="lg:w-1/4 md:w-1/2 p-4 w-full">
        <a class="block relative h-48 rounded overflow-hidden">
        <img alt="ecommerce"  class="object-contain object-center w-full h-full block" src="${product.image}">
        </a>
        <div class="mt-4">
        <h3 class="text-gray-500 text-xs tracking-widest title-font mb-1">${product.description}</h3>
        <h2 class="text-gray-900 title-font text-lg font-medium">${product.name}</h2>
        <p class="mt-1">₹ ${product.price}</p>
        <div class="dropdown inline-block relative">
          <button class="bg-gray-300 text-gray-700 font-semibold py-2 px-4 rounded inline-flex items-center">
            <span class="mr-1">Options</span>
          </button>
          <ul class="dropdown-menu absolute hidden text-gray-700 pt-1">
            <li><a class="modal-open rounded-t bg-gray-200 hover:bg-gray-400 py-2 px-4 block whitespace-no-wrap" href="editprod.html">Edit</a></li>
            <li><a class="bg-gray-200 hover:bg-gray-400 py-2 px-4 block whitespace-no-wrap" href="#">Delete</a></li>
          </ul>
        </div>
        </div>
    </div>
	`;
  }
  else{
    html=`
    <div class="lg:w-1/4 md:w-1/2 p-4 w-full">
        <a class="block relative h-48 rounded overflow-hidden">
        <img alt="ecommerce" class="object-contain object-center w-full h-full block" src="${product.image}">
        </a>
        <div class="mt-4">

        <h3 class="text-gray-500 text-xs tracking-widest title-font mb-1">${product.description}</h3>
        <h2 class="text-gray-900 title-font text-lg font-medium">${product.name}</h2>
        <p class="mt-1" id="price-${idpro}">₹ ${product.price}</p>
        <button class="flex ml-auto text-white bg-indigo-500 border-0 py-2 px-6 focus:outline-none hover:bg-indigo-600 rounded cart " id="${idpro}" >Add to Cart</button>
        </div>
    </div>
	`;
  }

	list.innerHTML +=html;
	$('.cart').click(function(){
	    if(user==null)
	    {
	        return alert("You are not logged in");
	    }

    var cart,size;



    if(window.localStorage.getItem(myParam)==null)
    {
        cart={};
        if(window.localStorage.length==0)
        {
            size=0;
        }
        else
        {
             size=JSON.parse(localStorage.getItem("size"));
        }
//        size=1;
    }
    else
    {
        cart=JSON.parse(localStorage.getItem(myParam));
        size=JSON.parse(localStorage.getItem("size"));
    }
    if(cart[this.id]!=1)
    {
        size=JSON.parse(localStorage.getItem("size"));
        size++;
        cart[this.id]=1;
    }
    window.localStorage.setItem("size",JSON.stringify(size));
    window.localStorage.setItem(myParam,JSON.stringify(cart));

    })
}




// add new product
const createprodForm = document.querySelector('#product-form');
createprodForm.addEventListener('submit', (e) => {
  e.preventDefault();

  var timestamp = Number(new Date());
  var fn = timestamp.toString();
  var file = document.getElementById("product_photo").files[0];
  var url="";
  firebase.storage().ref('images/' + fn + '.jpg').put(file).then(function() {
    firebase.storage().ref('images/' + fn + '.jpg').getDownloadURL().then(imgUrl => {
    url = imgUrl.toString();
    console.log(url);
  }).then(function(){
    console.log(createprodForm);
    db.collection('shop/'+myParam+'/product').add({
      name: createprodForm['product-name'].value,
      description: createprodForm['product-desc'].value,
      price: createprodForm['product-price'].value,
      image: url
  }).then(() => {
    createprodForm.reset();
    location.reload();
  }).catch(err => {
    console.log(err.message);
  });
  });
  });
});


