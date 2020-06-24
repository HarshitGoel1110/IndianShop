var url = document.location.href;

const lt=document.getElementById('storename');



const list=document.getElementById('displayproducts');

var qw="";
for(var i=url.length-1;i>=0;i--)
{   
    if(url[i]==='=')break;
    qw=url[i]+qw;
}
console.log(qw);
db.collection('shop').doc(qw).get().then(doc=>{
	let ht=`<h1 class="sm:text-4xl text-2xl font-medium title-font mb-2 text-gray-900">${doc.data().name}</h1>`;
	lt.innerHTML += ht;
}
)
const dispProduct=(product,id)=>{
  let html;
  if (id == firebase.auth().currentUser.uid){
    document.querySelector('#addbtn').style.display = 'block';
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

var myParam = location.search.split('name=')[1]

db.collection('shop/'+qw+'/product').get().then((snapshot) => {
    snapshot.docs.forEach(doc => {
        dispProduct(doc.data(),myParam);
    })
}).catch(err => {
    console.log(err);
});


// add new product
const createprodForm = document.querySelector('#product-form');
createprodForm.addEventListener('submit', (e) => {
e.preventDefault();
db.collection('shop/'+qw+'/product').add({
  name: createprodForm['product-name'].value,
  description: createprodForm['product-desc'].value,
  price: createprodForm['product-price'].value
}).then(() => {
  createprodForm.reset();
}).catch(err => {
  console.log(err.message);
});
});