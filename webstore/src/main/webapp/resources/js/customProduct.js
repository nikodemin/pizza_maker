$(function () {
    var baseUrl = window.location.origin + '/webstore_war/customProduct'
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var vueData={
        products: [],
        ingredients: [],
        category: 'Pizza',
        name: '',
        chosenIngs: [],
        total: 0,
        error: null
    }

    function getIngredients() {
        $.ajax({
            url: baseUrl + '/ingredients/'+vueData.category,
            type: 'get',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                vueData.ingredients = data
            },
            error: function (jqXHR, status, errorThrown) {
                raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                console.log('ERROR: ' + jqXHR.responseText)
            }
        })
    }
    getIngredients()

    function getUserProducts() {
        $.ajax({
            url: baseUrl + '/userProducts/'+vueData.category,
            type: 'get',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                vueData.products = data
            },
            error: function (jqXHR, status, errorThrown) {
                raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                console.log('ERROR: ' + jqXHR.responseText)
            }
        })
    }
    getUserProducts()

    var vue = new Vue({
        el: '#app',
        data: vueData,
        methods: {
            calcPrice: function (price) {
                return price/100;
            },
            categoryChanged: function () {
                getIngredients()
                getUserProducts()
                vueData.chosenIngs = []
                vueData.total = 0
            },
            addIngredient: function (e) {
                var chosen = vueData.ingredients.filter(function (value) {return  value.name==/(.*)\ [0-9]*\.?[0-9]*\$/g.exec($(e.target).text().trim())[1]})[0]
                if (vueData.chosenIngs.length > 9) {
                    vueData.error='Too many ingredients!'
                    return
                }
                vueData.chosenIngs.push(chosen)
                vueData.total += chosen.price
            },
            deleteIngredient: function (e) {
                vueData.error = null
                var name = $(e.target).parent().find('span.name').text().trim()
                var newIngs = vueData.chosenIngs.filter(function (value) { return value.name!=name })
                vueData.total = 0;
                newIngs.forEach(function (value) {vueData.total += value.price})
                vueData.chosenIngs = newIngs
            },
            submit: function () {
                var data = {
                    name: vueData.name,
                    price: vueData.total,
                    category: {
                        name: vueData.category
                    },
                    ingredients: vueData.chosenIngs
                }
                $.ajax({
                    url: baseUrl + '/userProducts/',
                    type: 'POST',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (data) {
                        raisePopup(data,'warning')
                        getIngredients()
                        getUserProducts()
                        vueData.chosenIngs = []
                        vueData.total = 0
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            addToCart: function (e) {
                var productName = /(.*)\ [0-9]*\.?[0-9]*\$/g.exec($(e.target).parents('div.product').find('span.name').text().trim())[1];
                $.ajax({
                    url: baseUrl + '/addToCart/'+productName,
                    type: 'PUT',
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (data) {
                        raisePopup(data,'warning')
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            deleteProduct: function (e) {
                var productName = /(.*)\ [0-9]*\.?[0-9]*\$/g.exec($(e.target).parents('div.product').find('span.name').text().trim())[1];
                $.ajax({
                    url: baseUrl + '/userProducts/'+productName,
                    type: 'DELETE',
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (data) {
                        getUserProducts()
                        raisePopup(data,'warning')
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