package com.miniorange.saml.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.miniorange.saml.helpers.Constants;
import com.miniorange.saml.helpers.ManageAttributes;
import com.miniorange.saml.helpers.SSO;
import com.miniorange.saml.models.Testcase;
import com.miniorange.saml.models.UserAttributes;

public class UserProfileTests {

	Set<String> ids;
	Iterator<String> it;
	String parentId, childId;
	UserAttributes user;

	SSO ssoObj = new SSO();
	ManageAttributes attributeHelper = new ManageAttributes();

	List<Testcase> testcases = new ArrayList<>();

	public List<Testcase> runTest(WebDriver driver) {
		getInitialState(driver);

		testcases.add(testRegex(driver));
		testcases.add(checkSeperateName(driver));
		testcases.add(incorrectUsernameMapping(driver));
		testcases.add(checkAttributeMapping(driver, user));
		testcases.add(checkDontUpdateAttribute(driver, user));

		createFinalState(driver);
		return testcases;
	}

	public Testcase testRegex(WebDriver driver) {
		try {
			if (!driver.findElement(By.id("regexPatternEnabled")).isSelected())
				driver.findElement(By.id("regexPatternEnabled")).click();

			driver.findElement(By.id("regexPattern")).clear();
			driver.findElement(By.id("regexPattern")).sendKeys("^.*?(?=@");
			driver.findElement(By.id("test-regex")).click();
			ids = driver.getWindowHandles();
			it = ids.iterator();
			parentId = it.next();
			childId = it.next();
			driver.switchTo().window(childId);
			System.out.println(driver.getTitle());
			driver.findElement(By.xpath("//*[@id='attrvalue']")).sendKeys(Constants.IDP_USERNAME);
			driver.findElement(By.xpath("//*[@id='testregex-button']")).click();
			String error = driver.findElement(By.xpath("//*[@id='error']")).getText();
			if (error.equals("Invalid Regex Expression.")) {
				driver.close();
				driver.switchTo().window(parentId);
				driver.findElement(By.id("regexPattern")).clear();
				driver.findElement(By.id("regexPattern")).sendKeys("^.*?(?=@)");
				driver.findElement(By.id("test-regex")).click();
				ids = driver.getWindowHandles();
				it = ids.iterator();
				parentId = it.next();
				childId = it.next();
				driver.switchTo().window(childId);
				System.out.println(driver.getTitle());
				driver.findElement(By.xpath("//*[@id='attrvalue']")).sendKeys(Constants.IDP_USERNAME);
				driver.findElement(By.xpath("//*[@id='testregex-button']")).click();
				String regexName = driver.findElement(By.xpath("//*[@id='result']")).getText();
				System.out.println("Regex Name: " + regexName);
				driver.close();
				driver.switchTo().window(parentId);
				if (regexName.equals(Constants.REGEX_NAME)) {
					driver.findElement(By.id("regexPatternEnabled")).click();
					return new Testcase("Regex Check", "PASS", "----");
				} else {
					driver.findElement(By.id("regexPatternEnabled")).click();
					return new Testcase("Regex Check", "FAIL", "Unable to detect right regex");
				}
			} else {
				driver.findElement(By.id("regexPatternEnabled")).click();
				return new Testcase("Regex Check", "FAIL", "Unable to detect wrong regex");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Regex Check", "FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkSeperateName(WebDriver driver) {
		try {
			WebElement fullNameBox = driver.findElement(By.id("fullNameAttribute"));
			if (!driver.findElement(By.id("useSeparateNameAttributes")).isSelected())
				driver.findElement(By.id("useSeparateNameAttributes")).click();

			WebElement firstNameBox = driver.findElement(By.id("firstNameAttribute"));
			WebElement lastNameBox = driver.findElement(By.id("lastNameAttribute"));

			System.out.println("LAST:-- " + lastNameBox.isDisplayed());
			System.out.println("FIRST:-- " + firstNameBox.isDisplayed());
			System.out.println("FULL:-- " + fullNameBox.isDisplayed());
			if (!fullNameBox.isDisplayed() && firstNameBox.isDisplayed() && lastNameBox.isDisplayed()) {
				driver.findElement(By.id("useSeparateNameAttributes")).click();
				return new Testcase(
						"If you check Separate Name Attributes then Full Name Attribute will be disabled and the First Name and Last Name field will enable.",
						"PASS", "----");
			} else {
				driver.findElement(By.id("useSeparateNameAttributes")).click();
				return new Testcase(
						"If you check Separate Name Attributes then Full Name Attribute will be disabled and the First Name and Last Name field will enable.",
						"FAIL", "Final state of elements is not correct.");
			}
		} catch (Exception e) {
			driver.findElement(By.id("useSeparateNameAttributes")).click();
			System.out.println(e);
			return new Testcase(
					"If you check Separate Name Attributes then Full Name Attribute will be disabled and the First Name and Last Name field will enable.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase incorrectUsernameMapping(WebDriver driver) {
		try {
			driver.findElement(By.id("usernameAttribute")).clear();
			driver.findElement(By.id("usernameAttribute")).sendKeys("abcdefghijkl");
			driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
			driver.findElement(By.linkText("Configure IDP")).click();
			driver.findElement(By.id("test-saml-configuration")).click();

			ids = driver.getWindowHandles();
			it = ids.iterator();
			parentId = it.next();
			childId = it.next();
			driver.switchTo().window(childId);

			String resultText = driver.findElement(By.xpath("/html/body/div[1]/div")).getText();
			System.out.println(resultText);
			if (resultText.equals("TEST FAILED")) {
				driver.close();
				driver.switchTo().window(parentId);
				driver.findElement(By.linkText("User Profile")).click();
				driver.findElement(By.id("usernameAttribute")).clear();
				driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
				return new Testcase(
						"Check if username attributes are not mapped properly. Test configuration will give an error.",
						"PASS", "----");
			} else {
				driver.close();
				driver.switchTo().window(parentId);
				driver.findElement(By.linkText("User Profile")).click();
				driver.findElement(By.id("usernameAttribute")).clear();
				driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
				return new Testcase(
						"Check if username attributes are not mapped properly. Test configuration will give an error.",
						"FAIL", "Test configuration did not failed");
			}
		} catch (Exception e) {
			driver.close();
			driver.switchTo().window(parentId);
			driver.findElement(By.linkText("User Profile")).click();
			driver.findElement(By.id("usernameAttribute")).clear();
			driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
			System.out.println(e.toString());
			return new Testcase(
					"Check if username attributes are not mapped properly. Test configuration will give an error.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}

	}

	public Testcase checkAttributeMapping(WebDriver driver, UserAttributes user) {
		try {
			Select attributeDropDown = new Select(driver.findElement(By.xpath("//*[@id='loginUserAttribute']")));
			attributeDropDown.selectByValue("username");
			driver.findElement(By.id("usernameAttribute")).clear();
			driver.findElement(By.id("usernameAttribute")).sendKeys(user.email);
			driver.findElement(By.xpath("//*[@id='emailAttribute']")).clear();
			driver.findElement(By.xpath("//*[@id='emailAttribute']")).sendKeys(user.email);
			driver.findElement(By.id("fullNameAttribute")).clear();
			driver.findElement(By.id("fullNameAttribute")).sendKeys(user.email);
			driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
			if ((ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0)) {
				return new Testcase("Check User Attribute Mapping", "FAIL", "SSO Failed");

			} else {
				driver.findElement(By.xpath("//*[@id='admin-menu-link']")).click();
				driver.findElement(By.linkText("User management")).click();
				driver.findElement(By.linkText("Show all users")).click();

				System.out.println("EMAIL:-- " + user.userEmail);
				int userCount = driver.findElements(By.linkText(user.userEmail)).size();
				System.out.println(userCount);
				if (userCount > 0) {
					driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
					driver.findElement(By.linkText("User Profile")).click();
					return new Testcase("Check User Attribute Mapping", "PASS", "----");
				} else {
					driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
					driver.findElement(By.linkText("User Profile")).click();
					return new Testcase("Check User Attribute Mapping", "FAIL", "Unable to find the user");
				}
			}
		} catch (Exception e) {
			driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
			driver.findElement(By.linkText("User Profile")).click();
			System.out.println(e.toString());
			return new Testcase("Check User Attribute Mapping", "FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkDontUpdateAttribute(WebDriver driver, UserAttributes user) {
		try {

			driver.findElement(By.id("fullNameAttribute")).clear();
			driver.findElement(By.id("fullNameAttribute")).sendKeys(user.fname);
			driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
			if ((ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0)) {
				return new Testcase(
						"Check the attribute of the existing user is updated or not according to the value of the Keep Existing User Attributes",
						"FAIL", "SSO Failed");
			} else {
				if (checkIfUserIsCreatedOrNot(driver, user.username)) {
					driver.findElement(By.id("useSeparateNameAttributes")).click();
					driver.findElement(By.id("firstNameAttribute")).clear();
					driver.findElement(By.id("firstNameAttribute")).sendKeys(user.fname);
					driver.findElement(By.id("lastNameAttribute")).clear();
					driver.findElement(By.id("lastNameAttribute")).sendKeys(user.lname);
					driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
					if ((ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0)) {
						return new Testcase(
								"Check the attribute of the existing user is updated or not according to the value of the Keep Existing User Attributes",
								"FAIL", "SSO Failed");
					} else {
						String name = user.username + " " + user.userLastName;
						System.out.println("SEPERATE NAME: " + name);
						if (checkIfUserIsCreatedOrNot(driver, name)) {
							driver.findElement(By.id("useSeparateNameAttributes")).click();
							driver.findElement(By.id("fullNameAttribute")).clear();
							driver.findElement(By.id("fullNameAttribute")).sendKeys(user.fname);
							driver.findElement(By.id("keepExistingUserAttributes")).click();
							driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
							if ((ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0)) {
								return new Testcase(
										"Check the attribute of the existing user is updated or not according to the value of the Keep Existing User Attributes",
										"FAIL", "SSO Failed");
							} else {
								if (checkIfUserIsCreatedOrNot(driver, user.username)) {
									return new Testcase(
											"Check the attribute of the existing user is updated or not according to the value of the Keep Existing User Attributes",
											"FAIL", "Existing user full name changed even when option is checked");
								} else {
									return new Testcase(
											"Check the attribute of the existing user is updated or not according to the value of the Keep Existing User Attributes",
											"PASS", "----");
								}
							}

						} else {
							return new Testcase(
									"Check the attribute of the existing user is updated or not according to the value of the Keep Existing User Attributes",
									"FAIL",
									"Unable to update user full name when option is unchecked and seperate atributes are used");
						}

					}
				} else {
					return new Testcase(
							"Check the attribute of the existing user is updated or not according to the value of the Keep Existing User Attributes",
							"FAIL", "Unable to update user full name when option is unchecked");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return new Testcase(
					"Check the attribute of the existing user is updated or not according to the value of the Keep Existing User Attributes",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public void getInitialState(WebDriver driver) {
		driver.get(Constants.BASE_URL_1 + "/plugins/servlet/saml/moconfreset");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.linkText("Configure IDP")).click();
		driver.findElement(By.linkText("Import From Metadata")).click();
		driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
		Select s = new Select(driver.findElement(By.id("metadataOption")));
		s.selectByValue("fromUrl");
		driver.findElement(By.id("inputUrl")).sendKeys(Constants.IMPORT_DATA_URL);
		driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
		driver.findElement(By.xpath("//input[@value='Save']")).click();
		driver.findElement(By.id("test-saml-configuration")).click();

		ids = driver.getWindowHandles();
		it = ids.iterator();
		parentId = it.next();
		childId = it.next();
		driver.switchTo().window(childId);

		driver.findElement(By.xpath("//input[contains(@id,'username')]")).sendKeys(Constants.IDP_USERNAME);
		driver.findElement(By.xpath("//input[contains(@id,'password')]")).sendKeys(Constants.IDP_PASSWORD);
		driver.findElement(By.xpath("//*[@id='okta-signin-submit']")).click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String resultText = driver.findElement(By.xpath("/html/body/div[1]/div")).getText();
		System.out.println(resultText);
		if (resultText.equals("TEST SUCCESSFUL")) {
			user = attributeHelper.setAttributes(driver);
		}
		driver.close();
		driver.switchTo().window(parentId);
		driver.findElement(By.linkText("User Profile")).click();
	}

	public void createFinalState(WebDriver driver) {
		if (driver.findElement(By.id("keepExistingUserAttributes")).isSelected())
			driver.findElement(By.id("keepExistingUserAttributes")).click();

		if (driver.findElement(By.id("useSeparateNameAttributes")).isSelected())
			driver.findElement(By.id("useSeparateNameAttributes")).click();

		driver.findElement(By.id("fullNameAttribute")).clear();
		driver.findElement(By.id("fullNameAttribute")).sendKeys(user.fname);
		driver.findElement(By.id("usernameAttribute")).clear();
		driver.findElement(By.id("usernameAttribute")).sendKeys(user.email);
		driver.findElement(By.xpath("//*[@id='emailAttribute']")).clear();
		driver.findElement(By.xpath("//*[@id='emailAttribute']")).sendKeys(user.email);
		driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
	}

	public boolean checkIfUserIsCreatedOrNot(WebDriver driver, String searchParameter) {
		int userCount = 0;
		driver.findElement(By.xpath("//*[@id='admin-menu-link']")).click();
		driver.findElement(By.linkText("User management")).click();
		driver.findElement(By.linkText("Show all users")).click();
		userCount = driver.findElements(By.linkText(searchParameter)).size();
		if (userCount == 1) {
			driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
			driver.findElement(By.linkText("User Profile")).click();
			return true;
		} else {
			driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
			driver.findElement(By.linkText("User Profile")).click();
			return false;
		}

	}

}
