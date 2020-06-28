


var w=window.localStorage;
var i;
var html;
console.log("Sansakr")
var ad=document.querySelector('.cart_item');

function func(key)
{
    var x=key.split('-');
    if(x.length==2)
    {
        return false;
    }
    return true;
}



var head=document.querySelector('.cart_head');
for (var key in w) {
      if (w.hasOwnProperty(key) && key!="size" && func(key)) {
//        html=html+" "+"shop"+" "+key;
          var name;
          db.collection('shop').doc(key).get().then(doc=>{

                name=doc.data().name;
//                html=`<br><------------------><br><div>       <h1>Shop : ${name} </h1>           </div>`;
                  html=`
              <div class="modal-header border-bottom-0   remove-${doc.id}">
                  <h5 class="modal-title" id="exampleModalLabel">
                    ${name}
                  </h5>

              </div>
              <div class="modal-body remove-${doc.id}">
              <table class="table table-image">
                  <thead>
                  <tr>
                    <th scope="col">Image</th>
                    <th scope="col">Product</th>
                    <th scope="col">Price</th>
                    <th scope="col">Qty</th>
                    <th scope="col">Total</th>
                    <th scope="col">Actions</th>
                  </tr>
                </thead>
              </table>
              </div>
                  `;
                head.innerHTML+=html;

          })

           var final;
          async function myfunc(){
           var x=JSON.parse(w[key]);
//           console.log(x,x.length);
           var price=0,total=0,count=0;
           var utr={};
           for(var key1 in x){

                var wer=key;
                db.collection('shop/'+key+'/product').doc(key1).get().then(pro=>{
//                html=`<div><h3>${pro.data().name}   Price:${pro.data().price}</h3><div>${pro.data().description}</div><br></div>`;

                utr[pro.id]=pro.data().price;
                html=`
                <table class="table table-image " id='view-${wer}-${pro.id}'>
                <tbody>
                  <tr>
                    <td class="w-25">
                      <img src="https://s3.eu-central-1.amazonaws.com/bootstrapbaymisc/blog/24_days_bootstrap/vans.png" class="img-fluid img-thumbnail" alt="Sheep">
                    </td>
                    <td>${pro.data().name}</td>
                    <td class='price-${wer}-${pro.id}'>${pro.data().price}</td>
                    <td id='${wer}-${pro.id}'>
                    <span class="qt-plus">+</span>
					<span class="qt">${x[pro.id]} </span>
					<span class="qt-minus">-</span>
                    </td>

                    <td><div class="total"><div id="total-${wer}-${pro.id}">${pro.data().price}*${x[pro.id]}</div></td>

                    <td>
                      <a href="#" class="btn btn-danger btn-sm delete" id='delete-${wer}-${pro.id}'>
                        <i class="fa fa-times"></i>
                      </a>
                    </td>
                  </tr>
                </tbody>
                </table>

                `;
                price=parseInt(pro.data().price);
                ad.innerHTML+=html;



                }).then(()=>{
                    total+=price;
                    final=total;
                    count++;
                    if(count==Object.keys(x).length)
                    {
//                        console.log(wer);
                        html=`
                        <div class="remove-${wer}">
                        <table class="table table-image">
                        <div class="d-flex justify-content-end">
                            <h5>Total: <span class="price text-success final-total" id='final-${wer}'>${total}</span></h5>
                        </div>
                        <div class="container ">
                        <a class="modal-open bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded m-4 float-right mr-16 buy_shop" id='${wer}'>
                            Buy
                        </a>
                        </div>
                        </table>
                        </div>
                        `;
                        ad.innerHTML+=html;
                        console.log(wer,utr);

                        document.querySelectorAll('.total').forEach(doc=>{
                          var x=doc.children[0].id.split("-");
                          var load=doc.children[0].innerHTML.split('*');
                          if(load.length==2)
                          {
                            doc.children[0].innerHTML=parseInt(load[0])*parseInt(load[1]);
                          }
                        })

                       function sans()
                       {
                            document.querySelectorAll('.final-total').forEach(doc=>{
                            var qw=doc.id.split("-")[1];
                            var wer=JSON.parse(localStorage.getItem(qw));
                            var pr=JSON.parse(localStorage.getItem(qw+'-item'));
//                            console.log(wer,pr);
                            var ans=0;
                            for(var k in wer){
//                                console.log(wer[k],pr[k]);
                                    ans+=parseInt(wer[k])*pr[k];
                            }
                            doc.innerHTML=ans;
                        })
                        }
                       document.querySelectorAll('.delete').forEach(doc=>{
                                doc.addEventListener('click',()=>{
                                    qw=(doc.id.split("-"));
                                    var wer=JSON.parse(localStorage.getItem(qw[1]));
                                    delete wer[qw[2]]
                                    if(Object.keys(wer).length==0)
                                    {
                                       localStorage.removeItem(qw[1]);
                                       localStorage.removeItem(qw[1]+'-item');
                                        document.querySelector('#view-'+qw[1]+'-'+qw[2]).style.display='none';;
                                        document.querySelectorAll('.remove-'+qw[1]).forEach(doc=>{
                                             console.log(doc);
                                             doc.style.display='none';
                                             });
                                    }
                                    else
                                    {
                                        wer=JSON.stringify(wer);
                                        localStorage.setItem(qw[1],wer);
                                        sans();
                                        document.querySelector('#view-'+qw[1]+'-'+qw[2]).style.display='none';;
                                    }

                                    console.log(wer);

                                })
                       }
                       )



                        sans();
                        window.localStorage.setItem(wer+'-item',JSON.stringify(utr));
                        $('.buy_shop').click(function(){
                            console.log(this.id,"clicked");
                        })



                         $(".qt-plus").click(function(){
                            $(this).parent().children(".qt").html(parseInt($(this).parent().children(".qt").html()) + 1);


                            var child = $(this).parent().children(".qt");
                            var qw=($(this).parent()[0].id).split("-");
                            var wer=localStorage.getItem(qw[0]);
                            var pr=localStorage.getItem(qw[0]+'-item');
                            pr=JSON.parse(pr);
//                            console.log(pr);
                            val=parseInt(pr[qw[1]]);
                            wer=JSON.parse(wer);
                            wer[qw[1]]=child.html();
                            wer=JSON.stringify(wer);
                            localStorage.setItem(qw[0],wer);
                            document.querySelector('#total-'+($(this).parent()[0].id)).innerHTML=val*parseInt(child.html());
                            sans();
                          });

                          $(".qt-minus").click(function(){

                            child = $(this).parent().children(".qt");

                            if(parseInt(child.html()) > 1) {
                              child.html(parseInt(child.html()) - 1);
                            }

                             var child = $(this).parent().children(".qt");
                            var qw=($(this).parent()[0].id).split("-");
                            var wer=localStorage.getItem(qw[0]);
                            var pr=localStorage.getItem(qw[0]+'-item');
                            pr=JSON.parse(pr);
//                            console.log(pr);
                            val=parseInt(pr[qw[1]]);
                            wer=JSON.parse(wer);
                            wer[qw[1]]=child.html();
                            wer=JSON.stringify(wer);
                            localStorage.setItem(qw[0],wer);
                            document.querySelector('#total-'+($(this).parent()[0].id)).innerHTML=val*parseInt(child.html());
                            sans();

                          });
                    }
                });


           }

           }
           myfunc();
      }
 }




//function total()