const loggedOutLinks = document.querySelectorAll('.logged-out');
const loggedInLinks = document.querySelectorAll('.logged-in');
const accountDetails = document.querySelector('.account-details');
const create = document.querySelectorAll('.create');
const view = document.querySelectorAll('.view');



const setupUI = (user) => {
  if (user) {
    loggedInLinks.forEach(item => item.style.display = 'block');
    loggedOutLinks.forEach(item => item.style.display = 'none');
    // account info
    db.collection('users').doc(user.uid).get().then(doc => {
      if(doc.data().shop==true)
      {
            db.collection('shop').doc(user.uid).get().then(s1=>{
                var date=new Date(parseInt(s1.data().timestamp));
//            var deliverery_address=doc.data().address;
            var d1=date.toDateString();
                 const html = `
                <div >Logged in as ${user.email}</div>
                <div >Name: <span class="name_acc">${doc.data().name}</span></div>
                <div >Mobile No: <span class="mobile_acc">${doc.data().mobile}</span></div>
                <div>Address: <span class="address_acc">${doc.data().address}</span></div>
                <div>Store Created: <span class="shop_created">${doc.data().shop}</span></div>
                <div>Date of store creation: ${d1}</div>
              `;
               accountDetails.innerHTML = html;

            }
            )

      }
      else
      {
             const html = `
        <div >Logged in as ${user.email}</div>
        <div >Name: <span class="name_acc">${doc.data().name}</span></div>
        <div >Mobile No: <span class="mobile_acc">${doc.data().mobile}</span></div>
        <div>Address: <span class="address_acc">${doc.data().address}</span></div>
        <div>Store Created: <span class="shop_created">${doc.data().shop}</span></div>
      `;
      accountDetails.innerHTML = html;
      }


    });
    // toggle user UI elements
    var shop;
    db.collection('users').doc(user.uid).get().then(doc =>{
    shop=doc.data().shop;
         if(shop==false)
        {
            create.forEach(item => item.style.display='block');
            view.forEach(item => item.style.display = 'none');
        }
        else
        {
            create.forEach(item => item.style.display='none');
            view.forEach(item => item.style.display = 'block');
            document.getElementById("qwe").href = `pro.html?name=${user.uid}`;
        }
    });
    document.querySelector('.setting-click').addEventListener("click",function(event){
       console.log("clicked");
       name=document.querySelector('.name_acc').innerHTML;
       mobile=document.querySelector('.mobile_acc').innerHTML;
       address=document.querySelector('.address_acc').innerHTML;
       console.log(name,mobile,address);
       event.preventDefault();
            Swal.fire({
            title: 'Edit Your Details',
            html:`<form id="user-form" class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            <div class="mb-4">
                Name
              <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="user-name" type="text" placeholder="Product Name" value="${name}">
            </div>
            <div class="mb-4">
                Mobile No
              <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="user-mobile" type="text" placeholder="package quantity gr/mL" value="${mobile}">
            </div>
            <div class="mb-4">
                 Address
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="user-address" type="text" placeholder="99" value="${address}">
            </div>


            </form>`,

            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, Edit it!'

            }).then((result) => {

            if (result.value) {
            	const createprodForm = document.querySelector('#user-form');

//            	console.log("user",user.uid);
//                console.log(createprodForm['product-name'].value);
                Swal.fire(
                'Edited! ',
                'Your Account is Edited.',
                'success'
                )
                db.collection('shop').doc(user.uid).update({
                    mobile:createprodForm['user-mobile'].value,
                })
                db.collection('users').doc(user.uid).update({
                  name: createprodForm['user-name'].value,
                  mobile: createprodForm['user-mobile'].value,
                  address: createprodForm['user-address'].value

              }).then(() => {
                createprodForm.reset();
                location.reload();
              }).catch(err => {
                console.log(err.message);
              });
            }
            })
    })
  }
  else {
    loggedInLinks.forEach(item => item.style.display = 'none');
    loggedOutLinks.forEach(item => item.style.display = 'block');
    // clear account info
    accountDetails.innerHTML = '';
    // toggle user elements
    localStorage.clear();
  }
};