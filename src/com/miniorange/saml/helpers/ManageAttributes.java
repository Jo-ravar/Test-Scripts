package com.miniorange.saml.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.miniorange.saml.models.UserAttributes;

public class ManageAttributes {

	public UserAttributes setAttributes(WebDriver driver) {
		String fname = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr[2]/td[1]")).getText();
		String lname = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr[3]/td[1]")).getText();
		String username = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr[2]/td[2]")).getText();
		String userLastName = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr[3]/td[2]")).getText();
		String email = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr[6]/td[1]")).getText();
		String userEmail = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr[6]/td[2]")).getText();
		String grpAttrName = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr[5]/td[1]")).getText();
		int adminSize = driver.findElements(By.xpath("/html/body/div[1]/table/tbody/tr[5]/td[2]")).size();
		System.out.println("ADMIN GRP SIZE:--" + adminSize);
		String grps = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr[5]/td[2]")).getText();
		String[] grpName = grps.split("\n");
		String adminGrp = "";
		String userGrp = "";
		System.out.println(grpName.length);
		System.out.print(grps);
		if (grpName.length > 1) {
			adminGrp = grpName[1];
			userGrp = grpName[2];
		} else {
			userGrp = grpName[0];
		}
		UserAttributes obj = new UserAttributes(fname, lname, email, username, userLastName, userEmail, grpAttrName,
				adminGrp, userGrp);
		return obj;
	}
}
