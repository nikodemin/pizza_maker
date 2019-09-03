function raisePopup(text, style){
    $('<div class="popup alert alert-'+style+'">\n' +
        '    <span class="close">&times;</span>\n' +
        '    <div>\n' +
        '       '+text+'\n' +
        '    </div>\n' +
        '</div>').appendTo('#popupContainer').hide().fadeIn(function () {
        $(this).find('.close').on('click', function (e) {
            $(e.target).parents('.popup').remove()
        })
        $(this).delay(1000).fadeOut(2000,function () {
            $(this).remove()
        })
    })
}
$(function () {
    var baseUrl = window.location.origin + '/webstore_war'
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $('.addToCart').on('click',function (e) {
        e.preventDefault()
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
    })
})