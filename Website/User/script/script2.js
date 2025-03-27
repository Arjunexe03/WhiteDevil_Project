import { initializeApp } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-app.js";
import { getDatabase, ref, push, set } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-database.js";
  
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

const app = initializeApp(firebaseConfig);
const database = getDatabase(app);

console.log("Firebase initialized:", app);


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

document.getElementById("problemForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const name = document.getElementById("name").value.trim();
    const phoneNumber = document.getElementById("phoneNumber").value.trim();
    const address = document.getElementById("address").value.trim();
    const problem = document.getElementById("problem").value.trim();

    if (!name || !phoneNumber || !address || !problem) {
        alert("Please fill all fields!");
        return;
    }

    const problemData = { name, phoneNumber, address, problem };
    console.log("Submitting:", problemData);

    addProblem(problemData, (success) => {
        if (success) {
            alert("Problem submitted successfully!");
            document.getElementById("problemForm").reset(); 
        } else {
            alert("Failed to submit problem. Check the console for errors.");
        }
    });
});
