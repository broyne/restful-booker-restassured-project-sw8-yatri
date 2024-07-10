package com.restful.booker.crudtest;

import com.restful.booker.model.BookingPojo;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.Utility;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class BookingCRUDTest extends TestBase {

    static String id ;
    static String fName;
    static String lName;
    static int totalPrice;
    static boolean depositPaid;
    static HashMap<String, String> bookingDates;
    static String additionalNeeds;

    @Test(priority = 0)
    public void verifyBookingCreatedSuccessfully() {
        fName = "yatri" + Utility.getRandomValue();
        lName = "hirani" + Utility.getRandomValue();
        totalPrice = 1000;
        depositPaid = true;
        bookingDates = new HashMap<>();
        String checkIn = "2024-07-01";
        String checkOut = "2024-07-08";
        bookingDates.put("checkin", checkIn);
        bookingDates.put("checkout", checkOut);
        additionalNeeds = "Breakfast";

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(fName);
        bookingPojo.setLastname(lName);
        bookingPojo.setTotalprice(totalPrice);
        bookingPojo.setDepositpaid(depositPaid);
        bookingPojo.setBookingdates(bookingDates);
        bookingPojo.setAdditionalneeds(additionalNeeds);

        Response response = given().log().ifValidationFails()
                .header("Content-Type", "application/json")
                .when()
                .body(bookingPojo)
                .post("/booking");
        System.out.println(response.jsonPath());
        id = response.jsonPath().getString("bookingid");

        response.prettyPrint();
        response.then().log().ifValidationFails().statusCode(200);
    }

    @Test(priority = 1)
    public void verifyBookingGetByIdSuccessfully() {

        Response response = given()
                .pathParam("id",id)
                .when()
                .get("/booking/{id}");  // john smith
        response.prettyPrint();
        response.then().statusCode(200);
    }


    @Test(priority = 2)
    public void verifyBookingUpdateSuccessfully(){
        String fName = "John" + "Updated";
        String lName = "Vialy" + "Updated";
        int totalPrice = 1100;
        boolean depositPaid = true;

        HashMap<String, String> bookingDates = new HashMap<>();
        String checkIn = "2024-07-03";
        String checkOut = "2024-07-10";
        bookingDates.put("checkin", checkIn);
        bookingDates.put("checkout", checkOut);
        String additionalNeeds = "Breakfast,Dinner";

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(fName);
        bookingPojo.setLastname(lName);
        bookingPojo.setTotalprice(totalPrice);
        bookingPojo.setDepositpaid(depositPaid);
        bookingPojo.setBookingdates(bookingDates);
        bookingPojo.setAdditionalneeds(additionalNeeds);

        Response response =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                        .header("Connection", "keep-alive")
                        .pathParam("id",id)
                        .body(bookingPojo)
                        .when()
                        .put("/booking/{id}");
        response.then().statusCode(200);
        response.prettyPrint();
    }

    @Test(priority = 3)
    public void verifyBookingDeletedSuccessfully() {
        Response response = given().log().all()
                .pathParam("id",id)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .when()
                .delete("booking/{id}");
        response.then().statusCode(201);
        response.prettyPrint();
    }
}
