package com.miniorange.saml.tests;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.miniorange.saml.helpers.Constants;
import com.miniorange.saml.helpers.SSO;
import com.miniorange.saml.models.Testcase;

public class IDPConfigureTests {
	Set<String> ids;
	Iterator<String> it;
	String parentId, childId;
	SSO ssoObj = new SSO();

	List<Testcase> testcases = new ArrayList<>();

	public List<Testcase> runTest(WebDriver driver) {
		testcases.add(checkIDPDefaultConfiguration(driver));
		testcases.add(checkInputValues(driver));
		testcases.add(checkIncorrectIdAndCorrectCertificateMapping(driver));
		testcases.add(checkCorrectIdAndInCorrectCertificateMapping(driver));
		// testcases.add(checkCertificateFormatting(driver));
		testcases.add(checkSSOBinding(driver));
		testcases.add(checkViewSamlRequest(driver));
		testcases.add(checkViewSamlResponse(driver));
		testcases.add(uploadMetaDataXML(driver));
		testcases.add(addMetadataURL(driver));
		testcases.add(uploadMultipleMetaDataXML(driver));
		testcases.add(addMultipleMetaDataXML(driver));
		testcases.add(refreshPeriodically(driver));
		testcases.add(checkMultipleCertificate(driver));

		return testcases;
	}

