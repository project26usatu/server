//проверка и отправка пароля из формы POST методом в /api/login
function login(){

    let username = $(".login__username__input").val();
    let password= $(".login__password__input").val();

    //проверяем пустые поля, обрезав пробелы по краям
    isEmpty(username.trim()) && throwUserInputError();
    isEmpty(username.trim()) && throwUserInputError();

    //отправляем запрос
    $.ajax({
        url: '/api/login',     
        type: 'POST',
        data : {
            username : username,
            password: password
        },
        dataType: 'json',                   
        success: function(json)
        {
      		//логин успешен
            userData = json.responseBody;
            setCookie("token", userData.apiToken);
            console.log(userData.username);
            setCookie("username", userData.username);
            //перебрасываем на главную
            window.location.replace("../");
        },
        statusCode: {
        	401: function(data) {
                //Неудачный логин (при ошибке 401)
                alert("Неправильный логин/пароль");
            }
        },
    })

}
//если юзер уже авторизован, отправить его на главную
let tokenCookie = getCookie("token"); 
if (tokenCookie) {
    alert("В данный момент вы уже авторизованы. Вам необходимо выйти из аккаунта");
    window.location.replace("../");
}
//слушатель кнопки "отправить"
$('.login__submit__input').click(function() {
    login();
});
