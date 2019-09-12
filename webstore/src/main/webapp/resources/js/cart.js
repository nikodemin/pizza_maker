$(function () {
    var baseUrl = window.location.origin + '/webstore_war'
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var vueData = {
        products: [],
        total: 0,
        payment: 'cash',
        cardError: false,
        delivery: 'courier',
        addressError: false,
        cardEmpty: false
    }

    function getProducts(){
        $.ajax({
            url: baseUrl + '/getCartProducts/',
            type: 'GET',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                console.log(data)
                vueData.products = data
                vueData.total = 0
                vueData.products.forEach(function (tuple) {
                    vueData.total += tuple.key.price*tuple.value
                })
            },
            error: function (jqXHR, status, errorThrown) {
                raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                console.log('ERROR: ' + jqXHR.responseText)
            }
        })
    }
    getProducts()

    var vue = new Vue({
        el: '#app',
        data: vueData,
        methods: {
            calcPrice: function (price) {
                return price/100;
            },
            addProduct: function (e) {
                var name = $(e.target).parents('tr').find('.productName').text()
                var data = vueData.products.filter(function (p) {return p.key.name==name})[0].key
                $.ajax({
                    url: baseUrl + '/addToCart/',
                    type: 'POST',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (data) {
                        console.log(data)
                        getProducts()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            removeProduct: function (e) {
                var name = $(e.target).parents('tr').find('.productName').text().trim()
                var data = vueData.products.filter(function (p) {return p.key.name==name})[0].key
                console.log(data)
                $.ajax({
                    url: baseUrl + '/removeFromCart/',
                    type: 'POST',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (data) {
                        console.log(data)
                        getProducts()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            deleteProduct:function (e) {
                var name = $(e.target).parents('tr').find('.productName').text()
                var data = vueData.products.filter(function (p) {return p.key.name==name})[0].key
                $.ajax({
                    url: baseUrl + '/deleteAllFromCart/',
                    type: 'POST',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (data) {
                        console.log(data)
                        getProducts()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            redirectToPayment: function () {
                window.location = baseUrl + '/payment'
            },
            submitOrder: function () {
                var deliveryForm = $('#deliveryForm')
                var cardForm = $('#cardForm')

                var data = {
                    key:{
                        country: deliveryForm.find('.country'),
                        city: deliveryForm.find('.city'),
                        street: deliveryForm.find('.street'),
                        house: deliveryForm.find('.house'),
                        flat: deliveryForm.find('.flat')
                    },
                    value:{
                        cardNumber: cardForm.find(''),
                        month: cardForm.find(''),
                        year: cardForm.find(''),
                        cvv: cardForm.find('')
                    }
                }

                vueData.addressError = false
                vueData.cardError = false
                vueData.cardEmpty = false

                $.ajax({
                    url: baseUrl + '/isCartEmpty',
                    type: 'GET',
                    async: false,
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function(data){
                        vueData.cardEmpty = data
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })


                if (vueData.cardEmpty)
                    return

                $.ajax({
                    url: baseUrl + '/submitOrder',
                    type: 'POST',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function(data){
                        //window.location = baseUrl + '/confirmation'
                        raisePopup("SUCCESS",'success')
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            }
        }
    })
})