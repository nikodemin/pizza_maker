$(function () {

    var baseUrl = window.location.origin + '/webstore_war'
    var currentCategory
    var currentProduct
    var isEditProduct = false

    var vueData = {
        categories: [],
        products: [],
        ingredients: [],
        tags: [],
        categoryTags: [],
        categoryIngs: []
    }

    function getCategories() {
        $.ajax({
            url: baseUrl + '/admin/getCategories',
            type: 'get',
            success: function (data) {
                vueData.categories = data
            },
            error: function (jqXHR, status, errorThrown) {
                raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                console.log('ERROR: ' + jqXHR.responseText)
            }
        })
    }
    getCategories()

    function getIngredients() {
        $.ajax({
            url: baseUrl + '/admin/getIngredients',
            type: 'get',
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

    function getTags() {
        $.ajax({
            url: baseUrl + '/admin/getTags',
            type: 'get',
            success: function (data) {
                vueData.tags = data
            },
            error: function (jqXHR, status, errorThrown) {
                raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                console.log('ERROR: ' + jqXHR.responseText)
            }
        })
    }
    getTags()

    function getProducts(category) {
        category = category || currentCategory
        $.ajax({
            url: baseUrl + '/admin/getProducts/' + category,
            type: 'get',
            success: function (data) {
                vueData.products = data
                currentCategory = category
            },
            error: function (jqXHR, status, errorThrown) {
                raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                console.log('ERROR: ' + jqXHR.responseText)
            }
        })
        $.ajax({
            url: baseUrl + '/admin/getCatTags/' + category,
            type: 'get',
            success: function (data) {
                vueData.categoryTags = data
            },
            error: function (jqXHR, status, errorThrown) {
                raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                console.log('ERROR: ' + jqXHR.responseText)
            }
        })
        $.ajax({
            url: baseUrl + '/admin/getCatIngs/' + category,
            type: 'get',
            success: function (data) {
                vueData.categoryIngs = data
            },
            error: function (jqXHR, status, errorThrown) {
                raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                console.log('ERROR: ' + jqXHR.responseText)
            }
        })
    }

    var app = new Vue({
        el: '#app',
        data: vueData,
        methods: {
            getImgUrl: function (url) {
                return baseUrl + url
            },
            calcPrice: function (price) {
                return price/100;
            },
            addCategory: function (e) {
                e.preventDefault()
                var data = new FormData($('#addCatForm')[0])

                $.ajax({
                    url: baseUrl + '/admin/addCategory',
                    type: 'POST',
                    enctype: 'multipart/form-data',
                    data: data,
                    cache: false,
                    processData: false,
                    contentType: false,
                    success: function (respond, status, jqXHR) {

                        if (typeof respond.popup === 'undefined') {
                            raisePopup("SUCCESS " + respond,'warning');                          vueData.categories.push({name: data.get('name')})
                        } else {
                            raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                            console.log('ERROR: ' + jqXHR.responseText)
                        }
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },

            getProductsByClick: function (e) {
                getProducts(e.target.innerText)
            },
            deleteCat: function (e) {
                $.ajax({
                    url: baseUrl + '/admin/deleteCategory/' + e.target.innerText,
                    type: 'DELETE',
                    success: function (data) {
                        vueData.products = data
                        if (e.target.innerText == currentCategory)
                            vueData.products = []
                        getCategories()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            deleteIng: function (e) {
                $.ajax({
                    url: baseUrl + '/admin/deleteIngredient/' + e.target.innerText,
                    type: 'DELETE',
                    success: function (data) {
                        getProducts()
                        getIngredients()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            addIng: function (e) {
                e.preventDefault()
                var data = {
                    name: $('#addIngForm input.name').val().trim(),
                    price: $('#addIngForm input.price').val().trim(),
                    categories: $('.ingCatToAddBtn.active').map(function () { return $(this).text().trim()}).get()
                }

                $.ajax({
                    url: baseUrl + '/admin/addIngredient',
                    type: 'POST',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    success: function (respond, status, jqXHR) {

                        if (typeof respond.popup === 'undefined') {
                            raisePopup("SUCCESS " + respond,'warning');                            vueData.ingredients.push({name: data.name})
                            getProducts()
                        } else {
                            raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                            console.log('ERROR: ' + jqXHR.responseText)
                        }
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            deleteTag: function (e) {
                $.ajax({
                    url: baseUrl + '/admin/deleteTag/' + e.target.innerText,
                    type: 'DELETE',
                    success: function (data) {
                        getProducts()
                        getTags()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            addTag: function (e) {
                e.preventDefault()
                var data = {
                    name : $('#addTagForm input').val().trim(),
                    categories: $('.catToAddBtn.active').map(function () {return $(this).text().trim()}).get()
                }

                $.ajax({
                    url: baseUrl + '/admin/addTag',
                    type: 'POST',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    success: function (respond, status, jqXHR) {

                        if (typeof respond.popup === 'undefined') {
                            raisePopup("SUCCESS " + respond,'warning');                            vueData.tags.push({name: data.name})
                            getProducts()
                        } else {
                            raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                            console.log('ERROR: ' + jqXHR.responseText)
                        }
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            deleteTagFromProduct: function (e) {
                var tagName = $(e.target).parent().attr('data-name').trim()
                var productName = $(e.target).parents('div.product')
                    .find('h5.name').text().trim()
                $.ajax({
                    url: baseUrl + '/admin/deleteTag/' + currentCategory + '/' + productName + '/' + tagName,
                    type: 'DELETE',
                    success: function (data) {
                        getProducts()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            deleteIngredientFromProduct: function (e) {
                var IngName = $(e.target).parent().attr('data-name').trim()
                var productName = $(e.target).parents('div.product')
                    .find('h5.name').text().trim()
                $.ajax({
                    url: baseUrl + '/admin/deleteIngredient/' + currentCategory + '/' + productName + '/' + IngName,
                    type: 'DELETE',
                    success: function (data) {
                        getProducts()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            editProduct: function (e) {
                currentProduct = $(e.target).siblings('h5.name').text()
                isEditProduct = true
                $('#shadow').show()
                $('#dialog').show()
                $('a.close').one('click', function () {
                    $('#shadow').hide()
                    $('#dialog').hide()
                })
            },
            submitProduct: function (e) {
                var data = new FormData($('#editProductForm')[0]),
                    url
                if (isEditProduct)
                    url = baseUrl + '/admin/updateProduct/' + currentCategory + '/' + currentProduct
                else
                    url = baseUrl + '/admin/addProdcut/' + currentCategory
                $.ajax({
                    url: url,
                    type: 'POST',
                    enctype: 'multipart/form-data',
                    data: data,
                    cache: false,
                    processData: false,
                    contentType: false,
                    success: function (respond, status, jqXHR) {

                        if (typeof respond.popup === 'undefined') {
                            raisePopup("SUCCESS " + respond,'warning')
                            getProducts()
                        } else {
                            raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        }
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            deleteProduct: function (e) {
                $.ajax({
                    url: baseUrl + '/admin/deleteProduct/' + currentCategory + '/' + currentProduct,
                    type: 'DELETE',
                    success: function (data) {
                        getProducts()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            addTagToProduct: function (e) {
                $.ajax({
                    url: baseUrl + '/admin/addTagToProduct/' + currentCategory + '/' +
                        currentProduct + '/' + $(e.target).text(),
                    type: 'PUT',
                    success: function (data) {
                        getProducts()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            addIngToProduct: function (e) {
                $.ajax({
                    url: baseUrl + '/admin/addIngToProduct/' + currentCategory + '/' +
                        currentProduct + '/' + $(e.target).text(),
                    type: 'PUT',
                    success: function (data) {
                        getProducts()
                    },
                    error: function (jqXHR, status, errorThrown) {
                        raisePopup('ERROR: ' + jqXHR.responseText,'danger')
                        console.log('ERROR: ' + jqXHR.responseText)
                    }
                })
            },
            addProduct: function (e) {
                isEditProduct = false
                $('#shadow').show()
                $('#dialog').show()
                $('a.close').one('click', function () {
                    $('#shadow').hide()
                    $('#dialog').hide()
                })
            },
            getTopProducts: function (e) {
                $.ajax({
                    url: baseUrl + '/admin/getTopProducts/',
                    type: 'get',
                    success: function (data) {
                        vueData.products = data
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