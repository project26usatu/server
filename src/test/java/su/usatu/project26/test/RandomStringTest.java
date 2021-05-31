package su.usatu.project26.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import su.usatu.project26.util.StringUtil;

public class RandomStringTest {
	@Test
	public void StringLengthTest() {

		String testString = StringUtil.generateRandomString();
		assertTrue(testString.length() == 24);

		String secondTestString = StringUtil.generateRandomString();
		assertTrue(secondTestString.length() == 24);

		assertFalse(testString.equals(secondTestString));

	}
}
