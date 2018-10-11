package com.miniorange.saml.tests;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.miniorange.saml.helpers.Constants;
import com.miniorange.saml.helpers.ManageAttributes;
import com.miniorange.saml.helpers.SSO;
import com.miniorange.saml.models.Testcase;
import com.miniorange.saml.models.UserAttributes;

public class SSOTests {

	Set<String> ids;
	Iterator<String> it;
	String parentId, childId;
	UserAttributes user;
	SSO ssoObj = new SSO();

	List<Testcase> testcases = new ArrayList<>();
	ManageAttributes attributeHelper = new ManageAttributes();

	public List<Testcase> runTest(WebDriver driver) {
		getInitialState(driver);
		testcases.add(checkLoginButtonText(driver));
		testcases.add(checkRelayStateURL(driver));
		testcases.add(checkDefaultRelayState(driver));
		testcases.add(autoRedirectToIDP(driver));
		testcases.add(autoRedirectWithDelay(driver));
		testcases.add(ssoErrorOnAutoRedirect(driver));
		testcases.add(relayStateAndAutoRedirect(driver));
		testcases.add(randomPageAutoRedirect(driver));
		testcases.add(enableBackDoorLogin(driver));
		testcases.add(loginAsAdmin(driver));
		testcases.add(loginAsUser(driver));
		testcases.add(autoRedirectAdmin(driver));
		testcases.add(customLogout(driver));
		// testcases.add(customLogoutTemplate(driver));

		return testcases;
	}

	public Testcase checkLoginButtonText(WebDriver driver) {
		// try {
		driver.findElement(By.xpath("//*[@id='loginButtonText']")).clear();
		driver.findElement(By.xpath("//*[@id='loginButtonText']")).sendKeys("SAML LOGIN");
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();

		WebDriver ssodriver = new ChromeDriver();
		ssodriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ssodriver.get("http://localhost:8090");
		int button = ssodriver.findElements(By.linkText("SAML LOGIN")).size();
		ssodriver.close();
		if (button > 0) {
			System.out.println("Change Login Button Text PASSED");
			driver.findElement(By.xpath("//*[@id='loginButtonText']")).clear();
			driver.findElement(By.xpath("//*[@id='loginButtonText']")).sendKeys("Use Corporate Login");
			driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
			return new Testcase("Change Login Button Text", "PASS", "----");
		} else {
			System.out.println("Change Login Button Text FAILED");
			driver.findElement(By.xpath("//*[@id='loginButtonText']")).clear();
			driver.findElement(By.xpath("//*[@id='loginButtonText']")).sendKeys("Use Corporate Login");
			driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
			return new Testcase("Change Login Button Text", "FAIL", "Login button text not changed");
		}
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			return new Testcase("Change Login Button Text", "FAIL", "SCRIPT FAILED:- " + e.toString());
//		}
	}

