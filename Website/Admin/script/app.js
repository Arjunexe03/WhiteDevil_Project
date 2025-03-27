import { initializeApp } from "https://www.gstatic.com/firebasejs/11.5.0/firebase-app.js";
  import { getAuth, signInWithEmailAndPassword, createUserWithEmailAndPassword, signOut } from "https://www.gstatic.com/firebasejs/11.5.0/firebase-auth.js";
  
const firebaseConfig = {
    apiKey: "AIzaSyCAH3c2EbIL-zeLaKQwb3SmajHjMIRgDwY",
    authDomain: "igsr-white-devil.firebaseapp.com",
    databaseURL: "https://igsr-white-devil-default-rtdb.firebaseio.com",
    projectId: "igsr-white-devil",
    storageBucket: "igsr-white-devil.firebasestorage.app",
    messagingSenderId: "802763677183",
    appId: "1:802763677183:web:3fd1d1acc9f51eb9545bca",
    measurementId: "G-LG5GYKKTQV"
};

let loginemail=document.querySelector("#loginEmail")
let loginpassword = document.querySelector("#loginPassword")
let loginbtn = document.querySelector("#loginBtn")

// if('anirudhkhemriya@gmail.com'=== loginemail && loginpassword === 98765){
//     loginbtn.onclick=()=>{
//         console.log("yeah")
//         location.href="index.html";
//     }
// }
loginbtn.onclick=()=>{
    location.href="index.html"
}


function addProblem(problem, callback) {
    const problemsRef = ref(database, "problems"); 
    const newProblemRef = push(problemsRef); 

    set(newProblemRef, problem)
        .then(() => callback(true))
        .catch((error) => {
            console.error("Error saving data:", error);
            callback(false);
        });
}