//берем куки токена
let tokenCookie = getCookie("token"); 
//если куки токена есть - удаляем
if (tokenCookie) eraseCookie("token");
//отправляем на главную
window.location.replace("../");