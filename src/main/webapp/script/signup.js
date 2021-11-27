var password1 = document.getElementById("password1");
var password2 = document.getElementById("password2");

password2.oninput = function(event) {
    if(password2.value != password1.value) {
        password2.setCustomValidity("Password don't match");
    } else {
        password2.setCustomValidity("")
    }
}