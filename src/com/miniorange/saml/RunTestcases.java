package com.miniorange.saml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.miniorange.saml.helpers.ManageAttributes;
import com.miniorange.saml.helpers.SSO;
import com.miniorange.saml.helpers.WriteExcelFile;
import com.miniorange.saml.models.Testcase;
import com.miniorange.saml.tests.IDPConfigureTests;
import com.miniorange.saml.tests.SSOTests;
import com.miniorange.saml.tests.SpInfoTests;
import com.miniorange.saml.tests.UserGroupTests;
import com.miniorange.saml.tests.UserProfileTests;

public class RunTestcases {

	public static Set<String> ids;
	public static Iterator<String> it;
	public static String parentId, childId;

	public static void main(String[] args) {

		List<Testcase> testcases = new ArrayList<>();
		SSO ssoObj = new SSO();
		SpInfoTests spInfoObj = new SpInfoTests();
		IDPConfigureTests idpObj = new IDPConfigureTests();
		UserProfileTests profileObj = new UserProfileTests();
		UserGroupTests groupObj = new UserGroupTests();
		SSOTests ssoTestObj = new SSOTests();
		ManageAttributes attributeHelper = new ManageAttributes();
		WebDriver driver;
		driver = ssoObj.loginAndManageAddon();
		driver.findElement(By.linkText("miniOrange Single Sign-On")).click();

		if (driver.findElements(By.xpath("//input[@value='Next']")).size() > 0) {

//			testcases.addAll(spInfoObj.runTest(driver));
//			testcases.addAll(idpObj.runTest(driver));
//
//			driver.close();
//			driver = ssoObj.loginAndManageAddon();
//			testcases.addAll(profileObj.runTest(driver));
//
//			driver.close();
//			driver = ssoObj.loginAndManageAddon();
//			testcases.addAll(groupObj.runTest(driver));
//
//			driver.close();
//			driver = ssoObj.loginAndManageAddon();
			testcases.addAll(ssoTestObj.runTest(driver));

		} else {
			System.out.println("Trial expired for the plugin");
		}

		WriteExcelFile newExcel = new WriteExcelFile();
		try {
			newExcel.writeXLSFile(testcases, "C:\\Users\\ravij\\Desktop\\", "CONFLUENCE SAML TEST REPORT");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
