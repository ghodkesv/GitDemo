package RestApi;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.PayLoad;
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
public class DynamicJson {
	@Test(dataProvider = "Booksdata")
	public void addBook(String isbn, String aisle) {
		
		RestAssured.baseURI="http://216.10.245.166";
		
	String response=given().log().all().header("Content-Type","application/json").
		body(PayLoad.Addbook(isbn, aisle)).
		when().post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
	
	JsonPath js=ReusableMethods.rawToJson(response);
	String id=js.get("ID");
	System.out.println(id);
	
	}
	
	@DataProvider(name = "Booksdata")
	public Object[][] getData() {
		return new Object[][] {
			{"ajishhs","276938"},
			{"mxnnmb","87979"},
			{"faodxd","54546"},
			{"acjxzc","194323"}
			};
	}
}