	public Testcase checkIDPDefaultConfiguration(WebDriver driver) {
		try {
			driver.get(Constants.BASE_URL_1 + "/plugins/servlet/saml/moconfreset");
			driver.findElement(By.linkText("Configure IDP")).click();
			if (driver.findElement(By.id("signedRequest")).isSelected()) {
				if (!driver.findElement(By.xpath("//*[@id='configure-sp-form']/div[10]/button[1]")).isEnabled()
						&& !driver.findElement(By.xpath("//*[@id='configure-sp-form']/div[10]/button[2]"))
								.isEnabled()) {
					if (driver.findElements(By.cssSelector("#configure-sp-form > div:nth-child(8) > label > span"))
							.size() > 0
							&& driver
									.findElements(
											By.cssSelector("#configure-sp-form > div:nth-child(9) > label > span"))
									.size() > 0
							&& driver
									.findElements(
											By.cssSelector("#configure-sp-form > div:nth-child(15) > label > span"))
									.size() > 0
							&& driver
									.findElements(
											By.cssSelector("#configure-sp-form > div:nth-child(23) > label > span"))
									.size() > 0) {
						return new Testcase("Check default settings of Configure IDP", "PASS", "----");

					} else {
						return new Testcase("Check default settings of Configure IDP", "FAIL",
								"All required fields are not showing mandatory sign");
					}

				} else {
					return new Testcase("Check default settings of Configure IDP", "FAIL",
							"View SAML Request & Response button is enable");
				}

			} else {
				return new Testcase("Check default settings of Configure IDP", "FAIL",
						"Signed request option is not selected by default");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Check default settings of Configure IDP", "FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkInputValues(WebDriver driver) {
		try {
			driver.get(Constants.BASE_URL_1 + "/plugins/servlet/saml/moconfreset");
			driver.findElement(By.linkText("Configure IDP")).click();
			driver.findElements(By.id("idpNameImport")).get(1).clear();
			driver.findElements(By.id("idpNameImport")).get(1).sendKeys(Constants.IDP_NAME);
			driver.findElement(By.xpath("//*[@id='idpEntityId']")).clear();
			driver.findElement(By.xpath("//*[@id='idpEntityId']")).sendKeys(Constants.IDP_ENTITIY_ID);
			driver.findElement(By.id("ssoUrl")).clear();
			driver.findElement(By.id("ssoUrl")).sendKeys(Constants.INVALID_SINGLE_SIGN_ON_URL);
			driver.findElement(By.id("sloUrl")).clear();
			driver.findElement(By.id("sloUrl")).sendKeys(Constants.INVALID_SINGLE_LOGOUT_URL);
			driver.findElement(By.xpath("//*[@id='x509Certificate']")).clear();
			driver.findElement(By.xpath("//*[@id='x509Certificate']")).sendKeys(Constants.INVALID_IDP_CERTIFICATE);
			driver.findElement(By.xpath("//input[@value='Save']")).click();

			int errorBoxSize = driver.findElements(By.xpath("//*[@id='tabs-example1']/div[1]/p")).size();
			System.out.println("Invalid box size:-- " + errorBoxSize);
			if (errorBoxSize == 3) {
				return new Testcase("Check error box is shown for invalid SSO login,logout URL and cerifticate", "PASS",
						"----");
			} else {
				return new Testcase("Check error box is shown for invalid SSO login,logout URL and cerifticate", "FAIL",
						"Error box is not displayed for invalid entries. server check failed");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Check error box is shown for invalid SSO login,logout URL and cerifticate", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkIncorrectIdAndCorrectCertificateMapping(WebDriver driver) {
		try {
			driver.findElements(By.id("idpNameImport")).get(1).clear();
			driver.findElements(By.id("idpNameImport")).get(1).sendKeys(Constants.IDP_NAME);
			driver.findElement(By.xpath("//*[@id='idpEntityId']")).clear();
			driver.findElement(By.xpath("//*[@id='idpEntityId']")).sendKeys(Constants.INVALID_ENTITY_ID);
			driver.findElement(By.id("ssoUrl")).clear();
			driver.findElement(By.id("ssoUrl")).sendKeys(Constants.SINGLE_SIGN_ON_URL);
			driver.findElement(By.xpath("//*[@id='x509Certificate']")).clear();
			driver.findElement(By.xpath("//*[@id='x509Certificate']")).sendKeys(Constants.IDP_CERTIFICATE);
			driver.findElement(By.xpath("//input[@value='Save']")).click();
			driver.findElement(By.id("test-saml-configuration")).click();

			ids = driver.getWindowHandles();
			it = ids.iterator();

			parentId = it.next();
			childId = it.next();
			driver.switchTo().window(childId);
			System.out.println(driver.getTitle());

			driver.findElement(By.xpath("//input[contains(@id,'username')]")).sendKeys(Constants.IDP_USERNAME);
			driver.findElement(By.xpath("//input[contains(@id,'password')]")).sendKeys(Constants.IDP_PASSWORD);
			driver.findElement(By.xpath("//*[@id='okta-signin-submit']")).click();
			String errorCode = driver.findElement(By.xpath("/html/body/div/table/tbody/tr[1]/td[2]")).getText();
			System.out.println("ErrorCode:-- " + errorCode);

			if (errorCode.equals("INVALID_ISSUER")) {
				driver.close();
				driver.switchTo().window(parentId);
				return new Testcase(
						"Check if error is shown in test configuration with wrong entity id and right certificate.",
						"PASS", "----");
			} else {
				driver.close();
				driver.switchTo().window(parentId);
				return new Testcase(
						"Check if error is shown in test configuration with wrong entity id and right certificate.",
						"FAIL", "Error message failed");
			}
		} catch (Exception e) {
			driver.close();
			driver.switchTo().window(parentId);
			System.out.println(e);
			return new Testcase(
					"Check if error is shown in test configuration with wrong entity id and right certificate.", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkCorrectIdAndInCorrectCertificateMapping(WebDriver driver) {
		try {
			driver.findElements(By.id("idpNameImport")).get(1).clear();
			driver.findElements(By.id("idpNameImport")).get(1).sendKeys(Constants.IDP_NAME);
			driver.findElement(By.xpath("//*[@id='idpEntityId']")).clear();
			driver.findElement(By.xpath("//*[@id='idpEntityId']")).sendKeys(Constants.IDP_ENTITIY_ID);
			driver.findElement(By.id("ssoUrl")).clear();
			driver.findElement(By.id("ssoUrl")).sendKeys(Constants.SINGLE_SIGN_ON_URL);
			driver.findElement(By.xpath("//*[@id='x509Certificate']")).clear();
			driver.findElement(By.xpath("//*[@id='x509Certificate']")).sendKeys(Constants.SP_PUBLIC_CERTIFICATE);
			driver.findElement(By.xpath("//input[@value='Save']")).click();
			driver.findElement(By.id("test-saml-configuration")).click();

			ids = driver.getWindowHandles();
			it = ids.iterator();

			parentId = it.next();
			childId = it.next();
			driver.switchTo().window(childId);
			System.out.println(driver.getTitle());
			String errorCode = driver.findElement(By.xpath("/html/body/div/table/tbody/tr[1]/td[2]")).getText();
			System.out.println("ErrorCode:-- " + errorCode);

			if (errorCode.equals("INVALID_SIGNATURE")) {
				driver.close();
				driver.switchTo().window(parentId);
				return new Testcase(
						"Check if error is shown in test configuration with right entity id and wrong certificate.",
						"PASS", "----");
			} else {
				driver.close();
				driver.switchTo().window(parentId);
				return new Testcase(
						"Check if error is shown in test configuration with right entity id and wrong certificate.",
						"FAIL", "Error message failed");
			}
		} catch (Exception e) {
			driver.close();
			driver.switchTo().window(parentId);
			System.out.println(e);
			return new Testcase(
					"Check if error is shown in test configuration with right entity id and wrong certificate.", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkCertificateFormatting(WebDriver driver) {
		try {
			driver.findElement(By.xpath("//*[@id='x509Certificate']")).clear();
			driver.findElement(By.xpath("//*[@id='x509Certificate']"))
					.sendKeys(Constants.SP_PUBLIC_CERTIFICATE_2_STRING);
			driver.findElement(By.xpath("//input[@value='Save']")).click();

			String getCertficateString = driver.findElement(By.xpath("//*[@id='x509Certificate']")).getText();
			String publicCertificate = ssoObj.serializePublicCertificate(Constants.SP_PUBLIC_CERTIFICATE_2);

			System.out.println(StringUtils.equals(publicCertificate, getCertficateString));

			if (StringUtils.equals(publicCertificate, getCertficateString)) {
				return new Testcase("Check if certifiacte is properly formatted or not on save.", "PASS", "----");
			} else {
				return new Testcase("Check if certificate is properly formatted or not on save.", "FAIL",
						"Certificate should have proper start and end tag and should not contain space in start. ");
			}
		} catch (Exception e) {
			return new Testcase("Check if certificate is properly formatted or not on save.", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}

	}

	public Testcase checkSSOBinding(WebDriver driver) {
		String failureReason = "";
		try {
			driver.findElements(By.id("idpNameImport")).get(1).clear();
			driver.findElements(By.id("idpNameImport")).get(1).sendKeys(Constants.IDP_NAME);
			driver.findElement(By.xpath("//*[@id='idpEntityId']")).clear();
			driver.findElement(By.xpath("//*[@id='idpEntityId']")).sendKeys(Constants.IDP_ENTITIY_ID);
			if (!driver.findElement(By.id("signedRequest")).isSelected())
				driver.findElement(By.id("signedRequest")).click();

			driver.findElement(By.id("httpRedirect")).click();
			driver.findElement(By.id("ssoUrl")).clear();
			driver.findElement(By.id("ssoUrl")).sendKeys(Constants.SINGLE_SIGN_ON_URL);
			Select s = new Select(driver.findElement(By.xpath("//*[@id='nameIdFormat']")));
			s.selectByValue("urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress");

			driver.findElement(By.xpath("//*[@id='x509Certificate']")).clear();
			driver.findElement(By.xpath("//*[@id='x509Certificate']")).sendKeys(Constants.IDP_CERTIFICATE);
			driver.findElement(By.xpath("//input[@value='Save']")).click();
			driver.findElement(By.id("test-saml-configuration")).click();

			if (checkTestConfig(driver)) {
				driver.findElement(By.id("httpPost")).click();
				driver.findElement(By.xpath("//input[@value='Save']")).click();
				driver.findElement(By.id("test-saml-configuration")).click();
				if (checkTestConfig(driver)) {
					driver.findElement(By.id("signedRequest")).click();
					driver.findElement(By.id("httpRedirect")).click();
					driver.findElement(By.xpath("//input[@value='Save']")).click();
					driver.findElement(By.id("test-saml-configuration")).click();
					if (checkTestConfig(driver)) {
						driver.findElement(By.id("httpPost")).click();
						driver.findElement(By.xpath("//input[@value='Save']")).click();
						driver.findElement(By.id("test-saml-configuration")).click();
						if (checkTestConfig(driver)) {
							return new Testcase(
									"Check if Test Configuration is working for different for diffferent binding type.",
									"PASS", "----");
						} else {
							failureReason = failureReason + " SSO binding failed for unsigned post.";
						}
					} else {
						failureReason = failureReason + " SSO binding failed for unsigned get.";
					}
				} else {
					failureReason = failureReason + " SSO binding failed for signed post.";
				}
			} else {
				failureReason = failureReason + " SSO binding failed for signed get.";
			}
			return new Testcase("Check if Test Configuration is working for different for diffferent binding type.",
					"FAIL", failureReason);
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Check if Test Configuration is working for different for diffferent binding type.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkViewSamlRequest(WebDriver driver) {
		String failureReason = "";
		try {
			if (!driver.findElement(By.id("signedRequest")).isSelected())
				driver.findElement(By.id("signedRequest")).click();

			driver.findElement(By.id("httpRedirect")).click();
			driver.findElement(By.xpath("//input[@value='Save']")).click();
			driver.findElement(By.xpath("//*[@id='display-saml-request']")).click();
			String reason = checkXMLAndCopy(driver, "GET");
			if (reason.equals("NO ERROR")) {
				driver.findElement(By.id("httpPost")).click();
				driver.findElement(By.xpath("//input[@value='Save']")).click();
				driver.findElement(By.xpath("//*[@id='display-saml-request']")).click();
				reason = checkXMLAndCopy(driver, "POST");
				if (reason.equals("NO ERROR")) {
					driver.findElement(By.id("signedRequest")).click();
					driver.findElement(By.id("httpRedirect")).click();
					driver.findElement(By.xpath("//input[@value='Save']")).click();
					driver.findElement(By.xpath("//*[@id='display-saml-request']")).click();
					reason = checkXMLAndCopy(driver, "GET");
					if (reason.equals("NO ERROR")) {
						driver.findElement(By.id("httpPost")).click();
						driver.findElement(By.xpath("//input[@value='Save']")).click();
						driver.findElement(By.xpath("//*[@id='display-saml-request']")).click();
						reason = checkXMLAndCopy(driver, "GET");
						if (reason.equals("NO ERROR")) {
							driver.findElement(By.id("signedRequest")).click();
							driver.findElement(By.id("httpRedirect")).click();
							driver.findElement(By.xpath("//input[@value='Save']")).click();
							return new Testcase("Check View SAML request for different signed and binding type.",
									"PASS", "----");
						} else {
							failureReason = failureReason + "Unsigned POST failed due to " + reason;
						}
					} else {
						failureReason = failureReason + "Unsigned GET failed due to " + reason;
					}
				} else {
					failureReason = failureReason + "Signed POST failed due to " + reason;
				}
			} else {
				failureReason = failureReason + "Signed GET failed due to " + reason;
			}
			return new Testcase("Check View SAML request for different signed and binding type.", "FAIL",
					failureReason);
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Check View SAML request for different signed and binding type.", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkViewSamlResponse(WebDriver driver) {
		try {
			driver.findElement(By.id("display-saml-response")).click();
			String reason = checkXMLAndCopy(driver, "RESPONSE");
			if (reason.equals("NO ERROR")) {
				return new Testcase("Check View SAML response for the IDP", "PASS", "----");
			} else {
				return new Testcase("Check View SAML response for the IDP", "FAIL", reason);
			}
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Check View SAML response for the IDP", "FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase uploadMetaDataXML(WebDriver driver) {
		try {
			driver.get(Constants.BASE_URL_1 + "/plugins/servlet/saml/moconfreset");
			driver.findElement(By.linkText("Configure IDP")).click();
			driver.findElement(By.linkText("Import From Metadata")).click();
			driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
			Select s = new Select(driver.findElement(By.id("metadataOption")));
			s.selectByValue("fromFile");
			driver.findElement(By.id("xmlFile")).sendKeys("C:\\Users\\ravij\\Desktop\\sampleResponse\\gitlab.json");
			driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
			String error = driver.findElement(By.xpath("//*[@id='tabs-example1']/div[1]/p")).getText();
			System.out.println(error);
			if (error.contains("Check the XML file format")) {
				driver.findElement(By.linkText("Import From Metadata")).click();
				driver.findElement(By.id("idpNameImport")).clear();
				driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
				Select s1 = new Select(driver.findElement(By.id("metadataOption")));
				s1.selectByValue("fromFile");
				driver.findElement(By.id("xmlFile")).sendKeys("C:\\Users\\ravij\\Desktop\\metaData.xml");
				driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
				if (checkIfIDPIsUpdated(driver, false)) {
					return new Testcase("Upload metadata XML having 1 certificate, try uploading wrong file extension",
							"PASS", "----");
				} else {
					return new Testcase("Upload metadata XML having 1 certificate, try uploading wrong file extension",
							"FAIL", "Metadata not changed. Check if XML file is correct or not.");
				}

			} else {
				return new Testcase("Upload metadata XML having 1 certificate, try uploading wrong file extension",
						"FAIL", "Not showing error for different extension file.");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Upload metadata XML having 1 certificate, try uploading wrong file extension", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase addMetadataURL(WebDriver driver) {
		try {
			driver.get(Constants.BASE_URL_1 + "/plugins/servlet/saml/moconfreset");
			driver.findElement(By.linkText("Configure IDP")).click();
			driver.findElement(By.linkText("Import From Metadata")).click();
			driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
			Select s = new Select(driver.findElement(By.id("metadataOption")));
			s.selectByValue("fromUrl");
			driver.findElement(By.id("inputUrl")).sendKeys(Constants.INVALID_IMPORT_DATA_URL);
			driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
			String error = driver.findElement(By.xpath("//*[@id='tabs-example1']/div[1]/p")).getText();
			System.out.println(error);
			if (error.contains("Check the XML file format")) {
				driver.findElement(By.linkText("Import From Metadata")).click();
				driver.findElement(By.id("idpNameImport")).clear();
				driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
				Select s1 = new Select(driver.findElement(By.id("metadataOption")));
				s1.selectByValue("fromUrl");
				driver.findElement(By.id("inputUrl")).clear();
				driver.findElement(By.id("inputUrl")).sendKeys(Constants.IMPORT_DATA_URL);
				driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
				if (checkIfIDPIsUpdated(driver, false)) {
					return new Testcase("Add metadata URL having 1 certificate, try adding an incorrect URL.", "PASS",
							"----");
				} else {
					return new Testcase("Add metadata URL having 1 certificate, try adding an incorrect URL.", "FAIL",
							"Check URL data not imported");
				}

			} else {
				return new Testcase("Add metadata URL having 1 certificate, try adding an incorrect URL.", "FAIL",
						"Not showing error for incorrect URL.");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Add metadata URL having 1 certificate, try adding an incorrect URL.", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase uploadMultipleMetaDataXML(WebDriver driver) {
		try {
			driver.get(Constants.BASE_URL_1 + "/plugins/servlet/saml/moconfreset");
			driver.findElement(By.linkText("Configure IDP")).click();
			driver.findElement(By.linkText("Import From Metadata")).click();
			driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
			Select s = new Select(driver.findElement(By.id("metadataOption")));
			s.selectByValue("fromFile");
			driver.findElement(By.id("xmlFile")).sendKeys("C:\\Users\\ravij\\Desktop\\sampleResponse\\gitlab.json");
			driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
			String error = driver.findElement(By.xpath("//*[@id='tabs-example1']/div[1]/p")).getText();
			System.out.println(error);
			if (error.contains("Check the XML file format")) {
				driver.findElement(By.linkText("Import From Metadata")).click();
				driver.findElement(By.id("idpNameImport")).clear();
				driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
				Select s1 = new Select(driver.findElement(By.id("metadataOption")));
				s1.selectByValue("fromFile");
				driver.findElement(By.id("xmlFile")).sendKeys("C:\\Users\\ravij\\Desktop\\multipleMetaData.xml");
				driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
				if (checkIfIDPIsUpdated(driver, true)) {
					return new Testcase("Upload metadata XML having 1 certificate, try uploading wrong file extension",
							"PASS", "----");
				} else {
					return new Testcase("Upload metadata XML having 1 certificate, try uploading wrong file extension",
							"FAIL", "Metadata not changed. Check if XML file is correct or not.");
				}

			} else {
				return new Testcase(
						"Upload metadata XML having multiple certificate, try uploading wrong file extension", "FAIL",
						"Not showing error for different extension file.");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Upload metadata XML having multiple certificate, try uploading wrong file extension",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}

	}

	public Testcase addMultipleMetaDataXML(WebDriver driver) {
		try {
			driver.get(Constants.BASE_URL_1 + "/plugins/servlet/saml/moconfreset");
			driver.findElement(By.linkText("Configure IDP")).click();
			driver.findElement(By.linkText("Import From Metadata")).click();
			driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
			Select s = new Select(driver.findElement(By.id("metadataOption")));
			s.selectByValue("fromUrl");
			driver.findElement(By.id("inputUrl")).sendKeys(Constants.INVALID_IMPORT_DATA_URL_2);
			driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
			String error = driver.findElement(By.xpath("//*[@id='tabs-example1']/div[1]/p")).getText();
			System.out.println(error);
			if (error.contains("Check the XML file format")) {
				driver.findElement(By.linkText("Import From Metadata")).click();
				driver.findElement(By.id("idpNameImport")).clear();
				driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
				Select s1 = new Select(driver.findElement(By.id("metadataOption")));
				s1.selectByValue("fromUrl");
				driver.findElement(By.id("inputUrl")).clear();
				driver.findElement(By.id("inputUrl")).sendKeys(Constants.IMPORT_DATA_URL_2);
				driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
				if (checkIfIDPIsUpdated(driver, true)) {
					return new Testcase("Add metadata URL having multiple certificate, try adding an incorrect URL.",
							"PASS", "----");
				} else {
					return new Testcase("Add metadata URL having multiple certificate, try adding an incorrect URL.",
							"FAIL", "Check URL data not imported");
				}

			} else {
				return new Testcase("Add metadata URL having multiple certificate, try adding an incorrect URL.",
						"FAIL", "Not showing error for incorrect URL.");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Add metadata URL having multiple certificate, try adding an incorrect URL.", "FAIL",
					"SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase refreshPeriodically(WebDriver driver) {
		try {
			driver.get(Constants.BASE_URL_1 + "/plugins/servlet/saml/moconfreset");
			driver.findElement(By.linkText("Configure IDP")).click();
			driver.findElement(By.linkText("Import From Metadata")).click();
			driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
			Select s = new Select(driver.findElement(By.id("metadataOption")));
			s.selectByValue("fromUrl");
			driver.findElement(By.id("inputUrl")).sendKeys(Constants.IMPORT_DATA_URL);
			if (!driver.findElement(By.id("refreshMetadata")).isSelected())
				driver.findElement(By.id("refreshMetadata")).click();
			s = new Select(driver.findElement(By.id("refreshInterval")));
			s.selectByValue("custom");
			driver.findElement(By.id("customRefreshInterval")).clear();
			driver.findElement(By.id("customRefreshInterval")).sendKeys(String.valueOf(1));
			s = new Select(driver.findElement(By.xpath("//*[@id='customRefreshValue']/select")));
			s.selectByValue("minutes");
			driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
			driver.findElement(By.id("idpEntityId")).clear();
			driver.findElement(By.id("idpEntityId")).sendKeys("abc");
			driver.findElement(By.xpath("//input[@value='Save']")).click();
			try {
				Date date = new Date();
				long time = date.getTime();
				Timestamp ts = new Timestamp(time);
				System.out.println("Sleep " + ts);
				Thread.sleep(60000);
				time = date.getTime();
				ts = new Timestamp(time);
				System.out.println("WAKE UP " + ts);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			driver.findElement(By.linkText("SP Info")).click();
			driver.navigate().refresh();
			driver.findElement(By.linkText("Configure IDP")).click();
			String reloadedText = driver.findElement(By.id("idpEntityId")).getText();
			if (reloadedText.equals("abc")) {
				return new Testcase("Upload metadata URL and select refresh periodically to update data after 1 min.",
						"FAIL", "Data not updated");
			} else {
				return new Testcase("Upload metadata URL and select refresh periodically to update data after 1 min.",
						"PASS", "----");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase("Upload metadata URL and select refresh periodically to update data after 1 min.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase checkMultipleCertificate(WebDriver driver) {
		try {
			driver.get(Constants.BASE_URL_1 + "/plugins/servlet/saml/moconfreset");
			driver.findElement(By.linkText("Configure IDP")).click();
			driver.findElement(By.linkText("Import From Metadata")).click();
			driver.findElement(By.id("idpNameImport")).sendKeys(Constants.IDP_NAME);
			Select s = new Select(driver.findElement(By.id("metadataOption")));
			s.selectByValue("fromUrl");
			driver.findElement(By.id("inputUrl")).sendKeys(Constants.IMPORT_DATA_URL_2);
			driver.findElement(By.xpath("//*[@id='importButtons']/table/tbody/tr/td[2]/input[1]")).click();
			// driver.findElement(By.xpath("//input[@value='Save']")).click();
			driver.findElement(By.id("test-saml-configuration")).click();
			ids = driver.getWindowHandles();
			it = ids.iterator();

			parentId = it.next();
			childId = it.next();
			driver.switchTo().window(childId);
			driver.findElement(By.xpath("//*[@id='i0116']")).sendKeys(Constants.SECOND_IDP_USERNAME);
			driver.findElement(By.xpath("//input[@value='Next']")).click();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(("PASSWORD:-- " + driver.findElements(By.xpath("//*[@id='i0118']")).size()));
			driver.findElement(By.xpath("//*[@id='i0118']")).sendKeys(Constants.SECOND_IDP_PASSWORD);
			driver.findElement(By.xpath("//input[@value='Sign in']")).click();
			driver.findElement(By.xpath("//input[@value='Yes']")).click();
			driver.close();
			driver.switchTo().window(parentId);
			String validCert = "";
			String cert1 = driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(0)
					.getText();
			String cert2 = driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(1)
					.getText();

			driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(1).clear();
			driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(1)
					.sendKeys(Constants.SP_PUBLIC_CERTIFICATE);
			driver.findElement(By.xpath("//input[@value='Save']")).click();
			driver.findElement(By.id("test-saml-configuration")).click();
			if (checkTestConfig(driver)) {
				validCert = cert1;
				driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(0).clear();
				driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(0)
						.sendKeys(Constants.SP_PUBLIC_CERTIFICATE);
				driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(1).clear();
				driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(1)
						.sendKeys(validCert);
			} else {
				validCert = cert2;
				driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(0).clear();
				driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(0)
						.sendKeys(validCert);
			}
			driver.findElement(By.xpath("//input[@value='Save']")).click();
			driver.findElement(By.id("test-saml-configuration")).click();
			if (checkTestConfig(driver)) {
				driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(0).clear();
				driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(0)
						.sendKeys(Constants.SP_PUBLIC_CERTIFICATE);
				driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(1).clear();
				driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(1)
						.sendKeys(Constants.SP_PUBLIC_CERTIFICATE);
				driver.findElement(By.xpath("//input[@value='Save']")).click();
				driver.findElement(By.id("test-saml-configuration")).click();
				if (checkTestConfig(driver)) {
					return new Testcase(
							"For multiple certificates,Test configuration should be successful even if the valid certificate is present in one oftext field.",
							"FAIL", "Test configuration passed in case of invalid certificates");
				} else {
					driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(0).clear();
					driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(1).clear();
					driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div/textarea")).get(1)
							.sendKeys(validCert);
					driver.findElement(By.xpath("//input[@value='Save']")).click();
					driver.findElement(By.id("test-saml-configuration")).click();
					if (checkTestConfig(driver)) {
						return new Testcase(
								"For multiple certificates,Test configuration should be successful even if the valid certificate is present in one oftext field.",
								"PASS", "----");
					} else {
						return new Testcase(
								"For multiple certificates,Test configuration should be successful even if the valid certificate is present in one oftext field.",
								"FAIL", "Test configuration failed when only one certificate is present");
					}
				}

			} else {
				return new Testcase(
						"For multiple certificates,Test configuration should be successful even if the valid certificate is present in one oftext field.",
						"FAIL", "Test configuration failed even when valid certificate is present in one field");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new Testcase(
					"For multiple certificates,Test configuration should be successful even if the valid certificate is present in one oftext field.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public boolean checkTestConfig(WebDriver driver) {
		ids = driver.getWindowHandles();
		it = ids.iterator();
		parentId = it.next();
		childId = it.next();
		driver.switchTo().window(childId);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(driver.getTitle());
		String resultText = driver.findElement(By.xpath("/html/body/div[1]/div")).getText();
		System.out.println(resultText);
		if (resultText.equals("TEST SUCCESSFUL")) {
			driver.close();
			driver.switchTo().window(parentId);
			return true;
		}
		driver.close();
		driver.switchTo().window(parentId);
		return false;
	}

	public String checkXMLAndCopy(WebDriver driver, String requestType) {
		System.out.println("IN XML");
		ids = driver.getWindowHandles();
		it = ids.iterator();
		parentId = it.next();
		childId = it.next();
		driver.switchTo().window(childId);
		driver.findElement(By.id("copy-button")).click();
		driver.switchTo().window(parentId);
		Actions a = new Actions(driver);
		driver.findElement(By.id("idpEntityId")).clear();
		driver.findElement(By.id("idpEntityId")).sendKeys(Keys.chord(Keys.CONTROL, "v"));
		System.out.println("ACTION DONE");
		driver.findElement(By.xpath("//input[@value='Save']")).click();

		String xml = driver.findElement(By.xpath("//*[@id='idpEntityId']")).getAttribute("value");
		System.out.println("THIS:-- " + xml + " " + xml.length());
		System.out.println("SSO URL:--" + driver.findElement(By.id("ssoUrl")).getAttribute("value"));

		driver.findElement(By.id("idpEntityId")).clear();
		driver.findElement(By.xpath("//*[@id='idpEntityId']")).sendKeys(Constants.IDP_ENTITIY_ID);
		driver.findElement(By.xpath("//input[@value='Save']")).click();
		if (xml.length() > 0) {
			System.out.println(xml.contains("xml version") + "  " + xml.contains("xml version"));
			if (requestType.equals("GET")) {
				if (xml.contains("xml version") && xml.contains("samlp:AuthnRequest")) {
					driver.switchTo().window(childId);
					driver.findElement(By.xpath("/html/body/div[3]/input")).click();
					try {
						driver.switchTo().window(childId);
						driver.close();
						driver.switchTo().window(parentId);
						return "Done is not closing window";

					} catch (Exception e) {
						driver.switchTo().window(parentId);
						return "NO ERROR";
					}

				} else {
					return "No SAML Request found";
				}
			} else if (requestType.equals("POST")) {

				if (xml.contains("xml version") && xml.contains("samlp:AuthnRequest")
						&& xml.contains("ds:SignatureValue")) {
					driver.switchTo().window(childId);
					driver.findElement(By.xpath("/html/body/div[3]/input")).click();
					try {
						driver.switchTo().window(childId);
						driver.close();
						driver.switchTo().window(parentId);
						return "Done is not closing window";

					} catch (Exception e) {
						driver.switchTo().window(parentId);
						return "NO ERROR";
					}

				} else {
					driver.close();
					driver.switchTo().window(parentId);
					return "No SAML Request and signatureValue found.";
				}
			} else {
				if (xml.contains("xml version") && xml.contains("saml2p:Response")
						&& xml.contains("ds:SignatureValue")) {
					driver.switchTo().window(childId);
					driver.findElement(By.xpath("/html/body/div[3]/input")).click();
					try {
						driver.switchTo().window(childId);
						driver.close();
						driver.switchTo().window(parentId);
						return "Done is not closing window";

					} catch (Exception e) {
						driver.switchTo().window(parentId);
						return "NO ERROR";
					}

				} else {
					driver.close();
					driver.switchTo().window(parentId);
					return "No SAML Response found.";
				}

			}
		} else {
			driver.close();
			driver.switchTo().window(parentId);
			return "Copy clipboard button is not working.";
		}

	}

	public boolean checkIfIDPIsUpdated(WebDriver driver, boolean multiple) {
		// driver.findElement(By.xpath("//input[@value='Save']")).click();
		String entId = driver.findElement(By.xpath("//*[@id='idpEntityId']")).getAttribute("value");
		System.out.println("DATA: " + entId + " " + entId.length());
		String ssoURL = driver.findElement(By.id("ssoUrl")).getAttribute("value");
		System.out.println("DATA: " + ssoURL + " " + ssoURL.length());
		if (multiple) {
			int noOftextArea = driver.findElements(By.xpath("//*[@id='configure-sp-form']/div[8]/div")).size();
			System.out.println(noOftextArea);
			if (noOftextArea > 2) {
				if (entId.length() > 0 && ssoURL.length() > 0) {
					return true;
				}
				return false;
			} else {
				return false;
			}

		} else {
			String certificate = driver.findElement(By.xpath("//*[@id='x509Certificate']")).getText();
			System.out.println("DATA: " + certificate + " " + certificate.length());
			if (entId.length() > 0 && ssoURL.length() > 0 && certificate.length() > 0) {
				return true;
			}
			return false;
		}

	}

}
