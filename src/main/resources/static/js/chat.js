// includes the csrf token in all ajax requests of this file
$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content"); $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token); });
});