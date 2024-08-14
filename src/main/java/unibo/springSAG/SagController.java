package unibo.springSAG;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.springSAG.connection.SagConnection;
import unibo.springSAG.connection.websocket.CoapObserver;
import unibo.springSAG.model.FWRequest;
import unibo.springSAG.model.TicketRequest;

import java.io.ByteArrayOutputStream;

@Controller
public class SagController {

    public static final String className = "SagController";
    @Value("${spring.application.name}")
    String appName;
    private SagConnection sagConnection;
    //private CoapConnection observerConn;
    private Interaction requestConnToTicketService;
    private Interaction requestConnToColdRoom;


    @Autowired
    public SagController(SagConnection sagConnection) {
        System.out.println(className + " | constructor");
        this.sagConnection = sagConnection;
        System.out.println(className + " | sagConnection: " + sagConnection);
        //this.observerConn = sagConnection.connectLocalActorUsingCoap();
        //observerConn.observeResource(new CoapObserver());
        this.requestConnToTicketService = sagConnection.connectLocalActorUsingCoap("ticketservice");
        this.requestConnToColdRoom = sagConnection.connectLocalActorUsingCoap("coldroom");

        //System.out.println(className + " | observerConn: " + observerConn);
    }

    @GetMapping("/")
    public String homePage(Model model) {
        String temp = "ERROR";
        String actual = "ERROR";

        model.addAttribute("arg", appName);

        if (this.requestConnToTicketService == null) {
            model.addAttribute("tempCurrentColdRoom", temp);
            model.addAttribute("actualCurrentColdRoom", actual);
        }

        /* 
        String answer = sagConnection.sendInitColdRoom(this.requestConnToTicketService);
        if (answer == null) {
            model.addAttribute("tempCurrentColdRoom", temp);
            model.addAttribute("actualCurrentColdRoom", actual);
        } else {
            String both = answer.substring(answer.indexOf("coldroom(") + 9, answer.indexOf(")"));
            actual = both.split(",")[0];
            temp = both.split(",")[1];
        }
        model.addAttribute("tempCurrentColdRoom", temp + " KG");
        model.addAttribute("actualCurrentColdRoom", actual + " KG");
        */
        return "main";
    }

    @PostMapping(value = "/sendStorageRequest", consumes = "application/json")
    public ResponseEntity<String> sendStorageRequest(@RequestBody FWRequest fwrequest) {
        if (fwrequest == null || fwrequest.getFw() == null || fwrequest.getFw() < 0) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " sendStorageRequest | ERROR: input error", headers, HttpStatus.BAD_REQUEST);
        }

        if (this.requestConnToTicketService == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " sendStorageRequest | ERROR: connection null", headers, HttpStatus.NOT_FOUND);
        }

        String answer = sagConnection.sendStorageRequest(this.requestConnToTicketService, fwrequest.getFw());
        if (answer == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " sendStorageRequest | ERROR: response null", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(className + " sendStorageRequest | answer: " + answer, headers, HttpStatus.OK);
    }

    // @PostMapping(value = "/sendChargeStatusRequest")
    // public ResponseEntity<String> sendChargeStatusRequest() {

    //     String answer = sagConnection.sendChargeStatusRequest(this.requestConnToTicketService);
    //     if (answer == null) {
    //         HttpHeaders headers = new HttpHeaders();
    //         return new ResponseEntity<>(className + " sendChargeStatusRequest | ERROR: response null", headers, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }

    //     HttpHeaders headers = new HttpHeaders();
    //     return new ResponseEntity<>(className + " sendChargeStatusRequest | answer: " + answer, headers, HttpStatus.OK);
    // }

    @PostMapping(value = "/enterTicketRequest", consumes = "application/json")
    public ResponseEntity<String> enterTicketRequest(@RequestBody TicketRequest ticketrequest) {
        if (ticketrequest == null || ticketrequest.getTicketCode() == null || ticketrequest.getTicketCode().isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " enterTicketRequest | ERROR: input error", headers, HttpStatus.BAD_REQUEST);
        }

        Interaction conn = sagConnection.connectLocalActorUsingCoap("ticketservice");
        if (conn == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " enterTicketRequest | ERROR: connection null", headers, HttpStatus.NOT_FOUND);
        }

        String answer = sagConnection.enterTicketRequest(conn, ticketrequest.getTicketCode());
        if (answer == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " enterTicketRequest | ERROR: response null", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(className + " enterTicketRequest | answer: " + answer, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/availability", consumes = "application/json")
    public ResponseEntity<String> sendStorageRequest() {

        if (this.requestConnToColdRoom == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " sendStorageRequest | ERROR: requestConnToColdRoom connection null", headers, HttpStatus.NOT_FOUND);
        }

        String answer = sagConnection.askAvailability(this.requestConnToTicketService);
        if (answer == null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(className + " sendStorageRequest | ERROR: response null", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(className + " sendStorageRequest | answer: " + answer, headers, HttpStatus.OK);
    }
}