

var url = document.location.href;
var myParam = location.search.split('name=')[1];
const list=document.getElementById('displayproducts');
function sans(){
    console.log('clicked');
    var owner_name=document.querySelector('.owner-name').innerHTML;
    var shop_name=document.querySelector('.shop-name').innerHTML;
    var shop_address=document.querySelector('.shop-address').innerHTML;
    var shop_mobile=document.querySelector('.shop-mobile').innerHTML;
    var myParam = location.search.split('name=')[1];
    Swal.fire({
            title: 'Edit Shop Details',
            html:`<form id="shop-form" class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            <div class="mb-4">
                Shop Name
              <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="shop-name" type="text" placeholder="Product Name" value="${shop_name}">
            </div>
            <div class="mb-4">
                Owner Name
              <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="owner-name" type="text" placeholder="Product Name" value="${owner_name}">
            </div>

            <div class="mb-4">
                Mobile No
              <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="shop-mobile" type="text" placeholder="package quantity gr/mL" value="${shop_mobile}">
            </div>
            <div class="mb-4">
                 Shop Address
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="shop-address" type="text" placeholder="99" value="${shop_address}">
            </div>


            </form>`,

            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, Edit it!'

            }).then((result) => {

            if (result.value) {
            	const createprodForm = document.querySelector('#shop-form');

//            	console.log("user",user.uid);
//                console.log(createprodForm['product-name'].value);
                Swal.fire(
                'Edited! ',
                'Your Shop is Edited.',
                'success'
                )
                console.log(myParam);
                db.collection('shop').doc(myParam).update({
                    mobile:createprodForm['shop-mobile'].value,
                    address: createprodForm['shop-address'].value,
                    name:createprodForm['shop-name'].value,
                })
                db.collection('users').doc(myParam).update({
                  name: createprodForm['owner-name'].value,
                  mobile: createprodForm['shop-mobile'].value,

              }).then(() => {
                createprodForm.reset();
                location.reload();
              }).catch(err => {
                console.log(err.message);
              });
            }
            })
}



const lt=document.getElementById('storename');
const cc=document.getElementById('contactcard');
db.collection('users').doc(myParam).get().then(s1=>{
db.collection('shop').doc(myParam).get().then(doc=>{

	let ht=`

	<h1 class="sm:text-4xl text-2xl font-medium title-font mb-2 text-gray-900">${doc.data().name}</h1>`;
  lt.innerHTML += ht;
  let cch=`

  <div class="lg:w-2/3 md:w-2/3 sm:w-2/3 bg-gray-300 rounded-lg overflow-hidden sm:mr-10 p-10 flex items-end justify-start relative">

    <div class="lg:w-1/2 md:w-1/1 sm:w-2/3 px-6">
      <h2 class="title-font font-medium text-gray-900 tracking-widest text-sm ">STORE NAME</h2>
      <p class="leading-relaxed"><span class='shop-name'>${doc.data().name}</span></p>
      <h2 class="title-font font-medium text-gray-900 tracking-widest text-sm mt-4">STORE OWNER NAME</h2>
      <p class="leading-relaxed"><span class='owner-name'>${s1.data().name}</span></p>
       <h2 class="title-font font-medium text-gray-900 tracking-widest text-sm mt-4">STORE ADDRESS</h2>
      <p class="leading-relaxed"><span class='shop-address'>${doc.data().address}</span></p>
      <h2 class="title-font font-medium text-gray-900 tracking-widest text-sm mt-4">STORE EMAIL</h2>
      <a class="text-purple-500 leading-relaxed"><span class='shop-email'>${doc.data().email}</span></a>
      <h2 class="title-font font-medium text-gray-900 tracking-widest text-sm mt-4">STORE PHONE</h2>
      <p class="leading-relaxed"><span class='shop-mobile'>${doc.data().mobile}</span></p>
    </div>
</div>
`;
  cc.innerHTML += cch;
});

});


console.log(myParam);
firebase.auth().onAuthStateChanged( user => {
  list.innerHTML='';
  if(user.uid==myParam)
  {

    cc.innerHTML=`<div ><button id="setting_yes" onclick = "sans()">setting</button></div>`+cc.innerHTML;
    document.querySelector('#setting_yes').onclick=sans;

  }
  var count=0;
  db.collection('shop/'+myParam+'/product').get().then((snapshot) => {
    var len=Object.keys(snapshot.docs).length
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
        count+=1;
        dispProduct(doc.data(),doc.id,user_id,count,len);
    })
});
  if (user) {
    if (myParam == firebase.auth().currentUser.uid)
    {
      document.querySelector('#addbtn').style.display = 'block';
    } 
  }
});

