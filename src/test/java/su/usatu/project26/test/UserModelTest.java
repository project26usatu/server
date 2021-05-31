package su.usatu.project26.test;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import su.usatu.project26.model.User;

public class UserModelTest {
	static User user;

	@BeforeClass
	public static void beforeClass() {
		user = new User();
	}

	@Test
	public void userGetSetTest() {
		user.setId(10);
		assertNotNull(user.getId());
		user.setUsername("username");
		assertNotNull(user.getUsername());
		user.setPassword("password");
		assertNotNull(user.getPassword());
		user.setSalt("stringWithSalt");
		assertNotNull(user.getSalt());
		user.setEmail("example@example.com");
		assertNotNull(user.getEmail());
		user.setFullName("John Doe");
		assertNotNull(user.getFullName());
		user.setGroupId(10);
		assertNotNull(user.getGroupId());
		user.setApiToken("token");
		assertNotNull(user.getApiToken());
		user.setMeterMode(3);
		assertNotNull(user.getMeterMode());
		user.setRatesSetId(2);
		assertNotNull(user.getRatesSetId());
	}
}
