let tokenCookie = getCookie("token"); 

let user;
let userLoggedIn = false;
if (tokenCookie) {
	userLoggedIn = true;
	$("#login_link").attr("href", "account/logout.html");
	$("#login_link").html("(Выйти)");
	$("#registration_link").hide();
	$("#account_link").attr("href", "account/main.html");
	user = getUserInfo(tokenCookie);
	$('#username').text(user.username);
} else {
	$("#logout_link").change( function(){
		alert ("")
	});
}
