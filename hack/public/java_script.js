

  // Initialize Firebase
  var config = {
    apiKey: "AIzaSyDQFEaP7UHTZ5YU8tTf2ATatw4hou5i7Z8",
    authDomain: "helpthehomeless-bc93a.firebaseapp.com",
    databaseURL: "https://helpthehomeless-bc93a.firebaseio.com",
    storageBucket: "helpthehomeless-bc93a.appspot.com",
    messagingSenderId: "732959824565"
  };
  firebase.initializeApp(config);

  var bigOne = document.getElementById('bigOne');
  var dbRef = firebase.database().ref().child('text');
  dbRef.on('value', snap => bigOne.innerText = snap.val());


 


