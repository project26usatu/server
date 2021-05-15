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
        success: function(data)
        {
      		// Логин успешен - перебрасываем на главную
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

//слушатель кнопки "отправить"
$('.login__submit__input').click(function() {
    login();
});