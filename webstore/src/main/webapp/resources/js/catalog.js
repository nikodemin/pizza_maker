$(function () {
    var baseUrl = window.location.origin + '/webstore_war'
    var category = document.location.pathname.match(/.*\/(.*)/)[1]
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var vueData = {
        products: []
    }

    function getProducts(){
        $.ajax({
            url: baseUrl + '/getProducts/'+category,
            type: 'GET',
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
    getProducts()

    var vue = new Vue({
        el: '#app',
        data: vueData,
        methods: {
            getImgUrl: function (url) {
                return baseUrl + url
            },
            calcPrice: function (price) {
                return price/100;
            },
            filterTags: function (e) {
                var data = $('button.filterTagsBtn.active').map(function () {return {name:$(this).text().trim()}}).get()
                if (!$(e.target).hasClass('active'))
                    data.push({name:$(e.target).text().trim()})
                else
                    data = data.filter(function (val) {return val.name != $(e.target).text().trim()})

                if(data.length == 9) {
                    getProducts()
                    return
                }

                $.ajax({
                    url: baseUrl + '/getProductsWithTags/'+category,
                    type: 'POST',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
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
            },
            addToCart: function (e) {
                var product = $(e.target).parents('.productItem').find('.productName').text()
                $.ajax({
                    url: baseUrl + '/addToCart/' + product,
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
            }
        }
    })
})