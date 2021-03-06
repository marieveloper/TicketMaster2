'use strict';

var stompClient = null;
const options = {
    year: 'numeric', month: 'numeric', day: 'numeric',
    hour: 'numeric', minute: 'numeric', second: 'numeric',
    hour12: false,
    timeZone: 'America/Los_Angeles'
};


$(window).onload = connect();

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#chatMessage").html("");
}

function connect() {
    var socket = new SockJS('/chatWebSockets');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chatWebSocket/' + ticket.ticketId, function (chatMessage) {
            showGreeting(chatMessage);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {

    var chatMessage = {
        'text': $("#messageInput").val(),
        'author': author,
        'receiver': receiver,
        'ticket': ticket,
        'creationTime': Date.now()

    }
    if(chatMessage.text.length <1){
        $("#messageInput").val("");
    }else{
    stompClient.send("/app/hello/" + ticket.ticketId, {}, JSON.stringify(chatMessage));
    $("#messageInput").val("");}
};

function showGreeting(message) {

    var chatMessageTest = JSON.parse(message.body);
    var list = $("#messageArea");
    var fillingElement = document.createElement("div");
    var fillingRight = document.createElement("div");
    fillingElement.classList.add("row");
    fillingRight.classList.add("right");
    var messageElement = document.createElement('li');
    if (chatMessageTest.author.username == author.username) {
        messageElement.classList.add('chat-message-right');
    } else {
        messageElement.classList.add('chat-message');
    }
    var usernameElement = document.createElement('span');
    usernameElement.classList.add('badge');
    usernameElement.classList.add('badge-primary');
    usernameElement.classList.add('text-white');
    if (chatMessageTest.author.username == author.username) {
        var usernameText = document.createTextNode("You");
    } else {
        var usernameText = document.createTextNode(chatMessageTest.author.username);

    }
    var messageTimeElement = document.createElement('span');
    messageTimeElement.classList.add('timeClass');
    const date = new Date(chatMessageTest.creationTime);
    var dateStr =
        ("00" + (date.getMonth() + 1)).slice(-2) + "/" +
        ("00" + date.getDate()).slice(-2) + "/" +
        date.getFullYear() + " " +
        ("00" + date.getHours()).slice(-2) + ":" +
        ("00" + date.getMinutes()).slice(-2);
    var messageTimeText = document.createTextNode(dateStr);
    messageTimeElement.appendChild(messageTimeText);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
    messageElement.appendChild(messageTimeElement);
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(chatMessageTest.text);
    textElement.appendChild(messageText);
    var breakElement = document.createElement('br');
    messageElement.appendChild(breakElement);
    messageElement.appendChild(textElement);
    if (chatMessageTest.author.username == author.username) {
        fillingRight.appendChild(messageElement);
        console.log(messageElement);
        fillingElement.appendChild(fillingRight);
    } else {
        fillingElement.appendChild(messageElement);
    }
    console.log(fillingElement);
    list.append(fillingElement);
    list.scrollTop(list.prop('scrollHeight'));


};




$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendMessage();
    });
    $("#send1").click(function () {
        sendMessage();
    });
});