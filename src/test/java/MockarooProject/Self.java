package MockarooProject;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Self {

	WebDriver driver;
	List<String> cities;
	List<String> countries;
	Set<String> citiesSet;
	Set<String> countriesSet;

	int lineCount;
	String temp;

	// ========= 2. Navigate to https://mockaroo.com/
	@BeforeClass
	public void set() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();

		driver.get("https://mockaroo.com/");

		cities = new ArrayList();
		countriesSet = new HashSet<>();
		citiesSet = new HashSet<>();
		countries = new ArrayList();
	}

	// =========3. Assert title is correct.
	@Test(priority = 1)
	public void Title() {
		assertEquals(driver.getTitle(),
				"Mockaroo - Random Data Generator and API Mocking Tool | JSON / CSV / SQL / Excel");
	}

	// =========4. Assert Mockaroo and realistic data generator are displayed
	@Test(priority = 2)
	public void display() {
		assertEquals(driver.findElement(By.xpath("//div[@class='brand']")).getText(), "mockaroo");
		assertEquals(driver.findElement(By.xpath("//div[@class='tagline']")).getText(), "realistic data generator");
	}

	// =========5. Remote all existing fields by clicking on x icon link
	@Test(priority = 3)
	public void remote() {
		List<WebElement> remote = driver
				.findElements(By.xpath("//a[@class='close remove-field remove_nested_fields']"));
		for (WebElement each : remote) {
			each.click();
		}
	}

	// =========6. Assert that ‘Field Name’ , ‘Type’, ‘Options’ labels are displayed
	@Test(priority = 4)
	public void display2() {
		assertEquals(driver.findElement(By.xpath("//div[@class='column column-header column-name']")).getText(),
				"Field Name");
		assertEquals(driver.findElement(By.xpath("//div[@class='column column-header column-type']")).getText(),
				"Type");
		assertEquals(driver.findElement(By.xpath("//div[@class='column column-header column-options']")).getText(),
				"Options");
	}

	// =========7. Assert that ‘Add another field’ button is enabled. Find using
	// xpath with tagname and text. isEnabled() method in selenium
	@Test(priority = 5)
	public void display3() {
		assertEquals(driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']"))
				.getText(), "Add another field");
		assertTrue(driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']"))
				.isEnabled());
	}

	// =========8. Assert that default number of rows is 1000.
	@Test(priority = 6)
	public void defaultNumber() {
		String actual = driver.findElement(By.cssSelector("input[id='num_rows']")).getAttribute("value");
		assertEquals(actual, "1000");
	}

	// =========9. Assert that default format selection is CSV
	@Test(priority = 7)
	public void CSV() {
		Select select = new Select(driver.findElement(By.cssSelector("select.form-control#schema_file_format")));
		String actual = select.getFirstSelectedOption().getText();
		assertEquals(actual, "CSV");
	}

	// =========10. Assert that Line Ending is Unix(LF)
	@Test(priority = 8)
	public void UNIX() {
		// Select select = new
		// Select(driver.findElement(By.xpath("//select[@id='schema_line_ending']")));
		Select select = new Select(driver.findElement(By.id("schema_line_ending")));
		String actual = select.getFirstSelectedOption().getText();
		assertEquals(actual, "Unix (LF)");
	}

	// =========11. Assert that header checkbox is checked and BOM is unchecked
	@Test(priority = 9)
	public void checkbox() {
		assertTrue(driver.findElement(By.xpath("//input[@id='schema_include_header']")).isSelected());
		assertFalse(driver.findElement(By.xpath("//input[@id='schema_bom']")).isSelected());
	}

	// =========12. Click on ‘Add another field’ and enter name “City”
	// =========13. Click on Choose type and assert that Choose a Type dialog box is
	// displayed.
	// =========14. Search for “city” and click on City on search results.
	@Test(priority = 10)
	public void chooseCity() throws InterruptedException {
		driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']")).click();
		driver.findElement(By.xpath("(//input[@class='column-name form-control'])[7]")).sendKeys("City");
		driver.findElement(By.xpath("(//input[@class='btn btn-default'])[7]")).click();
		Thread.sleep(2000);
		assertTrue((driver.findElement(By.xpath("//h3[@class='modal-title'][.='Choose a Type']"))).isDisplayed());
		driver.findElement(By.xpath("//input[@id='type_search_field']")).sendKeys("city");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//div[@class='examples'])")).click();

	}

	// =========15. Repeat steps 12-14 with field name and type “Country”
	@Test(priority = 11)
	public void chooseCountry() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//input[@class='column-name form-control'])[8]")).sendKeys("Country");
		driver.findElement(By.xpath("(//input[@class='btn btn-default'])[8]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='type_search_field']")).clear();
		driver.findElement(By.xpath("//input[@id='type_search_field']")).sendKeys("Country");
		driver.findElement(By.xpath("(//div[@class='examples'])[1]")).click();

	}

	// =========16. Click on Download Data.
	@Test(priority = 12)
	public void download() throws InterruptedException {
		Thread.sleep(2000);
		// driver.findElement(By.xpath("//button[@id='download'][@class='btn
		// btn-success']")).click();
		Thread.sleep(5000);
	}

	// =========18. Assert that first row is matching with Field names that we
	// selected.
	// =========19. Assert that there are 1000 records
	@Test(priority = 13)
	public void assertFiled() throws IOException, InterruptedException {

		loadLists();
		Thread.sleep(2000);
		assertEquals(lineCount, 1000);
	}

	
	// =========17. Open the downloaded file using BufferedReader.
	// =========20. From file add all Cities to Cities ArrayList
	// =========21. Add all countries to Countries ArrayList
	@Test(priority = 14)
	public void loadLists() throws IOException {
		FileReader reader = new FileReader("C:/Users/KNSA/Downloads/MOCK_DATA (3).csv");
		BufferedReader breader = new BufferedReader(reader);

		String temp = breader.readLine();

		assertEquals(temp, "City,Country");

		lineCount = 0;
		temp = breader.readLine();

		String[] something = new String[2];

		while (temp != null) {

			something = temp.split(",");
			cities.add(something[0]);
			countries.add(something[1]);
			lineCount++;
			temp = breader.readLine();

		}
		reader.close();
		breader.close();
	}

	// =========22. Sort all cities and find the city with the longest name and shortest name
	// =========24. From file add all Cities to citiesSet HashSet
	// =========26. Add all Countries to countrySet HashSet
    @Test(priority=15)
    public void sorted() {
   
    	for(String each : cities) {
  	      citiesSet.add(each);
  	  }
    	for(String str :countries) {
    		countriesSet.add(str);
    	}

	int max = cities.get(0).length(), min = cities.get(0).length();
	String maxname="",minname="";
	for(String each:cities){
		if (each.length() > max) {
			max = each.length();
			maxname = each;
		}
	}
	for(String string:cities){
		if (string.length() < min) {
			min = string.length();
			minname = string;
		}
	}
	System.out.println("City-Name: Maximum Length is "+max+" "+maxname);
	System.out.println("City_Name: Minimum Length is "+min+" "+minname);

   }
    
    
	// =========23. In Countries ArrayList, find how many times each Country is
	// mentioned.and print out
    @Test (priority=16)
    public void freqCountry() {
    	for (String str : countriesSet) {
    	    System.out.println(str + " was listed " + Collections.frequency(countries, str)+" times");
    	  }
    }
    
    
    
    // =========25. Count how many unique cities are in Cities ArrayList and assert that it is matching with the count of citiesSet HashSet.
    @Test (priority=17)
    public void matchingUniqueCity() {
    	List<String> UniqueCity=new ArrayList<>();
    	
    	for (String str : cities) {
			if(!UniqueCity.contains(str)) {
				UniqueCity.add(str);
			}
		}
    	int size=UniqueCity.size();
    	assertEquals(size,citiesSet.size());
    }

    
    
    // =========27. Count how many unique cities are in Countries ArrayList and assert that it is matching with the count of countrySet HashSet.
    @Test (priority=17)
    public void matchingUniqueCountry() {
    	List<String> UniqueCountry=new ArrayList<>();
    	
    	for (String str : countries) {
			if(!UniqueCountry.contains(str)) {
				UniqueCountry.add(str);
			}
		}
    	int size=UniqueCountry.size();
    	assertEquals(size,countriesSet.size());
    }


}
