package RestApi;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import files.ReusableMethods;

public class Basic1 {
	public static void main(String[] args) throws IOException {
		//Validate if Add place API is working as expected
		
				//given- All input details
				//when- Submit the Api- resource,http method
				//then- validate the response
				//content of the file to String -> Content of file can convert into Byte -> Byte data to String
				
				RestAssured.baseURI="https://rahulshettyacademy.com";
				
				String response=given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
				.body(new String(Files.readAllBytes(Paths.get("C:\\API\\AddPlace.json")))).when().post("/maps/api/place/add/json")
				.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
				.header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
				
				System.out.println();
				System.out.println(response);
				System.out.println();
				
				JsonPath js=new JsonPath(response);//for parsing Json
				String placeID=js.getString("place_id");
				
				System.out.println("Place ID : "+placeID);
				
				
				
			//Update adrewss
				String addressPlace="winter walk, USA";
				given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
				.body("{\r\n"
						+ "\"place_id\":\""+placeID+"\",\r\n"
						+ "\"address\":\""+addressPlace+"\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n"
						+ "}\r\n"
						+ "").
				when().put("/maps/api/place/update/json").
				then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
				
				//Get Place
			String getPlaceResponse=given().log().all().queryParam("key", "qaclick123")
				.queryParam("place_id", placeID)
				.when().get("/maps/api/place/get/json")
				.then().assertThat().log().all().statusCode(200).extract().response().asString();
			JsonPath js1=ReusableMethods.rawToJson(getPlaceResponse);
			String Actualaddress=js1.getString("address");
			
			Assert.assertEquals(Actualaddress,addressPlace);
			
			System.out.println();
			System.out.println(Actualaddress);

	}
}
