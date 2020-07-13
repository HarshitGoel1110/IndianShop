var qw=document.querySelector('.pending-order');
var qw1=document.querySelector('.ordered-delivered');
var html,headhtml;
var countsnap=0;
firebase.auth().onAuthStateChanged(user => {
db.collection('users/'+ firebase.auth().currentUser.uid +'/bought').orderBy("timestamp").get().then((snapshot) => {
        snapshot.docs.forEach(doc => {
            var date=new Date(parseInt(doc.id));
            var deliverery_address=doc.data().address;
            var d1=date.toDateString(),t1=date.toLocaleTimeString();
            db.collection('shop').doc(doc.data().shop).get().then(doc1=>{

             html=`<div class="container" id="remove-${doc.id}">
          <input type="checkbox" name="panel" id="panel-${doc.id}" class="hidden">
          <label for="panel-${doc.id}" >
           <div class="py-8 flex flex-wrap md:flex-no-wrap">
              <div class="md:w-64 md:mb-0 mb-6 flex-shrink-0 flex flex-col">
                <span class="mt-1 text-gray-500 text-sm">${d1}</span>
                <div class="mt-1 text-gray-500 text-sm">${t1}</div>
              </div>
              <div class="md:flex-grow">
                <h2 class="text-2xl font-medium text-gray-900 title-font mb-2"><strong>Name-</strong>${doc1.data().name}</h2>
                <p class="leading-relaxed"><strong>SHOP ADDRESS-</strong>${doc1.data().address}</p>
                <p class="leading-relaxed"><strong>SHOP PHONE NO.</strong> ${doc1.data().mobile}</p>
                <p class="leading-relaxed"><strong>ORDER DELIVERY ADDRESS</strong> ${deliverery_address}</p>
                <br>
                <p>Click to view the bill</p>
                <br>
                <div class="button-${doc.id} button-click" id="completed-${doc.id}">
                <div class="flex ml-auto text-white bg-indigo-500 border-0 py-2 px-6 focus:outline-none hover:bg-indigo-600 rounded cart   " >Order Pending</div>
                </div>
              </div>
            </div>
          </label>
          <div class="accordion__content overflow-hidden bg-grey-lighter ">
            <h2 class="text-2xl font-medium text-gray-900 title-font mb-2">Header</h2>
                <table>
                  <tr>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>total</th>
                  </tr>`
            }).then(()=>{
                   headhtml=html;
                  console.log(doc.data().product);
                  var prod=doc.data().product;
                  var count=0;
                  var total_p=0;
                  var total_q=0;
                  for(var i in prod)
                  {
                    total_q+=parseInt(prod[i][1]);
                    total_p+=parseInt(prod[i][1])*parseInt(prod[i][2]);
                    headhtml+=`<tr>
                                <td>${prod[i][0]}</td>
                                <td id='price-${doc.id}-${i}'>${prod[i][2]}</td>
                                <td id='quantity-${doc.id}-${i}'>${prod[i][1]}</td>
                                <td class="total-per-item-${doc.id}" id="total-${doc.id}-${i}">1500</td>
                               </tr>`
                    count++;
                    console.log(count,Object.keys(prod).length);
                    if(count==Object.keys(prod).length)
                    {
                        headhtml+=`<tr>
                                    <td><h2 class="text-2xl font-medium text-gray-900 title-font mb-2">Total:</h2></td>
                                    <td><h2 class="text-2xl font-medium text-gray-900 title-font mb-2 "></h2></td>
                                    <td><h2 class="text-2xl font-medium text-gray-900 title-font mb-2 total-quantity-${doc.id}">${total_q}</h2></td>
                                    <td><h2 class="text-2xl font-medium text-gray-900 title-font mb-2 total-price-${doc.id}">${total_p}</h2></td>
                                  </tr>
                                </table>
                                </div>
                                </div>`;

                            if(doc.data().delivered==false)
                            {

                                qw.innerHTML=headhtml+qw.innerHTML;
                            }


                            else
                            {

                                 qw1.innerHTML=headhtml+qw1.innerHTML;
                                document.querySelector(".button-"+doc.id).innerHTML='';
                            }

                            document.querySelectorAll('.total-per-item-'+doc.id).forEach(it=>{
                                   var arr=it.id.split("-");
                                   var price=parseInt(document.querySelector('#price-'+arr[1]+'-'+arr[2]).innerHTML);
                                   var qt=parseInt(document.querySelector('#quantity-'+arr[1]+'-'+arr[2]).innerHTML);
//                                   console.log(price,qt);
                                   it.innerHTML=price*qt;
                            })







                    }


                  }
                  })
            }
            )

    })

  });
