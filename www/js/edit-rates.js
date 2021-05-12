$('#table_selector').change(function() {
    getDataForEditForm();

});

let tokenCookie = getCookie("token"); 
console.log(tokenCookie);

$('#rates_form').change(function() {
    updateRates(tokenCookie);
    return false;
});

getDataForEditForm();