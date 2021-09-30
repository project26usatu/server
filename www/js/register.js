//проверка полей "пароль" и "повторите пароль", возвращает true, если строки одинаковы
function checkPasswords(){
	let password= $(".register__password__input").val();
	let repeatedPassword = $(".register__repeat__password__input").val();

	isEmpty(password.trim()) && throwUserInputError();
    isEmpty(repeatedPassword.trim()) && throwUserInputError();

    if (password == repeatedPassword) {
        return true;
    } else {
        alert ("Пароли не совпадают");
    return false;
  }
}
//проверка и отправка данных из формы POST методом в /api/register
function register(){

	let username = $(".register__username__input").val();
	let fullName= $(".register__fullname__input").val();
	let email = $(".register__email__input").val();
	let password= $(".register__password__input").val();

    //проверяем пустые поля, обрезав пробелы по краям
    isEmpty(username.trim()) && throwUserInputError();
    isEmpty(fullName.trim()) && throwUserInputError();
    isEmpty(email.trim()) && throwUserInputError();

    //отправляем запрос
    $.ajax({
    	url: '/api/register',     
    	type: 'POST',
    	data : {
    		username : username,
    		email: email,
    		full_name: fullName,
    		password: password
    	},
    	dataType: 'json',                   
    	success: function(json)
    	{
            //регистрация успешна
            userData = json.responseBody;
            setCookie("token", userData.apiToken);
            console.log(userData.username);
            setCookie("username", userData.username);
            //перебрасываем на главную
            window.location.replace("../");
      	},
      	statusCode: {
      		409: function(error) {
      			//Занятый логин или email (при ошибке 409)
      			//Получаем ответ сервера в JSON
      			let json = error.responseJSON;

            alert(json.errorMessage);
          }
        },
      })

}
//проверка авторизации. если юзер авторизован, отправить его на главную
let tokenCookie = getCookie("token"); 
if (tokenCookie) {
    alert("В данный момент вы авторизованы. Вам необходимо выйти из аккаунта");
    window.location.replace("../");
}
//слушатель кнопки "отправить"
$('.register__submit__input').click(function() {
        let passwordsAreEqual = checkPasswords();
        if (passwordsAreEqual) register();
});