import { initializeApp } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-app.js";
import { getDatabase, ref, onValue } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-database.js";

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
const problemsRef = ref(database, "problems");

const categories = {
    "Network Issue": ["internet", "wifi", "slow connection", "router", "no signal", "network"],
    "Electricity Issue": ["electricity", "power cut", "voltage", "blackout", "transformer", "electric fault"],
    "Water Supply Issue": ["water", "plumbing", "leak", "pipeline", "no water", "pipe broken"],
    "Road Damage": ["pothole", "road crack", "construction", "bad road", "repair", "roadblock"],
    "Garbage Collection": ["trash", "garbage", "waste", "cleaning", "dump", "bad smell"]
};


function cosineSimilarity(text, categoryKeywords) {
    const words = text.toLowerCase().split(/\W+/); 
    let score = 0;
    
    categoryKeywords.forEach(word => {
        if (words.includes(word)) score++;
    });

    return score / Math.sqrt(words.length * categoryKeywords.length); 
}

function categorizeProblem(problemText) {
    let bestCategory = "Uncategorized";
    let bestScore = 0;

    for (const [category, keywords] of Object.entries(categories)) {
        const score = cosineSimilarity(problemText, keywords);
        if (score > bestScore) {
            bestScore = score;
            bestCategory = category;
        }
    }

    return bestCategory;
}

onValue(problemsRef, (snapshot) => {
    if (snapshot.exists()) {
        const data = snapshot.val();
        console.log("Fetched Data:", data);
        displayReports(data);
    } else {
        console.log("No reports found.");
    }
});

function displayReports(data) {
    let tableBody = document.getElementById("reportsTableBody");
    tableBody.innerHTML = ""; 

    Object.entries(data).forEach(([key, report]) => {
        let category = categorizeProblem(report.problem);
        let row = `<tr>
            <td>${report.name}</td>
            <td>${report.phoneNumber}</td>
            <td>${report.address}</td>
            <td>${report.problem}</td>
            <td>${category}</td>
        </tr>`;
        tableBody.innerHTML += row;
    });
}