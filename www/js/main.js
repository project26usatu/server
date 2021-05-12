//получение json с тарифами и запись в форму (адрес api с параметром id таблицы)
function importJsonToEditForm(url){
    $.getJSON(url, function(data) {
    	const ratesArray = [];
        $.each(data, function(key, val) {

         ratesArray.push(val);

     });

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
    let tableId = $("#table_selector").val();
    let ratesGetterUrl = '/api/get_prices?table_id=' + tableId;
    importJsonToEditForm(ratesGetterUrl);
}
//вызов importJsonToInfoPage со сформированными параметрами
function getDataForInfoPage(){
    let tableId = $("#table_selector").val();
    let ratesGetterUrl = '/api/get_prices?table_id=' + tableId;
    importJsonToInfoPage(ratesGetterUrl);
}

//TODO: Restict negative numbers input
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
//обновление тарифа, формируем данные и отправляем запрос
function updateRates(tokenCookie){

    let specifiedSingleRatePrice = $(".single__rate__price").val();

    let specifiedDailyRatePrice= $(".daily__rate__price").val();
    let specifiedNightRatePrice = $(".night__rate__price").val();

    let specifiedPeakZoneRatePrice = $(".peak__zone__rate__price").val();
    let specifiedSemipeakZoneRatePrice = $(".semipeak__zone__rate__price").val();
    let specifiedNightZoneRatePrice = $(".night__zone__rate__price").val();

    let tableId = $("#table_selector").val();
    //проверяем пустые поля
    isEmpty(specifiedSingleRatePrice) && throwUserInputError();
    isEmpty(specifiedDailyRatePrice) && throwUserInputError();
    isEmpty(specifiedNightRatePrice) && throwUserInputError();
    isEmpty(specifiedPeakZoneRatePrice) && throwUserInputError();
    isEmpty(specifiedSemipeakZoneRatePrice) && throwUserInputError();
    isEmpty(specifiedNightZoneRatePrice) && throwUserInputError();

    //TODO: add isNumeric check

    $.ajax({
        url: '/api/edit_prices',     
        type: 'GET',
        data : {
            token : tokenCookie,
            single_rate_price : specifiedSingleRatePrice,
            daily_rate_price: specifiedDailyRatePrice,
            night_rate_price: specifiedNightRatePrice,
            peak_zone_rate_price: specifiedPeakZoneRatePrice,
            semipeak_zone_rate_price: specifiedSemipeakZoneRatePrice,
            night_zone_rate_price: specifiedNightZoneRatePrice,
            table_id: tableId
        },
        dataType: 'json',                   
        success: function(data)
        {
      //alert ("Edit successful");
  } 

})

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
//получить имя пользователя по его токену
function getUsername(url, token){
    $.getJSON(url + "?token=" + token, function(data) {
        const ratesArray = [];
        $.each(data, function(key, val) {

            ratesArray.push(val);

        });

        let jsonWithData = ratesArray[2];
        $('#username').text(jsonWithData.username);
    });
}