//берем куки токена
let tokenCookie = getCookie("token"); 
//если куки токена есть - удаляем
if (tokenCookie) deleteCookie();
//отправляем на главную
window.location.replace("../");