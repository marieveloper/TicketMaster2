var stompClient = null;
$(window).onload = connect ();

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
        stompClient.subscribe('/topic/chatWebSocket/' + ticket.ticketId,function(chatMessage){
            console.log("Hallleluja!");
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
    //stompClient.send("/topic/chatWebSocket", {}, JSON.stringify({'name': $("#name").val()}));
    var chatMessage = {
        'text': $("#messageInput").val(),
        'author': user,
        'receiver': receiver,
        'ticket': ticket
    }
    stompClient.send("/app/hello/" + ticket.ticketId, {}, JSON.stringify(chatMessage));
   $("#messageInput").val("");
};

function showGreeting(message) {
    console.log(message);
    var chatMessageTest = JSON.parse(message.body);
    $("#chatMessage").append("<tr><td>" + chatMessageTest.text + "</td></tr>");
};

/*function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    $("#chatMessage").append("<tr><td>" + message.message + "</td></tr>");
}*/


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
});