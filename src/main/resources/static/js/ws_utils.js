var socket;

function connect() {
    var host = document.location.host;
    var pathname = "/"
    var addr = "ws://" + host + pathname + "socket";
    console.log("addr:",addr);

    // Assicura che sia aperta un unica connessione
    if (socket !== undefined && socket.readyState !== WebSocket.CLOSED) {
        alert("WARNING: Connessione WebSocket già stabilita");
    }
    socket = new WebSocket(addr);

    socket.onopen = function (event) {
        console.log("ws-onopen event.data:" + event.data);
    };

    socket.onmessage = function (event) {
        console.log("ws-onmessage:" + `${event.data}`);
        if (`${event.data}`.includes("actualcurrent")) {
            var msg = `${event.data}`
            var actual = msg.split("(")[1].split(")")[0];
            const actualElement = document.getElementById('actual');
            actualElement.innerHTML = actual + " KG";
        }
        else if (`${event.data}`.includes("tempcurrent")) {
            var msg = `${event.data}`
            var temp = msg.split("(")[1].split(")")[0];
            const tempElement = document.getElementById('temp');
            tempElement.innerHTML = temp + " KG";
        }
        else if (`${event.data}`.includes("stomp")) {
            setTimeout(() => {
                location.reload();
            }, 5000);
        }

    };
}//connect
