package com.miniorange.saml.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.miniorange.saml.helpers.Constants;
import com.miniorange.saml.helpers.ManageAttributes;
import com.miniorange.saml.helpers.SSO;
import com.miniorange.saml.models.Testcase;
import com.miniorange.saml.models.UserAttributes;

public class UserGroupTests {

	Set<String> ids;
	Iterator<String> it;
	String parentId, childId;
	UserAttributes user;

	SSO ssoObj = new SSO();
	ManageAttributes attributeHelper = new ManageAttributes();

	List<Testcase> testcases = new ArrayList<>();

	public List<Testcase> runTest(WebDriver driver) {
		getInitialState(driver);
		// testcases.add(zeroDefaultGroup(driver));
		testcases.add(checkGroupMapping(driver, user));
		testcases.add(restrictUserCreation(driver));
		testcases.add(createUserIfGroupsMapped(driver));
		testcases.add(assignMultipleDefaultGroupsForNewUsers(driver));
		testcases.add(assignMultipleDefaultGroupsForNewUsers2(driver));
		testcases.add(assignMultipleDefaultGroupsForAllUsers(driver));
		testcases.add(assignMultipleDefaultGroupsForAllUsers2(driver));
		testcases.add(keepExisingUserGroups(driver));
		testcases.add(updateMappedGroups(driver));

		return testcases;
	}

