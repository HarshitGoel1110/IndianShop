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


db.collection('shop').doc(firebase.auth().currentUser.uid).set({
  name: createForm['storename'].value,
  address: createForm['address'].value,
  city: createForm['city'].value,
  pincode: createForm['pincode'].value,
  mobile: createForm['mobileno'].value,
  email: createForm['email'].value
 }).then(() => {
    db.collection('users').doc(firebase.auth().currentUser.uid).update({
    shop:true
    });
    location.reload();
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
    firebase.auth().currentUser.sendEmailVerification().then(function() {
      // Email sent.
      alert("Signup Successful !!!");
      console.log("sent");
    });
    db.collection('users').doc(cred.user.uid).set({
      name: signupForm['signup-name'].value,
      mobileno: signupForm['signup-mobileno'].value,
      address: signupForm['signup-address'].value,
      shop: false
    });
    auth.signOut();
  }).then(() => {
    // close the signup modal & reset form
    location.reload();
    signupForm.reset();
  }).catch(err => {
    alert(err.message);
  });
});



// logout
const logout = document.querySelector('#logout');
logout.addEventListener('click', (e) => {
e.preventDefault();
auth.signOut().then(() => {
  window.location.replace("index.html");
  alert('Successfully, logged out !!!');
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
auth.signInWithEmailAndPassword(email, password).then((user) => {
  if (!firebase.auth().currentUser.emailVerified)
  {
    auth.signOut();
    alert("Verify your email first and try logging in...");
  }
  loginForm.reset();
}).catch(err => {
  alert(err.message);
});

});