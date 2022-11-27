package Ecom;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import POJOClassForEcom.Order;
import POJOClassForEcom.loginRequest;
import POJOClassForEcom.loginResponsePayload;
import POJOClassForEcom.orderDetails;
public class E_CommerceApiTest {
		
	@Test
	public void main() {
		
		RequestSpecification req=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
		
		// login call
		loginRequest loginrequest=new loginRequest();
		loginrequest.setUserEmail("santoshghodke88@gmail.com");
		loginrequest.setUserPassword("Svg@8888");
		
		//relaxedHTTPSValidation()---->used to bypass SSL
		RequestSpecification reqLogin=given().log().all()/*.relaxedHTTPSValidation()*/.spec(req).body(loginrequest);
		
		loginResponsePayload loginresponse= reqLogin.when().post("/api/ecom/auth/login").then().log().all().extract().response().as(loginResponsePayload.class);
		
		System.out.println(loginresponse.getToken());
		String token=loginresponse.getToken();
		System.out.println(loginresponse.getUserId());
		String userID=loginresponse.getUserId();
		
		//Add Product
		
		RequestSpecification addProductReq=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.addHeader("Authorization", token).build();
		
		RequestSpecification reqAddProduct= given().log().all().spec(addProductReq).param("productName", "qwerty")
		.param("productAddedBy", userID)
		.param("productCategory", "fashion")
		.param("productSubCategory", "shirts")
		.param("productPrice", "11500")
		.param("productDescription", "Addias Originals")
		.param("productFor", "women")
		.multiPart("productImage", new File("D:\\Shir.jpg"));
		
		String addProductResponse= reqAddProduct.when().post("/api/ecom/product/add-product").
		then().log().all().extract().response().asString();
		
		JsonPath js=new JsonPath(addProductResponse);
		String productID=js.get("productId");
		
		//create Order
		
		RequestSpecification CreateOrderBaseReq=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).setContentType(ContentType.JSON).build();
		
		orderDetails OrderDetails=new orderDetails();
		OrderDetails.setCountry("India");
		OrderDetails.setProductOrderedId(productID);
		
		List<orderDetails> orderDetailList=new ArrayList<orderDetails>();
		orderDetailList.add(OrderDetails);
		
		Order order=new Order();
		order.setOrder(orderDetailList);
		
		RequestSpecification createOrderReq= given().log().all().spec(CreateOrderBaseReq).body(order);
		
		String responseAddOrder=createOrderReq.when().post("/api/ecom/order/create-order")
				.then().log().all().extract().response().asString();
		
		System.out.println(responseAddOrder);
		
		// Delete Product
		
		RequestSpecification DeleteProductBaseReq=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).setContentType(ContentType.JSON).build();
		
		RequestSpecification deleteprodReq=given().log().all().spec(DeleteProductBaseReq).pathParam("productId", productID);
		
		String deleteProductResponse=deleteprodReq.when().delete("/api/ecom/product/delete-product/{productId}")
		.then().log().all().extract().response().asString();
		
		JsonPath js1=new JsonPath(deleteProductResponse);
		
		Assert.assertEquals("Product Deleted Successfully", js1.get("message"));
		
		
	}

}
