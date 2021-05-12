let tokenCookie = getCookie("token"); 
console.log(tokenCookie);

let userInfoUrl = '/api/get_user_info';
getUsername(userInfoUrl, tokenCookie);