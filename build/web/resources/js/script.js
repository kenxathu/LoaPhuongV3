/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var leftPanelHeigh;
var windowHeight;

$(document).ready(function() {
    fixScrollDatatable();
});

$(window).load(function() {
    leftPanelHeigh = $("#left_panel").height();
    windowHeight = $(window).height();
});

function start() {
    PF('statusDialog').show();
}

function stop() {
    PF('statusDialog').hide();
}

function fixScrollDatatable() {
    $(".ui-datatable-tablewrapper:has(.ui-datatable-empty-message)").css("overflow", "hidden");
}

function scrollTo(element) {
    $('#layout_center .ui-layout-unit-content').animate({
        scrollTop: $(element).offset().top - 200
    }, 1000);
}

function resizeIframe(obj) {
    obj.style.height = (obj.contentWindow.document.body.scrollHeight + 25) + 'px';
}

function openInNewTab(url) {
    var win = window.open(url, '_blank');
    win.focus();
}