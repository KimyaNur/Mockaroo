package MockarooProject;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MockarooDataValidation {

	private static final String priority = null;
	WebDriver driver;
	List<String> cities;
	List<String> countries;
	Set<String> citiesSet;
	Set<String> countriesSet;

	int lineCount;

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://mockaroo.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		cities = new ArrayList();
	    countriesSet= new HashSet<>();
	    citiesSet=new HashSet<>();
	    countries = new ArrayList();
	}

	@Test(priority = 1)
	public void verifyTitle() {
		String actual = driver.getTitle();
		String expected = "Mockaroo - Random Data Generator and API Mocking Tool | JSON / CSV / SQL / Excel";
		assertEquals(actual, expected);
	}

	@Test(priority = 2)
	public void verifyHeader() {
		String actual = driver.findElement(By.xpath("//a[@href='/']/div[1]")).getText();
		String expected = "mockaroo";
		assertEquals(actual, expected);
		actual = driver.findElement(By.xpath("//div[@class='tagline'][contains(text(),'realistic data generator')]"))
				.getText();
		expected = "realistic data generator";
		assertEquals(actual, expected);
		removeExcitingFields();
	}

	@Test(priority = 3)
	public void verifyTableHeader() {

		assertTrue(verifyStrings());
		assertTrue(driver.findElement(By.xpath("//a[.='Add another field']")).isEnabled());

		String actual = driver.findElement(By.cssSelector("input[id='num_rows']")).getAttribute("value");
		assertEquals(actual, "1000");

		Select select = new Select(driver.findElement(By.cssSelector("select.form-control#schema_file_format")));
		actual = select.getFirstSelectedOption().getText();
		String expected = "CSV";
		assertEquals(actual, expected);

		Select select2 = new Select(driver.findElement(By.cssSelector("select.form-control#schema_line_ending")));
		actual = select2.getFirstSelectedOption().getText();
		String expected2 = "Unix (LF)";
		assertEquals(actual, expected2);

		assertFalse(driver.findElement(By.xpath("//input[@name='schema[bom]'][@value='1']")).isSelected());
		assertTrue(driver.findElement(By.xpath("//input[@name='schema[include_header]'][@value='1']")).isSelected());

	}

	@Test(priority = 4)
	public void creatingFields() throws InterruptedException {
		// driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn
		// add_nested_fields']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[.='Add another field']")).click();
		driver.findElement(By.xpath("(//div[@class='column']//input[@placeholder='enter name...'])[7]"))
				.sendKeys("City");

		driver.findElement(By.xpath("(//input[@class='btn btn-default'])[7]")).click();

		Thread.sleep(1000);
		assertEquals("Choose a Type",
				driver.findElement(By.xpath("//h3[@class='modal-title'][.='Choose a Type']")).getText());

		driver.findElement(By.xpath("//input[@id='type_search_field']")).sendKeys("city");
		driver.findElement(By.xpath("//div[@class='examples']")).click();

		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//input[@class='column-name form-control'])[8]")).sendKeys("Country");
		driver.findElement(By.xpath("(//input[@class='btn btn-default'])[8]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//input[@id='type_search_field'])")).clear();
		driver.findElement(By.xpath("(//input[@id='type_search_field'])")).sendKeys("country");
		driver.findElement(By.xpath("//div[.='Country']")).click();
		Thread.sleep(2000);
		// driver.findElement(By.id("download")).click();
		Thread.sleep(5000);
	}

//	@Test(priority = 5)
//	public void verifyDownloadedData() throws IOException {
//		FileReader reader = new FileReader("C:/Users/KNSA/Downloads/MOCK_DATA (1).csv");
//		BufferedReader breader = new BufferedReader(reader);
//
//		String temp = breader.readLine();
//
//		assertEquals(temp, "City,Country");
//
//		int count = -1;
//
//		while (temp != null) {
//			count++;
//			temp = breader.readLine();
//		}
//
//		assertEquals(count, 1000);
//
//	}

	@Test (priority=5)
	  public void verifyDownloadedData() throws IOException {
	    loadLists();
	    assertEquals(lineCount, 1000);

	    sortCities();
	    findCountries();

	    loadSets();
	    
//	    int actual = removeDuplicates(cities);
//	    assertEquals(actual, citiesSet.size());
//	    
//	    actual=removeDuplicates(countries);
//	    
//	    assertEquals(actual, countriesSet.size());
	    
	  }
	
	@AfterClass
	public void teardown() {
		// driver.close();
		driver.quit();
	}

	public void removeExcitingFields() {

		List<WebElement> fields = driver
				.findElements(By.xpath("//a[@class='close remove-field remove_nested_fields']"));

		for (WebElement webElement : fields) {
			webElement.click();
		}
	}

	public boolean verifyStrings() {
		List<WebElement> actual = driver.findElements(By.xpath("//div[@class='table-header']//div"));
		List<String> expected = new ArrayList<>();
		expected.add("Field Name");
		expected.add("Type");
		expected.add("Options");

		for (int i = 0; i < actual.size(); i++) {
			if (!(actual.get(i).getText().equals(expected.get(i)))) {
				return false;
			}
		}
		return true;
	}

//	 public void sortCities() {
//		
//
//		try (FileReader reader = new FileReader("C:/Users/KNSA/Downloads/MOCK_DATA (1).csv");
//				BufferedReader breader = new BufferedReader(reader);) {
//
//			String value;
//			while ((value = breader.readLine()) != null) {
//				int a = value.indexOf(",");
//				cities.add(value.substring(0, a));
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	
//		List<String> listCountry = new ArrayList<>();
//		String s2 = "";
//
//		try (FileReader reader = new FileReader("C:/Users/KNSA/Downloads/MOCK_DATA (1).csv");
//				BufferedReader breader = new BufferedReader(reader);) {
//
//			String value;
//			while ((value = breader.readLine()) != null) {
//				int a = value.indexOf(",");
//				s2 = value.substring(a + 1);
//				listCountry.add(s2);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	
	
	
	
	public void loadSets() throws IOException {
	    
	    FileReader reader = new FileReader("C:/Users/KNSA/Downloads/MOCK_DATA (3).csv");
	    BufferedReader breader = new BufferedReader(reader);

	    String temp = breader.readLine();

	    temp = breader.readLine();

	    String[] something = new String[2];

	    while (temp != null) {

	      something = temp.split(",");
	      citiesSet.add(something[0]);
	      countriesSet.add(something[1]);
	      lineCount++;
	      temp = breader.readLine();
	  }
	    
	    reader.close();
	    breader.close();
	}
	
	
	
	
	public void sortCities() {

		String minname="";
		String maxname="";
	    Collections.sort(cities);
	    int max = cities.get(0).length();
	    for (String string : cities) {
	      if (string.length() > max)
	        max = string.length();
	      maxname=string;
	    }
     
	    int min = cities.get(0).length();
	    for (String string : cities) {
	      if (string.length() < min)
	        min = string.length();
	      minname=string;
	    }

	    System.out.println("City-Name: Maximum Length is " + max+maxname);
	    System.out.println("City_Name: Minimum Length is " + min+minname);
	  }
	

	 public void findCountries() {
		
		SortedSet<String> sortedCountry = new TreeSet<>(countries);
		for (String str : sortedCountry) {
			System.out.println(str + " was listed " + Collections.frequency(countries, str) + " times");
		}

	}
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

}
