//округление числа (число, степень округления)
function roundNumber(num, scale) {
	if(!("" + num).includes("e")) {
		return +(Math.round(num + "e+" + scale)  + "e-" + scale);
	} else {
		var arr = ("" + num).split("e");
		var sig = ""
		if(+arr[1] + scale > 0) {
			sig = "+";
		}
		return +(Math.round(+arr[0] + "e" + sig + (+arr[1] + scale)) + "e-" + scale);
	}
}
//запись тарифов в скрытые теги <p> с указанием нужной таблицы и адреса api
function writeRatesToHiddenTags(){
	//получение id таблицы из выпадающего меню
	let ratesId = $(".rates__selector").val();
	let ratesGetterUrl = '/api/get_prices?rates_set_id=' + ratesId;
	importJsonToInfoPage(ratesGetterUrl);
}
//инициализация(обновление) расчётов блока с одноставочным тарифом
function updateSingleModeForms(){
	let result = 0;

	let singleRatePrice = parseFloat($('.single__rate__price').html());

	let prevMeterReading = parseFloat($('.prev__single__rate__price').val());
	let currMeterReading = parseFloat($('.curr__single__rate__price').val());
	if (prevMeterReading > currMeterReading) {
		alert("Предыдущие показания не могут быть больше текущих!");
	} else {
		let consuptionInKwt = currMeterReading - prevMeterReading;
		let result = consuptionInKwt * singleRatePrice;
		result = roundNumber(result, 2);
		$('.result__single__mode').html(result);
	}
}
//инициализация(обновление) расчётов блока с двухставочным тарифом
function updateDualModeForms(){
	let result = 0;

	let dailyRatePrice = parseFloat($('.daily__rate__price').html());
	let nightRatePrice = parseFloat($('.night__rate__price').html());


	let prevDayMeterReading = parseFloat($('.prev__daily__rate__price').val());
	let currDayMeterReading = parseFloat($('.curr__daily__rate__price').val());
	let prevNightMeterReading = parseFloat($('.prev__night__rate__price').val());
	let currNightMeterReading = parseFloat($('.curr__night__rate__price').val());
	if ((prevDayMeterReading > currDayMeterReading) || (prevNightMeterReading > currNightMeterReading)) {
		alert("Предыдущие показания не могут быть больше текущих!");
	} else {
		let dailyConsuptionInKwt = currDayMeterReading - prevDayMeterReading;
		let nightConsuptionInKwt = currNightMeterReading - prevNightMeterReading;
		let result = (dailyConsuptionInKwt * dailyRatePrice) + (nightConsuptionInKwt * nightRatePrice);
		result = roundNumber(result, 2);
		$('.result__dual__mode').html(result);
	}
}
//инициализация(обновление) расчётов блока с трехставочным тарифом
function updateTrialModeForms(){
	//let result = 0;

	let peakZoneRatePrice = parseFloat($('.peak__zone__rate__price').html());
	let semipeakZoneRatePrice = parseFloat($('.semipeak__zone__rate__price').html());
	let nightZoneRatePrice = parseFloat($('.night__zone__rate__price').html());


	let prevPeakZoneMeterReading = parseFloat($('.prev__peak__zone__rate__price').val());
	let currPeakZoneMeterReading = parseFloat($('.curr__peak__zone__rate__price').val());
	let prevSemipeakZoneMeterReading = parseFloat($('.prev__semipeak__zone__rate__price').val());
	let currSemipeakZoneMeterReading = parseFloat($('.curr__semipeak__zone__rate__price').val());
	let prevNightZoneMeterReading = parseFloat($('.prev__night__zone__rate__price').val());
	let currNightZoneMeterReading = parseFloat($('.curr__night__zone__rate__price').val());

	if ((prevPeakZoneMeterReading > currPeakZoneMeterReading) || (prevSemipeakZoneMeterReading > currSemipeakZoneMeterReading) || (prevNightZoneMeterReading > currNightZoneMeterReading)) {
		alert("Предыдущие показания не могут быть больше текущих!");
	} else {
		let peakZoneConsuptionInKwt = currPeakZoneMeterReading - prevPeakZoneMeterReading;
		let SemipeakZoneConsuptionInKwt = currSemipeakZoneMeterReading - prevSemipeakZoneMeterReading;
		let nightZoneConsuptionInKwt = currNightZoneMeterReading - prevNightZoneMeterReading;
		let result = (peakZoneConsuptionInKwt * peakZoneRatePrice) + (SemipeakZoneConsuptionInKwt * semipeakZoneRatePrice) + (nightZoneConsuptionInKwt * nightZoneRatePrice);
		result = roundNumber(result, 2);
		$('.result__trial__mode').html(result);
	}
}
//генерация PDF для однорежимных счётчиков
function createPdfIfSingleMode(tokenCookie){

	let ratesId = $(".rates__selector").val();

	let firstMeterPrevReadings = $(".prev__single__rate__price").val();
	let firstMeterCurrReadings = $(".curr__single__rate__price").val();
	let consumptionByFirstMeter = firstMeterCurrReadings - firstMeterPrevReadings;
	let firstMeterAmount = $(".result__single__mode").html();
	let totalAmount = $(".result__single__mode").html();

	$.ajax({
		url: '/api/generate_pdf',     
		type: 'GET',
		data : {
            //token : tokenCookie,
            meterMode : 1,
            ratesId : ratesId,
            firstMeterPrevReadings : firstMeterPrevReadings,
            firstMeterCurrReadings : firstMeterCurrReadings,
            consumptionByFirstMeter : consumptionByFirstMeter,
            firstMeterAmount : firstMeterAmount,
            totalAmount : totalAmount,
        },
        dataType: 'json',                   
        success: function(data)
        {
        	let pdfDocDownloadLink = "https://project26.usatu.su" + data.responseBody;
        	alert("Отчёт сформирован. Нажмите ОК для просмотра");
        	window.open(pdfDocDownloadLink, '_blank');
        },
        statusCode: {
        	400: function (response) {
        		alert("Недопустимые данные");
        	},
        	500: function (response) {
        		alert("Произошла ошибка");
        	}
        },
    })

}
//генерация PDF для двухрежимных счётчиков
function createPdfIfDualMode(tokenCookie){

	let ratesId = $(".rates__selector").val();

	let firstMeterPrevReadings = $(".prev__daily__rate__price").val();
	let firstMeterCurrReadings = $(".curr__daily__rate__price").val();
	let secondMeterPrevReadings = $(".prev__night__rate__price").val();
	let secondMeterCurrReadings = $(".curr__night__rate__price").val();
	let consumptionByFirstMeter = roundNumber(firstMeterCurrReadings - firstMeterPrevReadings, 2);
	let consumptionBySecondMeter = roundNumber(secondMeterCurrReadings - secondMeterPrevReadings, 2);
	
	let dailyRatePrice = parseFloat($('.daily__rate__price').html());
	let nightRatePrice = parseFloat($('.night__rate__price').html());
	
	let firstMeterAmount = roundNumber(consumptionByFirstMeter * dailyRatePrice, 2);
	let secondMeterAmount = roundNumber(consumptionBySecondMeter * nightRatePrice, 2);
	let totalAmount = roundNumber(firstMeterAmount + secondMeterAmount, 2);
	

	$.ajax({
		url: '/api/generate_pdf',     
		type: 'GET',
		data : {
            //token : tokenCookie,
            meterMode : 2,
            ratesId : ratesId,
            firstMeterPrevReadings : firstMeterPrevReadings,
            firstMeterCurrReadings : firstMeterCurrReadings,
            consumptionByFirstMeter : consumptionByFirstMeter,
            firstMeterAmount : firstMeterAmount,
            totalAmount : totalAmount,
            secondMeterPrevReadings : secondMeterPrevReadings,
			secondMeterCurrReadings : secondMeterCurrReadings,
			consumptionBySecondMeter : consumptionBySecondMeter,
			secondMeterAmount : secondMeterAmount
        },
        dataType: 'json',                   
        success: function(data)
        {
        	let pdfDocDownloadLink = "https://project26.usatu.su" + data.responseBody;
        	alert("Отчёт сформирован. Нажмите ОК для просмотра");
        	window.open(pdfDocDownloadLink, '_blank');
        },
        statusCode: {
        	400: function (response) {
        		alert("Недопустимые данные");
        	},
        	500: function (response) {
        		alert("Произошла ошибка");
        	}
        },
    })

}
//генерация PDF для трёхрежимных счётчиков
function createPdfIfTrialMode(tokenCookie){

	let ratesId = $(".rates__selector").val();

	let firstMeterPrevReadings = $(".prev__peak__zone__rate__price").val();
	let firstMeterCurrReadings = $(".curr__peak__zone__rate__price").val();
	let secondMeterPrevReadings = $(".prev__semipeak__zone__rate__price").val();
	let secondMeterCurrReadings = $(".curr__semipeak__zone__rate__price").val();
	let thirdMeterPrevReadings = $(".prev__night__zone__rate__price").val();
	let thirdMeterCurrReadings = $(".curr__night__zone__rate__price").val();
	let consumptionByFirstMeter = roundNumber(firstMeterCurrReadings - firstMeterPrevReadings, 2);
	let consumptionBySecondMeter = roundNumber(secondMeterCurrReadings - secondMeterPrevReadings, 2);
	let consumptionByThirdMeter = roundNumber(thirdMeterCurrReadings - thirdMeterPrevReadings, 2);
	
	let peakZoneRatePrice = parseFloat($('.peak__zone__rate__price').html());
	let semipeakZoneRatePrice = parseFloat($('.semipeak__zone__rate__price').html());
	let nightZoneRatePrice = parseFloat($('.night__zone__rate__price').html());
	
	let firstMeterAmount = roundNumber(consumptionByFirstMeter * peakZoneRatePrice, 2);
	let secondMeterAmount = roundNumber(consumptionBySecondMeter * semipeakZoneRatePrice, 2);
	let thirdMeterAmount = roundNumber(consumptionByThirdMeter * nightZoneRatePrice, 2);
	let totalAmount = roundNumber(firstMeterAmount + secondMeterAmount + thirdMeterAmount, 2)

	$.ajax({
		url: '/api/generate_pdf',     
		type: 'GET',
		data : {
            //token : tokenCookie,
            meterMode : 3,
            ratesId : ratesId,
            firstMeterPrevReadings : firstMeterPrevReadings,
            firstMeterCurrReadings : firstMeterCurrReadings,
            consumptionByFirstMeter : consumptionByFirstMeter,
            firstMeterAmount : firstMeterAmount,
            totalAmount : totalAmount,
            secondMeterPrevReadings : secondMeterPrevReadings,
			secondMeterCurrReadings : secondMeterCurrReadings,
			consumptionBySecondMeter : consumptionBySecondMeter,
			secondMeterAmount : secondMeterAmount,
			thirdMeterPrevReadings : thirdMeterPrevReadings,
			thirdMeterCurrReadings : thirdMeterCurrReadings,
			consumptionByThirdMeter: consumptionByThirdMeter,
			thirdMeterAmount : thirdMeterAmount
        },
        dataType: 'json',                   
        success: function(data)
        {
        	let pdfDocDownloadLink = "https://project26.usatu.su" + data.responseBody;
        	alert("Отчёт сформирован. Нажмите ОК для просмотра");
        	window.open(pdfDocDownloadLink, '_blank');
        },
        statusCode: {
        	400: function (response) {
        		alert("Недопустимые данные");
        	},
        	500: function (response) {
        		alert("Произошла ошибка");
        	}
        },
    })

}

