package OAuth;

import static io.restassured.RestAssured.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import POJO.GetCourses;
import POJO.api;
import POJO.webAutomation;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class OAuthTest {

	
	public static void main(String [] args) throws InterruptedException {
		
		
		// code to get authorization code through url
		WebDriverManager.chromedriver().setup();
		WebDriver driver=new ChromeDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&redirect_uri=https://rahulshettyacademy.com/getCourse.php&Auth_url=https://www.googleapis.com/oauth2/v4/token&response_type=code");
	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		//pass the username 
		driver.findElement(By.xpath("//input[@type='email']")).sendKeys("ghodkesantosh814@gmail.com");
		driver.findElement(By.xpath("//input[@type='email']")).sendKeys(Keys.ENTER);
		
		Thread.sleep(3000);
		
		// pass the password
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys("Svg@8888");
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		
		String url=driver.getCurrentUrl();
		
		//Seperate authorization code from url by using split method
		String partialCode=url.split("code=")[1];
		String code=partialCode.split("&scope")[0];
		driver.close();
		
		//code to get access token and passing contract details in query params
		String accessTokenResponse=given().urlEncodingEnabled(false)
		.queryParams("code",code)
		.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type", "authorization_code")
		.when().post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		// parsing response and get access token from response.
		JsonPath js=new JsonPath(accessTokenResponse);
		String AccessToken= js.getString("access_token");
		
		//Actual method to access the url throgh access token 
	/*	String response= given().queryParam("access_token", AccessToken)
		.when().log().all()
		.get("https://rahulshettyacademy.com/getCourse.php").asString();
		
		System.out.println(response);*/
		
		// By using POJO class
		
		GetCourses gc= given().queryParam("access_token", AccessToken).expect().defaultParser(Parser.JSON)
		.when()
		.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourses.class);
		
		String LinkedInUrl=gc.getLinkedIn();
		System.out.println(LinkedInUrl);
	/*Courses  Courses=gc.getCourses();
		System.out.println(Courses.toString());  */
		
		String expertise=gc.getExpertise();
		System.out.println(expertise);
		
		List<api> apiCourses=gc.getCourses().getApi();
		
		for(int i=0;i<apiCourses.size();i++) {
			 String courseTitle = apiCourses.get(i).getCourseTitle();
			 if(courseTitle.equals("Rest Assured Automation using Java")) {
				String price=apiCourses.get(i).getPrice();
				System.out.println(price);
			 }
			
		}
		
		
		//Get course name of webAutomation
		
		List<webAutomation> w = gc.getCourses().getWebAutomation();
		for(int i=0;i<w.size();i++) {
		System.out.println(w.get(i).getCourseTitle());
		}
		
		String[] coursesTitle= {"Selenium Webdriver Java","Cypress","Protractor"};
		
		ArrayList<String> ActualCourseTitle=new ArrayList<String>();
		for(int i=0;i<w.size();i++) {
			ActualCourseTitle.add(w.get(i).getCourseTitle());
			}
		
		List<String> ExpectedCourseTitle=Arrays.asList(coursesTitle);
		
		Assert.assertTrue(ActualCourseTitle.equals(ExpectedCourseTitle));
		
		
		

	}

}
