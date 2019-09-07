var baseUrl = window.location.origin + '/webstore_war'

var vueData = {
    products: []
}

$('.card-img-top').attr(':src','getImgUrl(product.image)')

var vue = new Vue({
    el: '#app',
    data: vueData,
    methods: {
        getImgUrl: function (url) {
            return baseUrl + url
        },
        calcPrice: function (price) {
            return price/100;
        }
    }
})

function socketListener(message, channel, event) {
    vueData.products = JSON.parse(message)
}

function getProducts() {
    $.ajax({
        url: baseUrl + '/external/getTopProducts',
        type: 'GET',
        success: function (data) {
            vueData.products = data
        },
        error: function (jqXHR, status, errorThrown) {
            console.log('ERROR: ' + jqXHR.responseText)
        }
    })
}
getProducts()