function dispProduct(product,idpro,user,count,final)
{
  let html;
  console.log(myParam,user);
  if (myParam == user){

    html=`
    <div class="lg:w-1/4 md:w-1/2 p-4 w-full block-${user}-${idpro}">
        <a class="block relative h-48 rounded overflow-hidden">
        <img alt="ecommerce"  class="object-contain object-center w-full h-full block" src="${product.image}">
        </a>
        <div class="mt-4">
        <h3 class="text-gray-500 text-xs tracking-widest title-font mb-1" id="desc-${idpro}">${product.description}</h3>
        <h2 class="text-gray-900 title-font text-lg font-medium" id="name-${idpro}">${product.name}</h2>
        <p class="mt-1" id="price-${idpro}">₹ ${product.price}</p>
        <div class="dropdown inline-block relative">
          <button class="bg-gray-300 text-gray-700 font-semibold py-2 px-4 rounded inline-flex items-center edit_yes" id="edit-${user}-${idpro}">
            <span class="mr-1">Edit</span>
          </button>


          <button class="bg-red-500 text-black font-semibold py-2 px-4 rounded inline-flex items-center edit_yes45" id="edit1-${user}-${idpro}" >
            <span class="mr-1">Delete</span>
          </button>
        </div>
    </div>
	`;
  }
  else{
    html=`
    <div class="lg:w-1/4 md:w-1/2 p-4 w-full block-${user}-${idpro}">
        <a class="block relative h-48 rounded overflow-hidden">
        <img alt="ecommerce" class="object-contain object-center w-full h-full block" src="${product.image}">
        </a>
        <div class="mt-4">

        <h3 class="text-gray-500 text-xs tracking-widest title-font mb-1" id="desc-${idpro}">${product.description}</h3>
        <h2 class="text-gray-900 title-font text-lg font-medium" id="name-${idpro}">${product.name}</h2>
        <p class="mt-1" id="price-${idpro}">₹ ${product.price}</p>
        <button class="flex ml-auto text-white bg-indigo-500 border-0 py-2 px-6 focus:outline-none hover:bg-indigo-600 rounded cart " id="${idpro}" >Add to Cart</button>
        </div>
    </div>
	`;
  }

	list.innerHTML +=html;




	document.querySelectorAll('.edit_yes').forEach((x)=>{

        x.onclick = function(event){
            event.preventDefault();
            console.log(x.id)
            content=x.id.split("-");
            var name=document.querySelector("#name-"+content[2]).innerHTML;
            var price=document.querySelector("#price-"+content[2]).innerHTML.split(" ")[1];
            var desc=document.querySelector("#desc-"+content[2]).innerHTML;
            Swal.fire({
            title: 'Edit',
            html:`<form id="product-form-${content[1]}-${content[2]}" class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            <div class="mb-4">
                Name
              <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="product-name" type="text" placeholder="Product Name" value="${name}">
            </div>
            <div class="mb-4">
                Description
              <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="product-desc" type="text" placeholder="package quantity gr/mL" value="${desc}">
            </div>
            <div class="mb-4">
                 Price
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="product-price" type="text" placeholder="99" value=${price}>
            </div>


            </form>`,

            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, Edit it!'

            }).then((result) => {

            if (result.value) {
            	const createprodForm = document.querySelector('#product-form-'+content[1]+"-"+content[2]);

            	console.log(content[1],content[2]);
                console.log(createprodForm['product-name'].value);
                Swal.fire(
                'Edited! ',
                'Your Product is Edited.',
                'success'
                )
                db.collection('shop/'+content[1]+'/product').doc(content[2]).update({
                  name: createprodForm['product-name'].value,
                  description: createprodForm['product-desc'].value,
                  price: createprodForm['product-price'].value
              }).then(() => {
                createprodForm.reset();
                location.reload();
              }).catch(err => {
                console.log(err.message);
              });
            }
            })

        }
        }
    )
//    console.log($('.delete_yes'));

    document.querySelectorAll('.edit_yes45').forEach((x)=>{

         x.onclick= function(event){
            event.preventDefault();
            Swal.fire({
            title: 'Are you sure? ',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
            if (result.value) {
                content=this.id.split("-");
                console.log(content);

                document.querySelector(".block-"+content[1]+'-'+content[2]).style.display='none';
                db.collection('shop/'+content[1]+'/product').doc(content[2]).delete();
                Swal.fire(
                'Deleted!',
                'Your file has been deleted.',
                'success'
                )
            }
            })
    };
    })


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


