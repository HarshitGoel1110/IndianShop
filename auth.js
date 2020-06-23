// listen for auth status changes
auth.onAuthStateChanged(user => {
  if (user) {
      setupUI(user);
  } else {
    setupUI();
  }
})

// create new store
const createForm = document.querySelector('#create-form');
createForm.addEventListener('submit', (e) => {
e.preventDefault();
const store = {
  name: createForm['storename'].value,
  firstname: createForm['firstname'].value,
  lastname: createForm['lastname'].value,
  address: createForm['address'].value,
  city: createForm['city'].value,
  country: createForm['country'].value,
  state: createForm['state'].value,
  pincode: createForm['pincode'].value,
  mobile: createForm['mobileno'].value,
  email: createForm['email'].value,
  isreg: createForm['isreg'].value
}
db.collection('shop').add(store).then(() => {
  createForm.reset();
}).catch(err => {
  console.log(err.message);
});
});

// signup
const signupForm = document.querySelector('#signup-form');
signupForm.addEventListener('submit', (e) => {
  e.preventDefault();
  
  // get user info
  const email = signupForm['signup-email'].value;
  const password = signupForm['signup-password'].value;

  // sign up the user & add firestore data
  auth.createUserWithEmailAndPassword(email, password).then(cred => {
    return db.collection('users').doc(cred.user.uid).set({
      name: signupForm['signup-name'].value,
      mobileno: signupForm['signup-mobileno'].value
    });
  }).then(() => {
    // close the signup modal & reset form
    signupForm.reset();
  });
});



// logout
const logout = document.querySelector('#logout');
logout.addEventListener('click', (e) => {
e.preventDefault();
auth.signOut().then(() => {
  console.log('user signed out');
})
});


// login
const loginForm = document.querySelector('#login-form');
loginForm.addEventListener('submit', (e) => {
e.preventDefault();

// get user info
const email = loginForm['login-email'].value;
const password = loginForm['login-password'].value;

// log the user in
auth.signInWithEmailAndPassword(email, password).then((cred) => {
  
  loginForm.reset();
});

});