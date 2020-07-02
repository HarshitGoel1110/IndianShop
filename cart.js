
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

function emptyCart()
{
    if(document.querySelector('.modal-body').innerText=="")
    {
        document.querySelector('.modal-body').innerHTML=`<div class="Empty-Cart">Cart Empty</div>`;
    }

}

emptyCart();

var head=document.querySelector('.cart_head');
var bodyhtml='',html='';
var foothtml='',headhtml='';
var print={};
for (var key in w) {
      if (w.hasOwnProperty(key) && key!="size" && func(key)) {
//        html=html+" "+"shop"+" "+key;
          var name;
          var keyx;
          db.collection('shop').doc(key).get().then(doc=>{
                keyx=doc.id;
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
                 console.log("firstprint",keyx);
              print[keyx]=html;

          }).then(()=>{
                   var final;
                   console.log(w,keyx,name);
                   x=JSON.parse(w[keyx]);

    //           console.log(x,x.length);
               var price=0,total=0,count=0;
               var utr={};
               var name_product={};
               for(var key1 in x){
                    if (x.hasOwnProperty(key1)) {
                    var wer=keyx;
                    db.collection('shop/'+keyx+'/product').doc(key1).get().then(pro=>{
    //                html=`<div><h3>${pro.data().name}   Price:${pro.data().price}</h3><div>${pro.data().description}</div><br></div>`;
                    var x=JSON.parse(w[wer])
                     utr[pro.id]=pro.data().price;
//                    console.log(pro);
                    name_product[pro.id]=pro.data().name;
                    html=`
                    <table class="table table-image remove-${wer}" id='view-${wer}-${pro.id}'>
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

                    print[wer]+=html;
                    window.localStorage.setItem(wer+'-item',JSON.stringify(utr));
                    window.localStorage.setItem(wer+'-name',JSON.stringify(name_product));
                    }).then(()=>{


                        total+=price;
                        final=total;
                        count++;
                        console.log(count,wer,JSON.parse(w[wer]));
                        if(count==Object.keys(JSON.parse(w[wer])).length)
                        {
                            console.log(wer);
                            html=`
                            <div class="remove-${wer}">
                            <table class="table table-image">
                            <div class="d-flex justify-content-end">
                                <h5>Total: <span class="price text-success final-total" id='final-${wer}'>${total}</span></h5>
                            </div>
                            <div class="container ">
                            <a class="modal-open bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded m-4 float-right mr-16 buy_shop" id='buy-${wer}'>
                                Buy
                            </a>
                            </div>
                            </table>
                            </div>
                            `;

                            print[wer]+=html;
                            console.log("printyes",wer);
                            document.querySelector('.Empty-Cart').innerHTML='';
                            ad.innerHTML+=print[wer];
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
                                        emptyCart();
                                        console.log(wer);

                                    })
                           }
                           )



                            sans();
                            window.localStorage.setItem(wer+'-item',JSON.stringify(utr));
                            $('.buy_shop').click(function(){
                                var shop_id=this.id.split("-")[1];
                                console.log(shop_id,"clicked",auth.W);
                                var quantity=JSON.parse(localStorage.getItem(shop_id));
                                var name1=JSON.parse(localStorage.getItem(shop_id+'-name'));
                                var price1=JSON.parse(localStorage.getItem(shop_id+'-item'));
                                var qw={};

                                for(var key in quantity)
                                {
                                    l=[]
                                    l[0]=name1[key];
                                    l[1]=parseInt(quantity[key]);
                                    l[2]=parseInt(price1[key]);
                                    qw[key]=l;
                                }
                                var timestamp=JSON.stringify(Date.now());
                                   db.collection('shop/'+shop_id+'/purchased').doc(timestamp).set({
                                    user:auth.W,
                                    product:qw,
                                    status:false

                                   });

                                 db.collection('users/'+auth.W+'/bought').doc(timestamp).set({
                                    shop:shop_id,
                                    product:qw,
                                    delivered:false
                                 }
                                 );


                                 localStorage.removeItem(shop_id);
                                 localStorage.removeItem(shop_id+"-item");
                                 document.querySelectorAll('.remove-'+shop_id).forEach(doc=>{
                                                 console.log(doc);
                                                 doc.style.display='none';
                                                 });
                                 emptyCart();
                                 alert(`Product  Bought  from shop ${shop_id}`);
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

          })


      }
 }




//function total()