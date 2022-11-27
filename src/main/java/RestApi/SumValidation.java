package RestApi;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.PayLoad;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	@Test
	public void sumOfCourses() {
		
		JsonPath js=new JsonPath(PayLoad.CoursePrice());
		
		int count = js.getInt("courses.size()");
		int sum=0;
		
		for(int i=0;i<count;i++) {
			int prices=js.getInt("courses["+i+"].price");
			int copies=js.getInt("courses["+i+"].copies");
			int amount =prices*copies;
			System.out.println(amount);
			sum=sum+amount;
		}
		System.out.println(sum);
		
		int purchaseAmount=js.getInt("dashboard.purchaseAmount");
		
		Assert.assertEquals(sum, purchaseAmount);
		
	}
}
