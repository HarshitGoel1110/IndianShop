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
  db.collection('shop/'+myParam+'/product').get().then((snapshot) => {
    snapshot.docs.forEach(doc => {
        dispProduct(doc.data(),firebase.auth().currentUser.uid);
    })
});
  if (user) {
    if (myParam == firebase.auth().currentUser.uid)
    {
      document.querySelector('#addbtn').style.display = 'block';
    } 
  }
});

const dispProduct=(product,user)=>{
  let html;
  if (myParam == user){
    html=`
    <div class="lg:w-1/4 md:w-1/2 p-4 w-full">
        <a class="block relative h-48 rounded overflow-hidden">
        <img alt="ecommerce" class="object-cover object-center w-full h-full block" src="https://dummyimage.com/428x268">
        </a>
        <div class="mt-4">
        <h3 class="text-gray-500 text-xs tracking-widest title-font mb-1">${product.description}</h3>
        <h2 class="text-gray-900 title-font text-lg font-medium">${product.name}</h2>
        <p class="mt-1">₹ ${product.price}</p>
        </div>
    </div>
	`;
  }
  else{
    html=`
    <div class="lg:w-1/4 md:w-1/2 p-4 w-full">
        <a class="block relative h-48 rounded overflow-hidden">
        <img alt="ecommerce" class="object-cover object-center w-full h-full block" src="https://dummyimage.com/428x268">
        </a>
        <div class="mt-4">
        <h3 class="text-gray-500 text-xs tracking-widest title-font mb-1">${product.description}</h3>
        <h2 class="text-gray-900 title-font text-lg font-medium">${product.name}</h2>
        <p class="mt-1">₹ ${product.price}</p>
        <button class="flex ml-auto text-white bg-indigo-500 border-0 py-2 px-6 focus:outline-none hover:bg-indigo-600 rounded">Add to Cart</button>
        </div>
    </div>
	`;
  }
	
	list.innerHTML +=html;
}




// add new product
const createprodForm = document.querySelector('#product-form');
createprodForm.addEventListener('submit', (e) => {
e.preventDefault();
db.collection('shop/'+myParam+'/product').add({
  name: createprodForm['product-name'].value,
  description: createprodForm['product-desc'].value,
  price: createprodForm['product-price'].value
}).then(() => {
  createprodForm.reset();
  location.reload();
}).catch(err => {
  console.log(err.message);
});
});