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
      const html = `
        <div>Logged in as ${user.email}</div>
        <div>Name: ${doc.data().name}</div>
        <div>Mobile No: ${doc.data().mobileno}</div>
      `;
      accountDetails.innerHTML = html;
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