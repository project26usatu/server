//обновление тарифа, формируем данные и отправляем запрос
function updateRates(tokenCookie){

    let ratesId = $(".rates__selector").val();

    let specifiedSingleRatePrice = $(".single__rate__price").val();

    let specifiedDailyRatePrice= $(".daily__rate__price").val();
    let specifiedNightRatePrice = $(".night__rate__price").val();

    let specifiedPeakZoneRatePrice = $(".peak__zone__rate__price").val();
    let specifiedSemipeakZoneRatePrice = $(".semipeak__zone__rate__price").val();
    let specifiedNightZoneRatePrice = $(".night__zone__rate__price").val();

    //проверяем пустые поля
    isEmpty(ratesId) && throwUserInputError();
    isEmpty(specifiedSingleRatePrice) && throwUserInputError();
    isEmpty(specifiedDailyRatePrice) && throwUserInputError();
    isEmpty(specifiedNightRatePrice) && throwUserInputError();
    isEmpty(specifiedPeakZoneRatePrice) && throwUserInputError();
    isEmpty(specifiedSemipeakZoneRatePrice) && throwUserInputError();
    isEmpty(specifiedNightZoneRatePrice) && throwUserInputError();

    $.ajax({
        url: '/api/edit_prices',     
        type: 'GET',
        data : {
            token : tokenCookie,
            rates_set_id: ratesId,
            single_rate_price : specifiedSingleRatePrice,
            daily_rate_price: specifiedDailyRatePrice,
            night_rate_price: specifiedNightRatePrice,
            peak_zone_rate_price: specifiedPeakZoneRatePrice,
            semipeak_zone_rate_price: specifiedSemipeakZoneRatePrice,
            night_zone_rate_price: specifiedNightZoneRatePrice,
            table_id: ratesId
        },
        dataType: 'json',                   
        success: function(data)
        {
      		//
        },
        statusCode: {
        	403: function (response) {
             alert("Доступ запрещён");
         }
     },
 })

}

//слушатели

$('.rates__selector').change(function() {
    getDataForEditForm();
});

let tokenCookie = getCookie("token"); 
console.log(tokenCookie);

$('#rates_form').change(function() {
    updateRates(tokenCookie);
    return false;
});

getDataForEditForm();