package com.miniorange.saml.tests;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.miniorange.saml.helpers.Constants;
import com.miniorange.saml.models.Testcase;

public class SpInfoTests {

	List<Testcase> testcases = new ArrayList<>();

	public List<Testcase> runTest(WebDriver driver) {
		testcases.add(changeSPBaseURL(driver));
		testcases.add(changeSPEntityId(driver));
		testcases.add(accessMetadataLink(driver));
		testcases.add(checkCustomCertificate(driver));
		testcases.add(checkDefaultSettings(driver));
		testcases.add(checkSigningMetadata(driver));
		testcases.add(checkEncryptionInMetadata(driver));

		return testcases;
	}

	public Testcase changeSPBaseURL(WebDriver driver) {
		try {
			driver.findElement(By.id("spBaseUrl")).clear();
			driver.findElement(By.id("spBaseUrl")).sendKeys(Constants.BASE_URL_2);
			driver.findElement(By.xpath("//*[@id='configure-idp-form']/div[3]/input")).click();
			String acsURL = driver.findElement(By.id("p2")).getText();
			String singleLogoutURL = driver.findElement(By.id("p3")).getText();
			String recipientURL = driver.findElement(By.id("p5")).getText();
			String destinationURL = driver.findElement(By.id("p6")).getText();
			if (acsURL.contains(Constants.BASE_URL_2) && singleLogoutURL.contains(Constants.BASE_URL_2)
					&& recipientURL.contains(Constants.BASE_URL_2) && destinationURL.contains(Constants.BASE_URL_2)) {
				return new Testcase("Change in SP BASE URL should show reflection in other URLs. ", "PASS", "----");
			} else {
				return new Testcase("Change in SP BASE URL should show reflection in other URLs. ", "FAIL",
						"All the required URL not changed");
			}
		} catch (Exception e) {
			return new Testcase("Change in SP BASE URL should show reflection in other URLs. ", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase changeSPEntityId(WebDriver driver) {
		try {
			driver.findElement(By.id("spBaseUrl")).clear();
			driver.findElement(By.id("spBaseUrl")).sendKeys(Constants.BASE_URL_1);
			driver.findElement(By.id("spEntityId")).clear();
			driver.findElement(By.id("spEntityId")).sendKeys(Constants.BASE_URL_2);
			driver.findElement(By.xpath("//*[@id='configure-idp-form']/div[3]/input")).click();
			String spEntityId = driver.findElement(By.id("p1")).getText();
			String audienceURI = driver.findElement(By.id("p4")).getText();

			if (spEntityId.contains(Constants.BASE_URL_2) && audienceURI.contains(Constants.BASE_URL_2)) {
				return new Testcase(
						"Change in SP ENTITY ID should show reflection in other issuer id and audience uri. ", "PASS",
						"----");
			} else {
				return new Testcase(
						"Change in SP ENTITY ID should show reflection in other issuer id and audience uri. ", "FAIL",
						"All the required URL not changed");
			}
		} catch (Exception e) {
			return new Testcase("Change in SP ENTITY ID should show reflection in other issuer id and audience uri.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase accessMetadataLink(WebDriver driver) {

		WebDriver newWindow = new ChromeDriver();
		try {
			driver.findElement(By.id("spEntityId")).clear();
			driver.findElement(By.id("spEntityId")).sendKeys(Constants.BASE_URL_1);
			driver.findElement(By.xpath("//*[@id='configure-idp-form']/div[3]/input")).click();
			String metaDataUrl = driver.findElement(By.xpath("//*[@id='configure-idp']/p[4]/a/b")).getText();
			newWindow.get(metaDataUrl);
			String getXMLText = newWindow.findElement(By.cssSelector("body > div.header > span")).getText();

			System.out.println(getXMLText);

			if (getXMLText.equals(
					"This XML file does not appear to have any style information associated with it. The document tree is shown below.")) {
				newWindow.close();
				return new Testcase(
						"Metadata link should display SP Metadata and this URL should also accessible in without being logged in.",
						"PASS", "----");
			} else {
				newWindow.close();
				return new Testcase(
						"Metadata link should display SP Metadata and this URL should also accessible in without being logged in.",
						"FAIL", "----");
			}
		} catch (Exception e) {
			newWindow.close();
			System.out.println(e);
			return new Testcase(
					"Metadata link should display SP Metadata and this URL should also accessible in without being logged in.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkCustomCertificate(WebDriver driver) {

		WebDriver newWindow = new ChromeDriver();
		try {
			driver.findElement(By.linkText("Certificates")).click();
			driver.findElement(By.id("publicSPCertificate")).clear();
			driver.findElement(By.id("publicSPCertificate")).sendKeys(Constants.SP_PUBLIC_CERTIFICATE_2);
			driver.findElement(By.id("privateSPCertificate")).clear();
			driver.findElement(By.id("privateSPCertificate")).sendKeys(Constants.SP_PRIVATE_CERTIFICATE_2);
			driver.findElement(By.xpath("//*[@id='configure-certificates']/div[3]/input")).click();
			driver.findElement(By.linkText("SP Info")).click();
			String metaDataUrl = driver.findElement(By.xpath("//*[@id='configure-idp']/p[4]/a/b")).getText();
			newWindow.get(metaDataUrl);
			String getCertificate = newWindow
					.findElement(By.cssSelector("#collapsible5 > div.expanded > div.collapsible-content > span"))
					.getText();
			System.out.println(getCertificate);
			System.out.println(Constants.SP_PUBLIC_CERTIFICATE_2_STRING);
			if (getCertificate.equals(Constants.SP_PUBLIC_CERTIFICATE_2_STRING)) {
				newWindow.close();
				return new Testcase(
						"If you have added the custom certificate in Certificate Tab then custom public certificate should overwrite existing X.509 certificate in Metadata.",
						"PASS", "----");
			} else {
				newWindow.close();
				return new Testcase(
						"If you have added the custom certificate in Certificate Tab then custom public certificate should overwrite existing X.509 certificate in Metadata.",
						"FAIL", "New certificate was not overwritten");
			}
		} catch (Exception e) {
			newWindow.close();
			System.out.println(e);
			return new Testcase(
					"If you have added the custom certificate in Certificate Tab then custom public certificate should overwrite existing X.509 certificate in Metadata.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkDefaultSettings(WebDriver driver) {
		try {
			driver.findElement(By.linkText("Certificates")).click();
			driver.findElement(By.id("publicSPCertificate")).clear();
			driver.findElement(By.id("publicSPCertificate")).sendKeys(Constants.SP_PUBLIC_CERTIFICATE);
			driver.findElement(By.id("privateSPCertificate")).clear();
			driver.findElement(By.id("privateSPCertificate")).sendKeys(Constants.SP_PRIVATE_CERTIFICATE);
			driver.findElement(By.xpath("//*[@id='configure-certificates']/div[3]/input")).click();
			driver.findElement(By.linkText("SP Info")).click();
			if (driver.findElement(By.id("signing")).isSelected()
					&& !driver.findElement(By.id("encryption")).isSelected()) {
				return new Testcase(
						"By default Signing in metadata should be enable and Encryption in metadata should be disable.",
						"PASS", "----");
			} else {
				return new Testcase(
						"By default Signing in metadata should be enable and Encryption in metadata should be disable.",
						"FAIL", "Default settings is having wrong configuration.");
			}
		} catch (Exception e) {
			return new Testcase(
					"By default Signing in metadata should be enable and Encryption in metadata should be disable.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkSigningMetadata(WebDriver driver) {
		WebDriver newWindow = new ChromeDriver();
		try {
			if (!driver.findElement(By.id("signing")).isSelected()) {
				driver.findElement(By.id("signing")).click();
			}
			driver.findElement(By.xpath("//*[@id='set-certificates-form']/div[3]/input")).click();
			String metaDataUrl = driver.findElement(By.xpath("//*[@id='configure-idp']/p[4]/a/b")).getText();
			newWindow.get(metaDataUrl);
			int signElement = newWindow
					.findElements(By.cssSelector("#collapsible2 > div.expanded > div:nth-child(1) > span.html-tag"))
					.size();
			System.out.println("SIGNING ELEMENT:--" + signElement);
			if (signElement > 0) {
				newWindow.close();
				return new Testcase("Enable Include Signing in metadata and check if it is shown in metadata", "PASS",
						"----");
			} else {
				newWindow.close();
				return new Testcase("Enable Include Signing in metadata and check if it is shown in metadata", "FAIL",
						"md use signing is not present in XML file ");
			}
		} catch (Exception e) {
			newWindow.close();
			return new Testcase("Enable Include Signing in metadata and check if it is shown in metadata", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkEncryptionInMetadata(WebDriver driver) {
		WebDriver newWindow = new ChromeDriver();
		try {
			if (!driver.findElement(By.id("encryption")).isSelected()) {
				driver.findElement(By.id("encryption")).click();
			}
			driver.findElement(By.xpath("//*[@id='set-certificates-form']/div[3]/input")).click();
			String metaDataUrl = driver.findElement(By.xpath("//*[@id='configure-idp']/p[4]/a/b")).getText();
			newWindow.get(metaDataUrl);
			int encryptionElement = newWindow
					.findElements(By.cssSelector("#collapsible6 > div.expanded > div:nth-child(1) > span.html-tag"))
					.size();
			System.out.println("ENCRYPTION ELEMENT:--" + encryptionElement);
			if (encryptionElement > 0) {
				newWindow.close();
				driver.findElement(By.id("encryption")).click();
				driver.findElement(By.xpath("//*[@id='set-certificates-form']/div[3]/input")).click();
				return new Testcase("Enable Include Encryption in metadata and check if it is shown in metadata",
						"PASS", "----");
			} else {
				newWindow.close();
				driver.findElement(By.id("encryption")).click();
				driver.findElement(By.xpath("//*[@id='set-certificates-form']/div[3]/input")).click();
				return new Testcase("Enable Include Encryption in metadata and check if it is shown in metadata",
						"FAIL", "md use encryption is not present in XML file ");
			}
		} catch (Exception e) {
			newWindow.close();
			driver.findElement(By.id("encryption")).click();
			driver.findElement(By.xpath("//*[@id='set-certificates-form']/div[3]/input")).click();
			return new Testcase("Enable Include Encryption in metadata and check if it is shown in metadata", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}
	}

}
