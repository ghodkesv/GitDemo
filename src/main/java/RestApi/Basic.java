package RestApi;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import files.PayLoad;

public class Basic {
	public static void main(String args[]) {
		//Validate if Add place API is working as expected
		
		//given- All input details
		//when- Submit the Api- resource,http method
		//then- validate the response
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(PayLoad.AddPlace()).when().post("/maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.41 (Ubuntu)");
	}
}
