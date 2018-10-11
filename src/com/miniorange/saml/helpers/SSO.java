package com.miniorange.saml.helpers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SSO {

	public WebDriver loginAndManageAddon() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Constants.BASE_URL_1);
		driver.findElement(By.xpath("//input[contains(@id,'username')]")).sendKeys("admin");
		driver.findElement(By.xpath("//input[contains(@id,'password')]")).sendKeys("admin");
		driver.findElement(By.id("loginButton")).click();

		int count = driver.findElements(By.xpath("//*[@id='admin-menu-link']")).size();
		System.out.println("count: " + count);
		driver.findElement(By.xpath("//*[@id='admin-menu-link']")).click();
		driver.findElement(By.linkText("Add-ons")).click();

		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='authenticateButton']")).click();
		return driver;
	}

	public int doSSo(String username, String password) {
		WebDriver ssodriver = new ChromeDriver();
		ssodriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ssodriver.get("http://localhost:8090");
		ssodriver.findElement(By.linkText("Use Corporate Login")).click();
		ssodriver.findElement(By.xpath("//input[contains(@id,'username')]")).sendKeys(username);
		ssodriver.findElement(By.xpath("//input[contains(@id,'password')]")).sendKeys(password);
		ssodriver.findElement(By.xpath("//*[@id='okta-signin-submit']")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ssodriver.getTitle());
		int errorBox = ssodriver.findElements(By.xpath("//*[@id='login-container']/div/div/p")).size();
		System.out.println("ERROR IN LOGIN " + errorBox);
		ssodriver.close();
		return errorBox;
	}

	public List<Integer> doSSoWithDelay(String username, String password, int check) {
		WebDriver ssodriver = new ChromeDriver();
		ssodriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ssodriver.get("http://localhost:8090");
		int progressBox = 0;
		if (check == 1) {
			progressBox = ssodriver.findElements(By.id("moRedirectProgress")).size();
		}

		try {
			Thread.sleep(11000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ssodriver.findElement(By.xpath("//input[contains(@id,'username')]")).sendKeys(username);
		ssodriver.findElement(By.xpath("//input[contains(@id,'password')]")).sendKeys(password);
		ssodriver.findElement(By.xpath("//*[@id='okta-signin-submit']")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ssodriver.getTitle());
		List<Integer> list = new ArrayList<Integer>();
		if (check == 0) {
			int errorBox = ssodriver.findElements(By.xpath("//*[@id='login-container']/div/div/p")).size();
			progressBox = ssodriver.findElements(By.id("moRedirectProgress")).size();
			System.out.println("ERROR IN LOGIN " + errorBox);
			System.out.println("Progress Box " + progressBox);
			list.add(errorBox);
			list.add(progressBox);
		} else if (check == 1) {

			URI uri = null;
			String domain = null, domain2 = null;
			try {
				uri = new URI(ssodriver.getCurrentUrl());
				domain = uri.getHost();
				uri = new URI(Constants.RELAY_STATE_URL);
				domain2 = uri.getHost();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("DOMAIN:-- " + domain + " Domain2: " + domain2);
			if (domain.equals(domain2))
				list.add(0);
			else
				list.add(1);
			list.add(progressBox);
		}
		ssodriver.close();
		return list;
	}

	public String getValueAfterSSO(String username, String password, String url, String parameter) {
		WebDriver ssodriver = new ChromeDriver();
		ssodriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		if (url.equals(Constants.BASE_URL_1)) {
			ssodriver.get(url);
			ssodriver.findElement(By.linkText("Use Corporate Login")).click();
		} else {
			ssodriver.get(url);
		}
		ssodriver.findElement(By.xpath("//input[contains(@id,'username')]")).sendKeys(username);
		ssodriver.findElement(By.xpath("//input[contains(@id,'password')]")).sendKeys(password);
		ssodriver.findElement(By.xpath("//*[@id='okta-signin-submit']")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ssodriver.getCurrentUrl());
		if (parameter.equals("URL")) {
			String URL = ssodriver.getCurrentUrl();
			ssodriver.close();
			return URL;
		} else if (parameter.equals("HOSTNAME")) {
			URI uri = null;
			try {
				uri = new URI(ssodriver.getCurrentUrl());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String domain = uri.getHost();
			System.out.println("DOMAIN:-- " + domain);
			ssodriver.close();
			return domain.startsWith("www.") ? domain.substring(4) : domain;
		}

		ssodriver.close();
		return null;
	}

	public boolean doSSoAndCheckAdminAccess(String username, String password, int cond) {
		WebDriver ssodriver = new ChromeDriver();
		ssodriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ssodriver.get("http://localhost:8090");
		try {
			Thread.sleep(11000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ssodriver.findElement(By.xpath("//input[contains(@id,'username')]")).sendKeys(username);
		ssodriver.findElement(By.xpath("//input[contains(@id,'password')]")).sendKeys(password);
		ssodriver.findElement(By.xpath("//*[@id='okta-signin-submit']")).click();
		ssodriver.findElement(By.xpath("//*[@id='grow-intro-video-skip-button']")).click();
		ssodriver.findElement(By.xpath("//button[contains(@data-action,'skip')]")).click();

		if (cond == 2) {
			ssodriver.findElement(By.linkText("Drop access")).click();
		}

		ssodriver.findElement(By.xpath("//*[@id='admin-menu-link']")).click();
		ssodriver.findElement(By.linkText("User management")).click();
		boolean ans;
		if (cond == 0) {
			if (ssodriver.findElements(By.linkText("Show all users")).size() > 0)
				ans = true;
			else
				ans = false;
		} else if (cond == 1) {
			if (ssodriver.findElement(By.xpath("//*[@id='login-container']/h2")).getText()
					.equals("Administrator Access"))
				ans = true;
			else
				ans = false;
		} else {
			if (ssodriver.findElement(By.xpath("//*[@id='login-container']/h2")).getText()
					.equals("Administrator Access") && ssodriver.findElements(By.id("moRedirectBox")).size() > 0)
				ans = true;
			else
				ans = false;

		}

		ssodriver.close();
		return ans;
	}

	public String doSSOAndGetLogoutUrl(String username, String password, int cond) {
		WebDriver ssodriver = new ChromeDriver();
		ssodriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ssodriver.get("http://localhost:8090");
		ssodriver.findElement(By.linkText("Use Corporate Login")).click();
		ssodriver.findElement(By.xpath("//input[contains(@id,'username')]")).sendKeys(username);
		ssodriver.findElement(By.xpath("//input[contains(@id,'password')]")).sendKeys(password);
		ssodriver.findElement(By.xpath("//*[@id='okta-signin-submit']")).click();
		ssodriver.findElement(By.xpath("//*[@id='grow-intro-video-skip-button']")).click();
		ssodriver.findElement(By.xpath("//button[contains(@data-action,'skip')]")).click();
		ssodriver.findElement(By.id("user-menu-link")).click();
		ssodriver.findElement(By.id("logout-link")).click();
		if (cond == 0) {
			String URL = ssodriver.getCurrentUrl();
			ssodriver.close();
			return URL;
		} else {
			String title = ssodriver.getTitle();
			System.out.println("title: " + title);
			ssodriver.close();
			return title;
		}

	}

	public String getHostName() {
		WebDriver ssodriver = new ChromeDriver();
		ssodriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ssodriver.get("http://localhost:8090");
		URI uri = null;
		try {
			uri = new URI(ssodriver.getCurrentUrl());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String domain = uri.getHost();
		System.out.println("DOMAIN:-- " + domain);
		ssodriver.close();
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}

	public String serializePublicCertificate(String certificate) {
		String BEGIN_CERTIFICATE = "BEGIN CERTIFICATE";
		String END_CERTIFICATE = "END CERTIFICATE";
		if (StringUtils.isNotBlank(certificate)) {
			certificate = StringUtils.remove(certificate, "\r");
			certificate = StringUtils.remove(certificate, "\n");
			certificate = StringUtils.remove(certificate, "-");
			certificate = StringUtils.remove(certificate, BEGIN_CERTIFICATE);
			certificate = StringUtils.remove(certificate, END_CERTIFICATE);
			certificate = StringUtils.remove(certificate, " ");
			org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64(64);
			certificate = encoder.encodeToString(org.apache.commons.codec.binary.Base64.decodeBase64(certificate));
			StringBuffer cert = new StringBuffer("-----" + BEGIN_CERTIFICATE + "-----\r\n");
			cert.append(certificate);
			cert.append("-----" + END_CERTIFICATE + "-----");
			return cert.toString();
		}
		return certificate;
	}

	public String splitAndJoin(String certificate) {
		String splitArray[] = certificate.split("/n");
		String result = "";
		for (int i = 0; i < splitArray.length; i++) {
			result = result + splitArray[i];
		}

		return result;
	}
}