	public Testcase zeroDefaultGroup(WebDriver driver) {
		int beforeSize, afterSize, initial;
		try {
			initial = driver.findElements(By.xpath("//*[@id='tabs-example1']/div[1]/p")).size();
			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			beforeSize = driver.findElements(By.xpath("//*[@id='tabs-example1']/div[1]/p")).size();
			System.out.println("BEFOR:--" + beforeSize);
			System.out.println("INITIAL:--" + initial);

			if (beforeSize == 1) {
				driver.findElement(By.xpath("//*[@id='tabs-example1']/div[1]/span")).click();
				driver.findElement(By.xpath("//a[@class='select2-search-choice-close']")).click();
				driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
				System.out.println(driver.findElements(By.xpath("//*[@id='tabs-example1']/div[1]/p")).size());
				afterSize = driver.findElements(By.xpath("//*[@id='tabs-example1']/div[1]/p")).size();
				System.out.println("AFTER:--" + afterSize);

				if (afterSize != initial) {
					return new Testcase(
							"If user does not select or deselects the default group then the form should not save.",
							"FAIL", "Form saved without default groups.");
				} else {
					return new Testcase(
							"If user does not select or deselects the default group then the form should not save.",
							"PASS", "----");
				}
			} else {
				return new Testcase(
						"If user does not select or deselects the default group then the form should not save.", "FAIL",
						"In default settings of group mapping no default group is selected.");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return new Testcase("If user does not select or deselects the default group then the form should not save.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}

	}

	public Testcase checkGroupMapping(WebDriver driver, UserAttributes user) {
		try {
			System.out.println("checkGroupMapping");
			System.out.println(user.grpAttrName + " " + user.adminGrp + " " + user.userGrp);
			driver.findElement(By.xpath("//*[@id='roleAttribute']")).clear();
			driver.findElement(By.xpath("//*[@id='roleAttribute']")).sendKeys(user.grpAttrName);
			driver.findElement(By.xpath("//*[@id='confluence-administrators']")).clear();
			driver.findElement(By.xpath("//*[@id='confluence-administrators']")).sendKeys(user.adminGrp);
			driver.findElement(By.xpath("//*[@id='confluence-users']")).clear();
			driver.findElement(By.xpath("//*[@id='confluence-users']")).sendKeys(user.userGrp);
			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			if ((ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0)) {
				driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
				driver.findElement(By.linkText("User Groups")).click();
				return new Testcase("Check Group Mapping", "FAIL", "SSO Failed");
			} else {
				List<String> groups = new ArrayList<String>();
				groups.add("confluence-administrators");
				groups.add("confluence-users");
				int result = checkIfAllGroupsAreAssigned(driver, user.username, groups);
				driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
				driver.findElement(By.linkText("User Groups")).click();
				if (result == 1) {
					return new Testcase("Check Group Mapping", "PASS", "----");
				} else if (result == 0) {
					return new Testcase("Check Group Mapping", "FAIL", "All the required groups are not mapped");
				} else {
					return new Testcase("Check Group Mapping", "FAIL", "User not created check attribute mapping.");
				}
			}
		} catch (Exception e) {
			driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
			driver.findElement(By.linkText("User Groups")).click();
			System.out.println(e.toString());
			return new Testcase("Check Group Mapping", "FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase restrictUserCreation(WebDriver driver) {
		try {
			System.out.println("restrictUserCreation");
			if (!driver.findElement(By.id("restrictUserCreation")).isSelected())
				driver.findElement(By.id("restrictUserCreation")).click();

			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			if (ssoObj.doSSo(Constants.IDP_USERNAME_2, Constants.IDP_PASSWORD_2) != 0) {
				if (ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0) {
					driver.findElement(By.id("restrictUserCreation")).click();
					driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
					return new Testcase("Do not create new user", "FAIL", "SSO Failed for existing user also.");
				} else {
					driver.findElement(By.id("restrictUserCreation")).click();
					driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
					return new Testcase("Do not create new user", "PASS", "----");
				}

			} else {
				System.out.println("Group Mapping do not create new user failed");
				driver.findElement(By.id("restrictUserCreation")).click();
				driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
				return new Testcase("Do not create new user", "FAIL", "Created new user since sso didnt failed");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			driver.findElement(By.id("restrictUserCreation")).click();
			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			return new Testcase("Do not create new user", "FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase createUserIfGroupsMapped(WebDriver driver) {
		try {
			System.out.println("createUserIfGroupsMapped");
			if (!driver.findElement(By.id("createUsersIfRoleMapped")).isSelected())
				driver.findElement(By.id("createUsersIfRoleMapped")).click();

			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			if (ssoObj.doSSo(Constants.IDP_USERNAME_2, Constants.IDP_PASSWORD_2) != 0) {
				System.out.println("Create User only if group are mapped PASSED");
				driver.findElement(By.id("createUsersIfRoleMapped")).click();
				driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
				return new Testcase("Create User only if groups are mapped", "PASS", "----");
			} else {
				System.out.println("Create User only if group are mapped FAILED");
				driver.findElement(By.id("createUsersIfRoleMapped")).click();
				driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
				return new Testcase("Create User only if groups are mapped", "FAIL",
						"Created new user since sso didnt failed");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			driver.findElement(By.id("createUsersIfRoleMapped")).click();
			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			return new Testcase("Create User only if groups are mapped", "FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase assignMultipleDefaultGroupsForNewUsers(WebDriver driver) {
		try {
			System.out.println("assignMultipleDefaultGroupsForNewUsers");
			driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys("confluence-demo");
			driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys(Keys.ENTER);
			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			if (ssoObj.doSSo(Constants.IDP_USERNAME_2, Constants.IDP_PASSWORD_2) != 0) {
				return new Testcase(
						"Assign user multiple default gropus and check new users and SSO whose group will not be mapped",
						"FAIL", "SSO Failed");
			} else {
				List<String> groups = new ArrayList<String>();
				groups.add("confluence-demo");
				groups.add("confluence-users");
				int result = checkIfAllGroupsAreAssigned(driver, Constants.IDP_FIRST_NAME, groups);
				driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
				driver.findElement(By.linkText("User Groups")).click();
				if (result == 1) {
					driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys("confluence-test");
					driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys(Keys.ENTER);
					driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
					if (ssoObj.doSSo(Constants.IDP_USERNAME_2, Constants.IDP_PASSWORD_2) != 0) {
						return new Testcase(
								"Assign user multiple default gropus and check new users and SSO whose group will not be mapped",
								"FAIL", "SSO Failed");
					} else {
						groups.add("confluence-test");
						result = checkIfAllGroupsAreAssigned(driver, Constants.IDP_FIRST_NAME, groups);
						driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
						driver.findElement(By.linkText("User Groups")).click();
						if (result == 0) {
							return new Testcase(
									"Assign user multiple default gropus and check new users and SSO whose group will not be mapped",
									"PASS", "----");
						} else if (result == 1) {
							return new Testcase(
									"Assign user multiple default gropus and check new users and SSO whose group will not be mapped",
									"FAIL",
									"All the default groups are mapped to existing user also whose groups are not mapped");
						} else {
							return new Testcase(
									"Assign user multiple default gropus and check new users and SSO whose group will not be mapped",
									"FAIL", "User not created check attribute mapping.");
						}

					}
				} else if (result == 0) {
					return new Testcase(
							"Assign user multiple default gropus and check new users and SSO whose group will not be mapped",
							"FAIL", "All the default groups not mapped to a user whose response was not mapped");
				} else {
					return new Testcase(
							"Assign user multiple default gropus and check new users and SSO whose group will not be mapped",
							"FAIL", "User not created check attribute mapping.");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
			driver.findElement(By.linkText("User Groups")).click();
			return new Testcase(
					"Assign user multiple default gropus and check new users and SSO whose group will not be mapped",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase assignMultipleDefaultGroupsForNewUsers2(WebDriver driver) {
		try {
			System.out.println("assignMultipleDefaultGroupsForNewUsers2");
			deleteUser(driver, user.username);

			if (ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0) {
				return new Testcase(
						"Assign user multiple default gropus and check new users and SSO whose group will be mapped",
						"FAIL", "SSO Failed");
			} else {
				List<String> groups = new ArrayList<String>();
				groups.add("confluence-demo");
				groups.add("confluence-users");
				groups.add("confluence-test");
				int result = checkIfAllGroupsAreAssigned(driver, user.username, groups);
				driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
				driver.findElement(By.linkText("User Groups")).click();
				if (result == 0) {
					if (ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0) {
						return new Testcase(
								"Assign user multiple default gropus and check new users and SSO whose group will be mapped",
								"FAIL", "SSO Failed");
					} else {
						result = checkIfAllGroupsAreAssigned(driver, user.username, groups);
						driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
						driver.findElement(By.linkText("User Groups")).click();
						if (result == 0) {
							return new Testcase(
									"Assign user multiple default gropus and check new users and SSO whose group will be mapped",
									"PASS", "----");
						} else if (result == 1) {
							return new Testcase(
									"Assign user multiple default gropus and check new users and SSO whose group will be mapped",
									"FAIL",
									"All the default groups are mapped to existing user also whose groups are  mapped");
						} else {
							return new Testcase(
									"Assign user multiple default gropus and check new users and SSO whose group will be mapped",
									"FAIL", "User not created check attribute mapping.");
						}

					}
				} else if (result == 1) {
					return new Testcase(
							"Assign user multiple default gropus and check new users and SSO whose group will  be mapped",
							"FAIL", "All the default groups got mapped to a user whose response was mapped");
				} else {
					return new Testcase(
							"Assign user multiple default gropus and check new users and SSO whose group will be mapped",
							"FAIL", "User not created check attribute mapping.");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
			driver.findElement(By.linkText("User Groups")).click();
			return new Testcase(
					"Assign user multiple default gropus and check new users and SSO whose group will be mapped",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase assignMultipleDefaultGroupsForAllUsers(WebDriver driver) {
		try {
			System.out.println("assignMultipleDefaultGroupsForAllUsers");
			deleteUser(driver, Constants.IDP_FIRST_NAME);
			driver.findElement(By.id("allUsers")).click();
			int size = driver.findElements(By.xpath("//*[@id='s2id_defaultGroups']/ul/li/a")).size();
			for (int i = size - 1; i >= 0; i--) {
				System.out.println("i: " + i);
				driver.findElements(By.xpath("//*[@id='s2id_defaultGroups']/ul/li/a")).get(i).click();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys("confluence-demo");
			driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys(Keys.ENTER);
			driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys("confluence-users");
			driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys(Keys.ENTER);
			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			if (ssoObj.doSSo(Constants.IDP_USERNAME_2, Constants.IDP_PASSWORD_2) != 0) {
				return new Testcase(
						"Assign user multiple default gropus and check all users and SSO whose group will not be mapped",
						"FAIL", "SSO Failed");
			} else {
				List<String> groups = new ArrayList<String>();
				groups.add("confluence-demo");
				groups.add("confluence-users");
				int result = checkIfAllGroupsAreAssigned(driver, Constants.IDP_FIRST_NAME, groups);
				driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
				driver.findElement(By.linkText("User Groups")).click();
				if (result == 1) {
					driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys("confluence-test");
					driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys(Keys.ENTER);
					driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
					if (ssoObj.doSSo(Constants.IDP_USERNAME_2, Constants.IDP_PASSWORD_2) != 0) {
						return new Testcase(
								"Assign user multiple default gropus and check all users and SSO whose group will not be mapped",
								"FAIL", "SSO Failed");
					} else {
						groups.add("confluence-test");
						result = checkIfAllGroupsAreAssigned(driver, Constants.IDP_FIRST_NAME, groups);
						driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
						driver.findElement(By.linkText("User Groups")).click();
						if (result == 1) {
							return new Testcase(
									"Assign user multiple default gropus and check all users and SSO whose group will not be mapped",
									"PASS", "----");
						} else if (result == 0) {
							return new Testcase(
									"Assign user multiple default gropus and check all users and SSO whose group will not be mapped",
									"FAIL",
									"All the default groups are not mapped to existing user  whose groups are not mapped");
						} else {
							return new Testcase(
									"Assign user multiple default gropus and check all users and SSO whose group will not be mapped",
									"FAIL", "User not created check attribute mapping.");
						}

					}
				} else if (result == 0) {
					return new Testcase(
							"Assign user multiple default gropus and check all users and SSO whose group will not be mapped",
							"FAIL",
							"All the default groups are not mapped to a new user whose response was not mapped");
				} else {
					return new Testcase(
							"Assign user multiple default gropus and check all users and SSO whose group will not be mapped",
							"FAIL", "User not created check attribute mapping.");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return new Testcase(
					"Assign user multiple default gropus and check all users and SSO whose group will not be mapped",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase assignMultipleDefaultGroupsForAllUsers2(WebDriver driver) {
		try {
			System.out.println("assignMultipleDefaultGroupsForAllUsers2");
			deleteUser(driver, user.username);
			int size = driver.findElements(By.xpath("//*[@id='s2id_defaultGroups']/ul/li/a")).size();
			System.out.println(size);
			for (int i = size - 1; i >= 0; i--) {
				System.out.println("i: " + i);
				driver.findElements(By.xpath("//*[@id='s2id_defaultGroups']/ul/li/a")).get(i).click();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys("confluence-demo");
			driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys(Keys.ENTER);
			driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys("confluence-users");
			driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys(Keys.ENTER);
			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			if (ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0) {
				return new Testcase(
						"Assign user multiple default gropus and check all users and SSO whose group will be mapped",
						"FAIL", "SSO Failed");
			} else {
				List<String> groups = new ArrayList<String>();
				groups.add("confluence-demo");
				groups.add("confluence-users");
				groups.add("confluence-administrators");
				int result = checkIfAllGroupsAreAssigned(driver, user.username, groups);
				driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
				driver.findElement(By.linkText("User Groups")).click();
				if (result == 0) {
					driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys("confluence-test");
					driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys(Keys.ENTER);
					driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
					if (ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0) {
						return new Testcase(
								"Assign user multiple default gropus and check all users and SSO whose group will be mapped",
								"FAIL", "SSO Failed");
					} else {
						groups.add("confluence-test");
						result = checkIfAllGroupsAreAssigned(driver, user.username, groups);
						driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
						driver.findElement(By.linkText("User Groups")).click();
						if (result == 0) {
							return new Testcase(
									"Assign user multiple default gropus and check all users and SSO whose group will be mapped",
									"PASS", "----");
						} else if (result == 1) {
							return new Testcase(
									"Assign user multiple default gropus and check all users and SSO whose group will be mapped",
									"FAIL",
									"All the default groups got mapped to existing user whose group response was  mapped");
						} else {
							return new Testcase(
									"Assign user multiple default gropus and check all users and SSO whose group will be mapped",
									"FAIL", "User not created check attribute mapping.");
						}

					}
				} else if (result == 1) {
					return new Testcase(
							"Assign user multiple default gropus and check all users and SSO whose group will  be mapped",
							"FAIL", "All the default groups got mapped to a new user whose group response was mapped");
				} else {
					return new Testcase(
							"Assign user multiple default gropus and check all users and SSO whose group will be mapped",
							"FAIL", "User not created check attribute mapping.");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return new Testcase(
					"Assign user multiple default gropus and check all users and SSO whose group will be mapped",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}
	}

	public Testcase keepExisingUserGroups(WebDriver driver) {
		try {
			System.out.println("keepExisingUserGroups");
			deleteUser(driver, Constants.IDP_FIRST_NAME);
			driver.findElement(By.id("allUsers")).click();

			System.out.println(driver.findElement(By.xpath("//*[@id='s2id_defaultGroups']/ul/li[2]")).getText());
			if (driver.findElement(By.xpath("//*[@id='s2id_defaultGroups']/ul/li[2]")).getText()
					.equals("confluence-test"))
				driver.findElement(By.xpath("//*[@id='s2id_defaultGroups']/ul/li[2]/a")).click();

			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();

			if ((ssoObj.doSSo(Constants.IDP_USERNAME_2, Constants.IDP_PASSWORD_2) != 0)) {
				return new Testcase(
						"Keep Existing User Groups : If checked groups of existing users will not be updated.", "FAIL",
						"SSO Failed");
			} else {
				List<String> groups = new ArrayList<String>();
				groups.add("confluence-demo");
				groups.add("confluence-users");
				int result = checkIfAllGroupsAreAssigned(driver, Constants.IDP_FIRST_NAME, groups);
				driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
				driver.findElement(By.linkText("User Groups")).click();
				if (result == 1) {
					driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys("confluence-test");
					driver.findElement(By.xpath("//*[@id='s2id_autogen1']")).sendKeys(Keys.ENTER);

					if (!driver.findElement(By.id("keepExistingUserRoles")).isSelected())
						driver.findElement(By.id("keepExistingUserRoles")).click();

					driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
					if (ssoObj.doSSo(Constants.IDP_USERNAME_2, Constants.IDP_PASSWORD_2) != 0) {
						return new Testcase(
								"Keep Existing User Groups : If checked groups of existing users will not be updated.",
								"FAIL", "SSO Failed");
					} else {
						groups.add("confluence-test");
						result = checkIfAllGroupsAreAssigned(driver, Constants.IDP_FIRST_NAME, groups);
						driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
						driver.findElement(By.linkText("User Groups")).click();
						if (result == 0) {
							return new Testcase(
									"Keep Existing User Groups : If checked groups of existing users will not be updated.",
									"PASS", "----");
						} else if (result == 1) {
							return new Testcase(
									"Keep Existing User Groups : If checked groups of existing users will not be updated.",
									"FAIL", "Groups of existing user got updated");
						} else {
							return new Testcase(
									"Keep Existing User Groups : If checked groups of existing users will not be updated.",
									"FAIL", "User not created check attribute mapping.");
						}

					}

				} else if (result == 0) {
					return new Testcase(
							"Keep Existing User Groups : If checked groups of existing users will not be updated.",
							"FAIL", "All default groups were not present for new user");
				} else {
					return new Testcase(
							"Keep Existing User Groups : If checked groups of existing users will not be updated.",
							"FAIL", "User not created check attribute mapping.");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return new Testcase("Keep Existing User Groups : If checked groups of existing users will not be updated.",
					"FAIL", "SCRIPT FAILED:- " + e.toString());
		}

	}

	public Testcase updateMappedGroups(WebDriver driver) {
		try {
			System.out.println("updateMappedGroups");
			deleteUser(driver, user.username);
			driver.findElement(By.id("keepExistingUserRoles")).click();
			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			driver.findElement(By.xpath("//*[@id='confluence-administrators']")).clear();
			driver.findElement(By.xpath("//*[@id='confluence-test']")).sendKeys(user.adminGrp);
			driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
			if (ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0) {
				return new Testcase(
						"Check update user group otion by mapping user groups and check if user is removed from empty group.",
						"FAIL", "SSO Failed");
			} else {
				List<String> groups = new ArrayList<String>();
				groups.add("confluence-test");
				groups.add("confluence-users");
				int result = checkIfAllGroupsAreAssigned(driver, user.username, groups);
				driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
				driver.findElement(By.linkText("User Groups")).click();
				if (result == 1) {
					driver.findElement(By.xpath("//*[@id='confluence-test']")).clear();
					driver.findElement(By.xpath("//*[@id='confluence-demo']")).sendKeys(user.adminGrp);
					driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
					if (ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0) {
						return new Testcase(
								"Check update user group otion by mapping user groups and check if user is removed from empty group.",
								"FAIL", "SSO Failed");
					} else {
						groups.remove(0);
						groups.add("confluence-demo");
						result = checkIfAllGroupsAreAssigned(driver, user.username, groups);
						driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
						driver.findElement(By.linkText("User Groups")).click();
						if (result == 1) {
							driver.findElement(By.xpath("//*[@id='confluence-demo']")).clear();
							driver.findElement(By.id("updateUserOnlyIfRoleMapped")).click();
							driver.findElement(By.xpath("//*[@id='confluence-test']")).sendKeys(user.adminGrp);
							driver.findElement(By.xpath("//*[@id='role-mapping-form']/div[12]/input")).click();
							if (ssoObj.doSSo(Constants.IDP_USERNAME, Constants.IDP_PASSWORD) != 0) {
								return new Testcase(
										"Check update user group otion by mapping user groups and check if user is removed from empty group.",
										"FAIL", "SSO Failed");
							} else {
								groups.add("confluence-test");
								result = checkIfAllGroupsAreAssigned(driver, user.username, groups);
								driver.findElement(By.linkText("miniOrange Single Sign-On")).click();
								driver.findElement(By.linkText("User Groups")).click();
								if (result == 1) {
									return new Testcase(
											"Check update user group otion by mapping user groups and check if user is removed from empty group.",
											"PASS", "----");
								} else if (result == 0) {
									return new Testcase(
											"Check update user group otion by mapping user groups and check if user is removed from empty group.",
											"FAIL",
											"All required groups are not updated for existing user when option is checked.");
								} else {
									return new Testcase(
											"Check update user group otion by mapping user groups and check if user is removed from empty group.",
											"FAIL", "User not created check attribute mapping.");
								}
							}

						} else if (result == 0) {
							return new Testcase(
									"Check update user group otion by mapping user groups and check if user is removed from empty group.",
									"FAIL",
									"All required groups are not updated for existing user when option is unchecked.");
						} else {
							return new Testcase(
									"Check update user group otion by mapping user groups and check if user is removed from empty group.",
									"FAIL", "User not created check attribute mapping.");
						}
					}
				} else if (result == 0) {
					return new Testcase(
							"Check update user group otion by mapping user groups and check if user is removed from empty group.",
							"FAIL", "All required groups are not present for new user");
				} else {
					return new Testcase(
							"Check update user group otion by mapping user groups and check if user is removed from empty group.",
							"FAIL", "User not created check attribute mapping.");
				}

			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return new Testcase(
					"Check update user group otion by mapping user groups and check if user is removed from empty group.",
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
		driver.findElement(By.id("usernameAttribute")).clear();
		driver.findElement(By.id("usernameAttribute")).sendKeys(user.email);
		driver.findElement(By.xpath("//*[@id='emailAttribute']")).clear();
		driver.findElement(By.xpath("//*[@id='emailAttribute']")).sendKeys(user.email);
		driver.findElement(By.id("fullNameAttribute")).clear();
		driver.findElement(By.id("fullNameAttribute")).sendKeys(user.fname);
		driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/input")).click();
		driver.findElement(By.xpath("//*[@id='attribute-mapping-form']/div[9]/a[1]/input")).click();
		deleteUser(driver, user.username);
		deleteUser(driver, Constants.IDP_FIRST_NAME);
	}

	public int checkIfAllGroupsAreAssigned(WebDriver driver, String linkName, List<String> groups) {
		driver.findElement(By.xpath("//*[@id='admin-menu-link']")).click();
		driver.findElement(By.linkText("User management")).click();
		driver.findElement(By.linkText("Show all users")).click();
		if (driver.findElements(By.linkText(linkName)).size() > 0) {
			driver.findElement(By.linkText(linkName)).click();
			for (int i = 0; i < groups.size(); i++) {
				if (driver.findElements(By.linkText(groups.get(i))).size() == 0) {
					driver.findElement(By.linkText("Back to Users")).click();
					return 0;
				}
			}
			driver.findElement(By.linkText("Back to Users")).click();
			return 1;
		} else {
			driver.findElement(By.linkText("Back to Users")).click();
			return -1;
		}
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
		driver.findElement(By.linkText("User Groups")).click();

	}

}
