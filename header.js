const loggedOutLinks = document.querySelectorAll('.logged-out');
const loggedInLinks = document.querySelectorAll('.logged-in');
const accountDetails = document.querySelector('.account-details');
const create = document.querySelector('.create');
const view = document.querySelector('.view');

const setupUI = (user) => {
  if (user) {
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
            create.style.display='block';
            view.style.display='none';
        }
        else
        {
            create.style.display='none';
            view.style.display='block';
        }
    });
    loggedInLinks.forEach(item => item.style.display = 'block');
    loggedOutLinks.forEach(item => item.style.display = 'none');
  } else {
    // clear account info
    accountDetails.innerHTML = '';
    // toggle user elements
    create.style.display='none';
    view.style.display='none';
    loggedInLinks.forEach(item => item.style.display = 'none');
    loggedOutLinks.forEach(item => item.style.display = 'block');
  }
};
  