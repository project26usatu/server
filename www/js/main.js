//получение json с тарифами и запись в форму (адрес api с параметром id таблицы)
function importJsonToEditForm(url){
    $.getJSON(url, function(data) {
    	const ratesArray = [];
        $.each(data, function(key, val) {

         ratesArray.push(val);

     });
        //в ratesArray[2] находится запрашиваемый у api объект (responseBody)
        let jsonWithData = ratesArray[2];
        $('.single__rate__price').val(jsonWithData.single_rate_price);
        $('.daily__rate__price').val(jsonWithData.daily_rate_price);
        $('.night__rate__price').val(jsonWithData.night_rate_price);

        $('.peak__zone__rate__price').val(jsonWithData.peak_zone_rate_price);
        $('.semipeak__zone__rate__price').val(jsonWithData.semipeak_zone_rate_price);
        $('.night__zone__rate__price').val(jsonWithData.night_zone_rate_price);
    });
}
//получение json с тарифами и запись на страницу с информацией о тарифах (адрес api с параметром id таблицы)
function importJsonToInfoPage(url){
    $.getJSON(url, function(data) {
        const ratesArray = [];
        $.each(data, function(key, val) {

            ratesArray.push(val);

        });
        //в ratesArray[2] находится запрашиваемый у api объект
        let jsonWithData = ratesArray[2];
        $('.single__rate__price').html(jsonWithData.single_rate_price);
        $('.daily__rate__price').html(jsonWithData.daily_rate_price);
        $('.night__rate__price').html(jsonWithData.night_rate_price);

        $('.peak__zone__rate__price').html(jsonWithData.peak_zone_rate_price);
        $('.semipeak__zone__rate__price').html(jsonWithData.semipeak_zone_rate_price);
        $('.night__zone__rate__price').html(jsonWithData.night_zone_rate_price);
    });
}
//вызов importJsonToEditForm со сформированными параметрами
function getDataForEditForm(){
    let ratesId = $(".rates__selector").val();
    let ratesGetterUrl = '/api/get_prices?rates_set_id=' + ratesId;
    importJsonToEditForm(ratesGetterUrl);
}
//вызов importJsonToInfoPage со сформированными параметрами
function getDataForInfoPage(){
    let ratesId = $("#table_selector").val();
    let ratesGetterUrl = '/api/get_prices?rates_set_id=' + ratesId;
    importJsonToInfoPage(ratesGetterUrl);
}
//проверка пустого значения (переменная)
function isEmpty(value){
    return (value == null || value.length === 0);
}
//генератор ошибки ввода
function throwUserInputError(){
    let errorMessage = "Check if all required parameters specified";

    throw new Error(errorMessage);
    return 0;
}
//генератор ошибки доступа
function throwAccessDeniedError(){
    let errorMessage = "Access Denied";

    throw new Error(errorMessage);
    return 0;
}
//получить значение куки (имя атрибута)
function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i <ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
  }
  if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
  }
}
return "";
}
//удалить куки с атрибутом token
function deleteCookie(){
    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

function getval(token) {
    let queryUrl = "/api/get_user_info?token=" + token;
    jQuery.getJSON(queryUrl, function(data) {
        // You have to use "data" here
        alert(data['responseBody'].avg.value);
    });
}
//получить всю информацию о пользователе по его токену
function getUserInfo(token){

    let userData = null;
    let queryUrl = "/api/get_user_info?token=" + token;

    $.ajax({
      url: queryUrl,
      async: false,
      dataType: 'json',
      success: function (json) {
        userData = json.responseBody;
      },
      error: function (error) {
        alert ("При получении данных произошла ошибка");
        //возможно, проблема с куками, так что удаляем
        deleteCookie();
      }
    });

    return userData;
}
