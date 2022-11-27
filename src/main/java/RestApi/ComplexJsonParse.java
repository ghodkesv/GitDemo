package RestApi;


import files.PayLoad;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JsonPath js=new JsonPath(PayLoad.CoursePrice());
		
		//Print No of courses returned by API
		//size() method can only apply on array
		int count = js.getInt("courses.size()");
		System.out.println(count);
		
		//Print Purchase Amount
		int TotalAmmount=js.getInt("dashboard.purchaseAmount");
		System.out.println(TotalAmmount);
		
		//Print Title of the first course
		String TitleOfFirstCourse= js.get("courses[0].title");
		System.out.println(TitleOfFirstCourse);
		
		//Print All course titles and their respective Prices
		
		for(int i=0;i<count;i++) {
			String Title=js.get("courses["+i+"].title").toString();
			System.out.println(Title);
			
			String Prices=js.get("courses["+i+"].price").toString();
			System.out.println(Prices);
		}
		
		//Print no of copies sold by RPA Course
		
		for(int i=0;i<count;i++) {
			String Title=js.get("courses["+i+"].title");
			if(Title.equalsIgnoreCase("RPA")) {
				int Copy=js.get("courses["+i+"].copies");
				System.out.println("Print no of copies sold by RPA Course : "+Copy);
				break;
			}
		}
		
		//Verify if Sum of all Course prices matches with Purchase Amount
		
	}
}
