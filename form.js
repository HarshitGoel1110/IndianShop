const form = document.querySelector('form');

form.addEventListener('submit', e =>{
    e.preventDefault();
    const store = {
        name: form.shop.value,
        firstname: form.First_Name.value,
        lastname: form.Last_Name.value,
        address: form.Address.value,
        apartment: form.Apartment.value,
        city: form.City.value,
        country: form.sel1.value,
        state: form.sel2.value,
        pincode: form.Pin_code.value,
        mobile: form.mobile_no.value,
        email: form.email.value,
        isreg: form.checkbox.value
    }

db.collection('shop').add(store).then(() => {
    console.log('shop created');
}).catch(err => {
    console.log(err);
});

});