let successCalculation = false;

//очистка форм
$('.calculation__form').find("select").val("0");
$('.calculation__form').find("input").val('');

//слушатели (обновление результатов при взаимодействии):
//выпадающее меню выбора тарифных ставок
$('.rates__selector').change(function() {
	writeRatesToHiddenTags();
	successCalculation = false;

});
//выпадающее меню выбора типа счётчика
$('.meter__mode__selector').change(function() {
	successCalculation = false;
	if ($(this).find(":selected").val() == 1) {
		$('.single__rates__div').show();
		$('.dual__rates__div').hide();
		$('.trial__rates__div').hide();
	}
	if ($(this).find(":selected").val() == 2) {
		$('.single__rates__div').hide();
		$('.dual__rates__div').show();
		$('.trial__rates__div').hide();
	} 
	if ($(this).find(":selected").val() == 3) {
		$('.single__rates__div').hide();
		$('.dual__rates__div').hide();
		$('.trial__rates__div').show();
	}
});
//кнопка расчёта
$('.calculate__button').click(function() {
	let ratesId = $('.rates__selector').val();
	let meterMode = $('.meter__mode__selector').val();
	if (ratesId == 0 || meterMode == 0){
		alert ("Выберите подходящую категорию тарифов и режим работы счётчика")
	} else {
		updateSingleModeForms();
		updateDualModeForms();
		updateTrialModeForms();
		successCalculation = true;
	}
});
//кнопка PDF отчёта
$('.report__button').click(function() {
	let ratesId = $('.rates__selector').val();
	let meterMode = $('.meter__mode__selector').val();
	if (successCalculation && meterMode == 1){
		createPdfIfSingleMode();
	} else if (successCalculation && meterMode == 2){
		createPdfIfDualMode();
	}  else if (successCalculation && meterMode == 3){
		createPdfIfTrialMode();
	} else {
		alert ("Сначала нужно произвести расчёт");
	}
});