	public Testcase checkRelayStateURL(WebDriver driver) {
//		try {
		driver.findElement(By.id("relayState")).sendKeys(Constants.RELAY_STATE_URL);
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		String URL = ssoObj.getValueAfterSSO(Constants.IDP_USERNAME, Constants.IDP_PASSWORD, Constants.BASE_URL_1,
				"URL");
		if (URL.equals(Constants.RELAY_STATE_URL)) {
			driver.findElement(By.id("relayState")).clear();
			driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
			return new Testcase("Set relay state and the user should be redirected to that URL after login", "PASS",
					"----");
		} else {
			driver.findElement(By.id("relayState")).clear();
			driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
			return new Testcase("Set relay state and the user should be redirected to that URL after login", "FAIL",
					"user was not redirected to relay state url.s");
		}
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			return new Testcase("Set relay state and the user should be redirected to that URL after login", "FAIL",
//					"SCRIPT FAILED:- " + e.toString());
//		}
	}

	public Testcase checkDefaultRelayState(WebDriver driver) {
		// try {
		String hostname = ssoObj.getValueAfterSSO(Constants.IDP_USERNAME, Constants.IDP_PASSWORD, Constants.BASE_URL_1,
				"HOSTNAME");
		if (hostname.equals("localhost")) {
			return new Testcase("Don't set relay state and the user should go to default page", "PASS", "----");
		} else {
			return new Testcase("Don't set relay state and the user should go to default page", "FAIL",
					"user was not redirected to localhost domain");
		}
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			return new Testcase("Don't set relay state and the user should go to default page", "FAIL",
//					"SCRIPT FAILED:- " + e.toString());
//		}
	}

	public Testcase autoRedirectToIDP(WebDriver driver) {
		// try {
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		String hostname = ssoObj.getHostName();
		System.out.println("Hostname After save: " + hostname);
		if (hostname.equals(Constants.IDP_HOST_NAME)) {
			driver.findElement(By.id("disableDefaultLogin")).click();
			driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
			return new Testcase("Hide default Login-Form and redirect user to the IdP", "PASS", "----");
		} else {
			driver.findElement(By.id("disableDefaultLogin")).click();
			driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
			return new Testcase("Hide default Login-Form and redirect user to the IdP", "FAIL",
					"user was not redirected to right IDP");
		}
//		} catch (Exception e) {
//			driver.findElement(By.id("disableDefaultLogin")).click();
//			driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
//			System.out.println(e.toString());
//			return new Testcase("Hide default Login-Form and redirect user to the IdP", "FAIL",
//					"SCRIPT FAILED:- " + e.toString());
//		}

	}

	public Testcase autoRedirectWithDelay(WebDriver driver) {
		driver.findElement(By.id("disableDefaultLogin")).click();
		if (!driver.findElement(By.id("enableAutoRedirectDelay")).isSelected())
			driver.findElement(By.id("enableAutoRedirectDelay")).click();

		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		WebDriver ssodriver = new ChromeDriver();
		ssodriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ssodriver.get("http://localhost:8090");
		int size = ssodriver.findElements(By.id("moRedirectBox")).size();
		if (size > 0) {
			try {
				Thread.sleep(11000);
				URI uri = null;
				uri = new URI(ssodriver.getCurrentUrl());
				String domain = uri.getHost();
				domain = domain.startsWith("www.") ? domain.substring(4) : domain;
				ssodriver.close();
				System.out.println("Hostname After save: " + domain);
				if (domain.equals(Constants.IDP_HOST_NAME)) {
					driver.findElement(By.id("enableAutoRedirectDelay")).click();
					driver.findElement(By.id("disableDefaultLogin")).click();
					driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
					return new Testcase("Turn on auto-redirect delay: there should be delay on auto-redirect", "PASS",
							"----");
				} else {
					driver.findElement(By.id("enableAutoRedirectDelay")).click();
					driver.findElement(By.id("disableDefaultLogin")).click();
					driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
					return new Testcase("Turn on auto-redirect delay: there should be delay on auto-redirect", "FAIL",
							"user was not redirected to right IDP");
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				return new Testcase("Turn on auto-redirect delay: there should be delay on auto-redirect", "FAIL",
						"SCRIPT FAILED:- " + e.toString());
			}

		} else {
			return new Testcase("Turn on auto-redirect delay: there should be delay on auto-redirect", "FAIL",
					"Delay box was not present on login page");
		}
	}

	public Testcase ssoErrorOnAutoRedirect(WebDriver driver) {
		driver.findElement(By.linkText("User Groups")).click();
		driver.findElement(By.id("restrictUserCreation")).click();
		driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
		driver.findElement(By.linkText("SSO Settings")).click();
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.id("enableAutoRedirectDelay")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		List<Integer> response = ssoObj.doSSoWithDelay(Constants.IDP_USERNAME, Constants.IDP_PASSWORD, 0);
		Testcase newTest;
		if (response.get(0) > 0) {
			if (response.get(1) > 0) {
				newTest = new Testcase(" On SSO error, there should be no auto-redirect without delay and no delay UI.",
						"FAIL", "Progress box is still visible");
			} else {
				newTest = new Testcase(" On SSO error, there should be no auto-redirect without delay and no delay UI.",
						"PASS", "----");
			}
		} else {
			newTest = new Testcase(" On SSO error, there should be no auto-redirect without delay and no delay UI.",
					"FAIL", "Auto redirect is working even after SSO Failure");
		}

		driver.findElement(By.linkText("User Groups")).click();
		driver.findElement(By.id("restrictUserCreation")).click();
		driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
		driver.findElement(By.linkText("SSO Settings")).click();
		driver.findElement(By.id("enableAutoRedirectDelay")).click();
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		return newTest;
	}

	public Testcase relayStateAndAutoRedirect(WebDriver driver) {
		driver.findElement(By.id("relayState")).sendKeys(Constants.RELAY_STATE_URL);
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.id("enableAutoRedirectDelay")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		List<Integer> response = ssoObj.doSSoWithDelay(Constants.IDP_USERNAME, Constants.IDP_PASSWORD, 1);
		Testcase newTest;
		if (response.get(1) > 0) {
			if (response.get(0) > 0) {
				newTest = new Testcase(
						"Turn on relay state and auto-redirect should go to the URL, test for delay also", "FAIL",
						"User redirected to wrong rely state");
			} else {
				newTest = new Testcase(
						"Turn on relay state and auto-redirect should go to the URL, test for delay also", "PASS",
						"----");
			}
		} else {
			newTest = new Testcase("Turn on relay state and auto-redirect should go to the URL, test for delay also",
					"FAIL", "Delay UI is not present");
		}
		driver.findElement(By.id("relayState")).clear();
		driver.findElement(By.id("enableAutoRedirectDelay")).click();
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		return newTest;
	}

	public Testcase randomPageAutoRedirect(WebDriver driver) {
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		String URL = ssoObj.getValueAfterSSO(Constants.IDP_USERNAME, Constants.IDP_PASSWORD, Constants.BASE_URL_RANDOM,
				"URL");
		Testcase newTest;
		if (URL.equals(Constants.BASE_URL_RANDOM)) {
			newTest = new Testcase(
					"Start with a random page and auto-redirect should take you back there, test for delay also",
					"PASS", "----");
		} else {
			newTest = new Testcase(
					"Start with a random page and auto-redirect should take you back there, test for delay also",
					"FAIL", "user was not redirect to same page");
		}
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		return newTest;
	}

	public Testcase enableBackDoorLogin(WebDriver driver) {
		driver.findElement(By.id("disableDefaultLogin")).click();
		if (!driver.findElement(By.id("enableBackdoor")).isSelected())
			driver.findElement(By.id("enableBackdoor")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		WebDriver ssodriver = new ChromeDriver();
		ssodriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ssodriver.get(Constants.BACKDOOR_URL);
		int loginPage = ssodriver.findElements(By.linkText("Use Corporate Login")).size();
		ssodriver.close();
		Testcase newTest;
		if (loginPage > 0) {
			newTest = new Testcase("Enable backdoor: Stop autoredirect and display default login-form", "PASS", "----");
		} else {
			newTest = new Testcase("Enable backdoor: Stop autoredirect and display default login-form", "FAIL",
					"Not showing login form");
		}

		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();

		return newTest;
	}

	public Testcase loginAsAdmin(WebDriver driver) {
		// deleteUser(driver, user.username);
		// WebDriverWait d = new WebDriverWait(driver, 10);
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.id("enableAutoRedirectDelay")).click();
		driver.findElement(By.id("noAdminSessionOption")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		Testcase newTest;
		if (ssoObj.doSSoAndCheckAdminAccess(Constants.IDP_USERNAME, Constants.IDP_PASSWORD, 0))
			newTest = new Testcase("SSO Settings: Login as admin only once", "PASS", "----");
		else
			newTest = new Testcase("SSO Settings: Login as admin only once", "FAIL", "----");

		driver.findElement(By.id("enableAutoRedirectDelay")).click();
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		return newTest;
	}

	public Testcase loginAsUser(WebDriver driver) {
		deleteUser(driver, user.username);
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.id("enableAutoRedirectDelay")).click();
		driver.findElement(By.id("disableAdminSession")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		Testcase newTest;
		if (ssoObj.doSSoAndCheckAdminAccess(Constants.IDP_USERNAME, Constants.IDP_PASSWORD, 1))
			newTest = new Testcase("SSO Settings: Login as user", "PASS", "----");
		else
			newTest = new Testcase("SSO Settings: Login as user", "FAIL", "----");

		driver.findElement(By.id("enableAutoRedirectDelay")).click();
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.id("noAdminSessionOption")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		return newTest;
	}

	public Testcase autoRedirectAdmin(WebDriver driver) {
		deleteUser(driver, user.username);
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.id("enableAutoRedirectDelay")).click();
		driver.findElement(By.id("redirectOnAdminTimeout")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		Testcase newTest;
		if (ssoObj.doSSoAndCheckAdminAccess(Constants.IDP_USERNAME, Constants.IDP_PASSWORD, 2))
			newTest = new Testcase("SSO Settings: Auto-redirect admin", "PASS", "----");
		else
			newTest = new Testcase("SSO Settings: Auto-redirect admin", "FAIL", "----");

		driver.findElement(By.id("enableAutoRedirectDelay")).click();
		driver.findElement(By.id("disableDefaultLogin")).click();
		driver.findElement(By.id("noAdminSessionOption")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		return newTest;
	}

	public Testcase customLogout(WebDriver driver) {
		deleteUser(driver, user.username);
		if (driver.findElements(By.linkText("2. Show Sign Out Settings")).size() > 0)
			driver.findElement(By.linkText("2. Show Sign Out Settings")).click();

		driver.findElement(By.id("customLogoutURL")).clear();
		driver.findElement(By.id("customLogoutURL")).sendKeys(Constants.LOGOUT_URL);
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		Testcase newTest;
		if (ssoObj.doSSOAndGetLogoutUrl(Constants.IDP_USERNAME, Constants.IDP_PASSWORD, 0)
				.equals(Constants.LOGOUT_URL)) {
			newTest = new Testcase("SSO Settings: Custom Logout URL", "PASS", "----");
		} else {
			newTest = new Testcase("SSO Settings: Custom Logout URL", "FAIL", "----");
		}

		driver.findElement(By.id("customLogoutURL")).clear();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		return newTest;
	}

	// Not Done
	public Testcase customLogoutTemplate(WebDriver driver) {
		deleteUser(driver, user.username);
		if (driver.findElements(By.linkText("2. Show Sign Out Settings")).size() > 0)
			driver.findElement(By.linkText("2. Show Sign Out Settings")).click();

		if (!driver.findElement(By.id("enablelogoutTemplate")).isSelected())
			driver.findElement(By.id("enablelogoutTemplate")).click();

		Testcase newTest;
		if (ssoObj.doSSOAndGetLogoutUrl(Constants.IDP_USERNAME, Constants.IDP_PASSWORD, 1)
				.equals("Logout - Confluence")) {
			newTest = new Testcase("SSO Settings: Custom Logout Template", "PASS", "----");
		} else {
			newTest = new Testcase("SSO Settings: Custom Logout Template", "FAIL", "----");
		}

		driver.findElement(By.id("enablelogoutTemplate")).click();
		driver.findElement(By.xpath("//*[@id='signin-settings-form']/div[5]/input")).click();
		return newTest;
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
		driver.findElement(By.id("usernameAttribute")).clear();
		driver.findElement(By.id("usernameAttribute")).sendKeys(user.email);
		driver.findElement(By.xpath("//*[@id='emailAttribute']")).clear();
		driver.findElement(By.xpath("//*[@id='emailAttribute']")).sendKeys(user.email);
		driver.findElement(By.id("fullNameAttribute")).clear();
		driver.findElement(By.id("fullNameAttribute")).sendKeys(user.fname);
		driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
		driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/a[1]/input")).click();
		driver.findElement(By.xpath("//*[@id='roleAttribute']")).clear();
		driver.findElement(By.xpath("//*[@id='roleAttribute']")).sendKeys(user.grpAttrName);
		driver.findElement(By.xpath("//*[@id='confluence-administrators']")).clear();
		driver.findElement(By.xpath("//*[@id='confluence-administrators']")).sendKeys(user.adminGrp);
		driver.findElement(By.xpath("//*[@id='confluence-users']")).clear();
		driver.findElement(By.xpath("//*[@id='confluence-users']")).sendKeys(user.userGrp);
		driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
		driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/a[1]/input")).click();
		deleteUser(driver, user.username);
		deleteUser(driver, Constants.IDP_FIRST_NAME);
	}

	public void deleteUser(WebDriver driver, String linkName) {
		driver.findElement(By.xpath("//*[@id='admin-menu-link']")).click();
		driver.findElement(By.linkText("User management")).click();
		driver.findElement(By.linkText("Show all users")).click();
		if (driver.findElements(By.linkText(linkName)).size() > 0) {
			driver.findElement(By.linkText(linkName)).click();
			driver.findElement(By.linkText("Delete")).click();
			driver.findElement(By.id("confirm")).click();
		}
		driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
		driver.findElement(By.linkText("SSO Settings")).click();

	}

}
