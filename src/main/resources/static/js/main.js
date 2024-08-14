// Definisci la variabile globale
window.reloadTime = 7000;

function sendStorageRequest(quantityFw) {

    const saveButton = document.querySelector("#save");

    saveButton.firstElementChild.removeAttribute("hidden");
    saveButton.disabled = true

    const spinner = document.querySelector('.spinner-border');
    spinner.removeAttribute('hidden');

    //data structure to send to server
    const requestBodyMap = {
        fw: quantityFw
    }

    //request to server
    const relativeEndpoint = '/sendStorageRequest';
    fetch(
        relativeEndpoint,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBodyMap)
        }
    ).then(response => {
        if (response.ok) {
            //Saving animation end OK
            setTimeout(() => {
                const saveStatus = document.getElementById('save-status');
                saveStatus.removeAttribute('hidden');
                spinner.setAttribute('hidden', 'hidden');
                saveButton.firstElementChild.hidden = "hidden";
                saveButton.lastElementChild.textContent = 'Request sent';
                saveButton.disabled = true;
            }, 1700);
        } else {
            //Saving animation end ERROR
            setTimeout(() => {
                const saveStatus = document.getElementById('save-status');
                saveStatus.removeAttribute('hidden');
                spinner.setAttribute('hidden', 'hidden');
                saveButton.firstElementChild.hidden = "hidden";
                saveButton.lastElementChild.textContent = "Error!";
            }, 1700);
        }
        return response;
    }).then(response => {
        response.text().then((resolvedValue) => {

            if (!response.ok) {
                showError(response.text());
            } else {
                setTimeout(() => {
                    if (resolvedValue.includes("storerequestaccepted")) {
                        console.log("resolvedValue:",resolvedValue)
                        var ticketCodeTmp = resolvedValue.split("(")[2]
                        var ticketCode = ticketCodeTmp.split(")")[0]
                        showResponseStorageRequest("accepted", ticketCode)
                    } else if (resolvedValue.includes("storerequestrefused")) {
                        showResponseStorageRequest("rejected", null)
                    } else {
                        showResponseStorageRequest("KO", null)
                    }
                }, 2000);
            }
        });

    }).catch(error => {
        console.log(error);
    });
}

// function sendChargeStatusRequest() {

//     //request to server
//     const relativeEndpoint = '/sendChargeStatusRequest';
//     fetch(
//         relativeEndpoint,
//         {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: ""
//         }
//     ).then(response => {
//         response.text().then((resolvedValue) => {

//             if (!response.ok) {
//                 showError(response.text());
//             } else {
//                 setTimeout(() => {
//                     if (resolvedValue.includes("chargetaken")) {
//                         showResponseChargeStatus("accepted")
//                     } else {
//                         showResponseChargeStatus("KO")
//                     }
//                 }, 2000);
//             }
//         });

//     }).catch(error => {
//         console.log(error);
//     });
// }

function enterTicketRequest(inputValue) {

    const saveButton = document.querySelector("#sendTicketNumber");

    saveButton.firstElementChild.removeAttribute("hidden");
    saveButton.disabled = true

    const spinner = document.querySelector('#spinner-border-ticket');
    spinner.removeAttribute('hidden');


    //data structure to send to server
    const requestBodyMap = {
        ticketCode: inputValue
    }

    //request to server
    const relativeEndpoint = '/enterTicketRequest';
    fetch(
        relativeEndpoint,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBodyMap)
        }
    ).then(response => {
        if (response.ok) {
            //Saving animation end OK
            setTimeout(() => {
                const saveStatus = document.getElementById('save-status-ticket');
                saveStatus.removeAttribute('hidden');
                spinner.setAttribute('hidden', 'hidden');
                saveButton.firstElementChild.hidden = "hidden";
                saveButton.lastElementChild.textContent = 'Ticket entered';
                saveButton.disabled = true;
            }, 1700);
        } else {
            //Saving animation end ERROR
            setTimeout(() => {
                const saveStatus = document.getElementById('save-status-ticket');
                saveStatus.removeAttribute('hidden');
                spinner.setAttribute('hidden', 'hidden');
                saveButton.firstElementChild.hidden = "hidden";
                saveButton.lastElementChild.textContent = "Error!";
            }, 1700);
        }
        return response;
    }).then(response => {
        response.text().then((resolvedValue) => {

            if (!response.ok) {
                showError(response.text());
            } else {
                setTimeout(() => {
                    if (resolvedValue.includes("ticketisvalid")) {
                        showResponseTicket("accepted")
                    } else if (resolvedValue.includes("ticketinvalidated")) {
                        showResponseTicket("rejected")
                    } else {
                        showResponseTicket("KO")
                    }
                }, 2000);
            }
        });

    }).catch(error => {
        console.log(error);
    });
}

function showResponseStorageRequest(response, ticketCode) {
    const responseBody = document.getElementById('responseBodyStorage');
    responseBody.style.display = "block";
    const responseText = document.getElementById('responseTextStorage');
    if (response === "accepted") {
        setTimeout(() => {
            responseText.innerHTML = "The storage request has been accepted! <br>Your ticket is: <b>" + ticketCode + "</b>"
        }, 500);
    } else if (response === "rejected") {
        setTimeout(() => {
            responseText.innerHTML = "The request has been rejected! <br>The page will be restored shortly."
        }, 500);
        countdownFail(window.reloadTime, "countdownStorage");
        setTimeout(() => {
            location.reload();
        }, reloadTime);
    } else {
        responseText.innerHTML = "Error during processing the deposit! <br>The page will be restored shortly."
        countdownFail(window.reloadTime, "countdownStorage")
        setTimeout(() => {
            location.reload();
        }, reloadTime);
    }
}

