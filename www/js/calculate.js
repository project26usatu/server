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
//получение json с тарифами и запись в скрытые теги <p> (адрес api, id таблицы)
function importJsonToInfoPage(url, tableId){
	$.getJSON(url + "?table_id=" + tableId, function(data) {
		const ratesArray = [];
		$.each(data, function(key, val) {

			ratesArray.push(val);

		});
		//в ratesArray[2] находится запрашиваемый у api объект
		let jsonWithData = ratesArray[2];
		$('.single_rate_price').html(jsonWithData.single_rate_price);
		$('.daily_rate_price').html(jsonWithData.daily_rate_price);
		$('.night_rate_price').html(jsonWithData.night_rate_price);

		$('.peak_zone_rate_price').html(jsonWithData.peak_zone_rate_price);
		$('.semipeak_zone_rate_price').html(jsonWithData.semipeak_zone_rate_price);
		$('.night_zone_rate_price').html(jsonWithData.night_zone_rate_price);
	});
}
//запись тарифов в скрытые теги <p> с указанием нужной таблицы и адреса api
function writeRatesToHiddenTags(){
	//получение id таблицы из выпадающего меню

	let tableId = $('.table_selector').val();

	importJsonToInfoPage('/api/get_prices', tableId);
}
//инициализация(обновление) расчётов блока с одноставочным тарифом
function updateSingleModeForms(){
	let singleRatePrice = parseFloat($('.single_rate_price').html());

	let prevMeterReading = parseFloat($('.prev_single_rate_price').val());
	let currMeterReading = parseFloat($('.curr_single_rate_price').val());
	if (prevMeterReading > currMeterReading) {
		alert("Предыдущие показания не могут быть больше текущих!");
	} else {
		let consuptionInKwt = currMeterReading - prevMeterReading;
		let result = consuptionInKwt * singleRatePrice;
		result = roundNumber(result, 2);
		$('.result_single_mode').html(result);
	}
}
//инициализация(обновление) расчётов блока с двухставочным тарифом
function updateDualModeForms(){
	let dailyRatePrice = parseFloat($('.daily_rate_price').html());
	let nightRatePrice = parseFloat($('.night_rate_price').html());


	let prevDayMeterReading = parseFloat($('.prev_daily_rate_price').val());
	let currDayMeterReading = parseFloat($('.curr_daily_rate_price').val());
	let prevNightMeterReading = parseFloat($('.prev_night_rate_price').val());
	let currNightMeterReading = parseFloat($('.curr_night_rate_price').val());
	if ((prevDayMeterReading > currDayMeterReading) || (prevNightMeterReading > currNightMeterReading)) {
		alert("Предыдущие показания не могут быть больше текущих!");
	} else {
		let dailyConsuptionInKwt = currDayMeterReading - prevDayMeterReading;
		let nightConsuptionInKwt = currNightMeterReading - prevNightMeterReading;
		let result = (dailyConsuptionInKwt * dailyRatePrice) + (nightConsuptionInKwt * nightRatePrice);
		result = roundNumber(result, 2);
		$('.result_dual_mode').html(result);
	}
}
//инициализация(обновление) расчётов блока с трехставочным тарифом
function updateTrialModeForms(){
	let peakZoneRatePrice = parseFloat($('.peak_zone_rate_price').html());
	let semipeakZoneRatePrice = parseFloat($('.semipeak_zone_rate_price').html());
	let nightZoneRatePrice = parseFloat($('.night_zone_rate_price').html());


	let prevPeakZoneMeterReading = parseFloat($('.prev_peak_zone_rate_price').val());
	let currPeakZoneMeterReading = parseFloat($('.curr_peak_zone_rate_price').val());
	let prevSemipeakZoneMeterReading = parseFloat($('.prev_semipeak_zone_rate_price').val());
	let currSemipeakZoneMeterReading = parseFloat($('.curr_semipeak_zone_rate_price').val());
	let prevNightZoneMeterReading = parseFloat($('.prev_night_zone_rate_price').val());
	let currNightZoneMeterReading = parseFloat($('.curr_night_zone_rate_price').val());

	if ((prevPeakZoneMeterReading > currPeakZoneMeterReading) || (prevSemipeakZoneMeterReading > currSemipeakZoneMeterReading) || (prevNightZoneMeterReading > currNightZoneMeterReading)) {
		alert("Предыдущие показания не могут быть больше текущих!");
	} else {
		let peakZoneConsuptionInKwt = currPeakZoneMeterReading - prevPeakZoneMeterReading;
		let SemipeakZoneConsuptionInKwt = currSemipeakZoneMeterReading - prevSemipeakZoneMeterReading;
		let nightZoneConsuptionInKwt = currNightZoneMeterReading - prevNightZoneMeterReading;
		let result = (peakZoneConsuptionInKwt * peakZoneRatePrice) + (SemipeakZoneConsuptionInKwt * semipeakZoneRatePrice) + (nightZoneConsuptionInKwt * nightZoneRatePrice);
		result = roundNumber(result, 2);
		$('.result_trial_mode').html(result);
	}
}

//слушатели (обновление результатов при взаимодействии):
//выпадающее меню
$('.table_selector').change(function() {
	writeRatesToHiddenTags();

});
//кнопка рассчёта
$('.calculate_button').click(function() {
	let tableId = $('.table_selector').val();
	if (tableId == 255){
		alert ("Выберите подходящую категорию тарифов")
	} else {
		updateSingleModeForms();
		updateDualModeForms();
		updateTrialModeForms();
	}
});