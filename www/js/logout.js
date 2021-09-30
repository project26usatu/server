//берем куки токена и никнейма
let tokenCookie = getCookie("token"); 
let usernameCookie = getCookie("username"); 
//если куки есть — удаляем
if (tokenCookie) eraseCookie("token");
if (usernameCookie) eraseCookie("username");
//отправляем на главную
window.location.replace("../");