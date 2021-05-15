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

//слушатели (обновление результатов при взаимодействии):
//выпадающее меню
$('.rates__selector').change(function() {
	writeRatesToHiddenTags();

});
//кнопка рассчёта
$('.calculate__button').click(function() {
	let ratesId = $('.rates__selector').val();
	if (ratesId == 0){
		alert ("Выберите подходящую категорию тарифов")
	} else {
		updateSingleModeForms();
		updateDualModeForms();
		updateTrialModeForms();
	}
});