function showResponseTicket(response) {
    const responseBody = document.getElementById('responseBodyTicket');
    responseBody.style.display = "block";
    const responseText = document.getElementById('responseTextTicket');
    if (response === "accepted") {
        setTimeout(() => {
            responseText.innerHTML = "Your ticket has been accepted, the service is taking care of your load. <br> Wait until the handling is completed."
        }, 500);
        // setTimeout(() => {
        //     sendChargeStatusRequest();
        // }, 1000);
    } else if (response === "rejected") {
        setTimeout(() => {
            responseText.innerHTML = "Your ticket has been rejected! <br>Check that you have entered it correctly."
        }, 500);
    } else {
        responseText.innerHTML = "Error during processing the ticket validation! <br>The page will be restored shortly."
        countdownFail(window.reloadTime, "countdownTicket")
        setTimeout(() => {
            location.reload();
        }, reloadTime);
    }
}

// function showResponseChargeStatus(response) {
//     const responseBody = document.getElementById('responseBodyChargeStatus');
//     responseBody.style.display = "block";
//     const responseText = document.getElementById('responseTextChargeStatus');
//     if (response === "accepted") {
//         setTimeout(() => {
//             responseText.innerHTML = "Your load has been taken in charge from the service! <br> The page will be restored shortly: you can leave the INDOOR."
//             countdownFail(window.reloadTime, "countdownChargeStatus");
//             setTimeout(() => {
//                 location.reload();
//             }, reloadTime);
//         }, 500);
//     } else if (response === "KO") {
//         setTimeout(() => {
//             responseText.innerHTML = "The service encountered an issue and the load was not taken over! <br>The page will be restored shortly."
//         }, 500);
//         countdownFail(window.reloadTime, "countdownChargeStatus");
//         setTimeout(() => {
//             location.reload();
//         }, reloadTime);
//     } else {
//         responseText.innerHTML = "Error during processing the handling of the load! <br>The page will be restored shortly."
//         countdownFail(window.reloadTime, "countdownChargeStatus")
//         setTimeout(() => {
//             location.reload();
//         }, reloadTime);
//     }
// }

function validateInput() {
    const inputElement = document.getElementById('quantity');
    const inputValue = parseFloat(inputElement.value);

    if (isNaN(inputValue) || inputValue <= 0.0) {
        // Display an error message
        showError('Please enter a positive integer value!');
    } else {
        sendStorageRequest(inputValue);
    }
}

function validateTicket() {
    const inputElement = document.getElementById('ticketNumberField');
    const inputValue = inputElement.value;

    const regex = /\d{10}/g

    if (inputValue.length < 1) {
        // Display an error message
        showError('Ticket field must be filled!');
    } else if (!inputValue.match(regex)) {
        showError('Ticket code is not in the correct format!');
    } else {
        enterTicketRequest(inputValue);
    }
}

function showError(message) {
    const errorToast = document.getElementById('errorToast');
    const toastBody = errorToast.querySelector('.toast-body');
    toastBody.textContent = message;
    errorToast.classList.add('show');
    setTimeout(() => {
        errorToast.classList.remove('show');
    }, 2000);
}

document.addEventListener("DOMContentLoaded", function () {
    const inputElement = document.getElementById('quantity');
    inputElement.value = ''; // Imposta il campo di input a una stringa vuota
    //connect();
});

function countdown(time, element) {
    const countdownDate = new Date().getTime() + time - 1000;

    // Aggiorna il conto alla rovescia ogni secondo
    const countdownInterval = setInterval(() => {
        const now = new Date().getTime();
        const timeLeft = countdownDate - now;

        if (timeLeft <= 0) {
            clearInterval(countdownInterval);
            element.innerHTML = "Updating the page ...";
        } else {
            const seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
            element.innerHTML = `${seconds} seconds left ...`;
        }
    }, 1000);
}

function countdownFail(time, countdownElement) {
    countdown(time - 1000, document.getElementById(countdownElement));
}


function getColdRoomAvailability() {

    const spinner = document.querySelector('.spinner-border');
    spinner.removeAttribute('hidden');

    const relativeEndpoint = '/availability';
    fetch(
        relativeEndpoint,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        }
    ).then(response => {
        response.text().then((resolvedValue) => {

            if (!response.ok) {
                showError(response.text());
            } else {
                setTimeout(() => {
                    console.log("availability:",resolvedValue)
                    //var availabilityTmp = resolvedValue.split("(")[2]
                    //var availability = availabilityTmp.split(")")[0]
                    //showAvailability("accepted", availability)
                }, 2000);
            }
        });

    }).catch(error => {
        console.log(error);
    });
}



function startPeriodicRequests(intervalInSeconds) {
    const intervalInMilliseconds = intervalInSeconds * 1000;
    setInterval(() => {
        getColdRoomAvailability();
    }, intervalInMilliseconds);
}
//startPeriodicRequests(2); 