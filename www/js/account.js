//заполнение списка созданных пользователем отчётов
function formPdfReportsPlace(userId){
	$.ajax({ 
		type : 'GET', 
		url : '/api/get_user_reports', 
		async : false, 
		data : {
			ownerId : userId
		},
		dataType : 'json', 
		success : function(result){
			let reports_count = 1;
			$.each(result.responseBody, function(index, val){
				let uri = "https://project26.usatu.su"; 
				let docUrl;
				docUrl = uri + val;
				$(".pdf__reports__place").append('<li><a href=' + docUrl +'>Отчёт ' + reports_count + '</a></li>');
				reports_count++;
			});
		},
		error : function(error){
			$(".pdf__reports__place").append('<li>Пока вы не сделали ни одного отчёта</li>');
		}
	});
}

let tokenCookie = getCookie("token"); 
//получение информации о пользователе
let user;
if (tokenCookie) {
	user = getUserInfo(tokenCookie);
	$('.edit__username__input').val(user.username);
	$('.edit__fullname__input').val(user.fullName);
	$('.edit__email__input').val(user.email);
	$('.rates__selector').val(user.ratesSetId);
	$('.meter__mode__selector').val(user.meterMode);
} else {
	//редиректим если нет кук
	window.location.href = "../";
}
//слушатель нажатия кнопки внесения изменений
$('.edit__account__button').click({token: tokenCookie}, function(event) { //https://stackoverflow.com/questions/3273350/jquerys-click-pass-parameters-to-user-function
	let username = $('.edit__username__input').val();
	let fullName = $('.edit__fullname__input').val();
	let email = $('.edit__email__input').val();
	let ratesId = parseInt($('.rates__selector').val());
	let meterMode = parseInt($('.meter__mode__selector').val());

	if (username === "" || fullName === "" || email === "" || ratesId === 0 || meterMode === 0){
		alert ("Значения полей не могут быть пустыми")
	} else {
		$.ajax({
			url: '/api/update_user',     
			type: 'POST',
			data : {
				token : event.data.token,
				email : email,
				fullName : fullName,
				meterMode : meterMode,
				ratesId : ratesId
			},
			dataType: 'json',
			success: function(data)
			{
				alert("Данные обновлены");
			},
			statusCode: {
				404: function (response) {
					alert("Не найден пользователь для редактирования");
				},
				500: function (response) {
					alert("При обновлении произошла ошибка");
				}
			},
		})

	}
});
//подгружаем список pdf отчетов пользователя
formPdfReportsPlace(user.id);
//если админ - открываем блок с админскими функциями
if (user.groupId === 1) $('.admin__zone').show();