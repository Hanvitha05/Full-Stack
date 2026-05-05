const API = "http://localhost:8080/api/auth";

function saveToken(token){
    localStorage.setItem("token", token);
}

function getToken(){
    return localStorage.getItem("token");
}

function logout(){
    localStorage.clear();
    window.location = "login.html";
}

function protect(){
    if(!getToken()){
        window.location = "login.html";
    }
}