let tokenCookie = getCookie("token"); 
let username = getCookie("username"); 

let user;
let userLoggedIn = false;
if (tokenCookie) {
	userLoggedIn = true;
	$("#login_link").attr("href", "account/logout");
	$("#login_link").html("(Выйти)");
	$("#registration_link").hide();
	$("#account_link").attr("href", "account/main");
	$('#username').text(username);
} else {
	$('#username').text("гость");
	$("#logout_link").change( function(){
		alert ("")
	});
}
