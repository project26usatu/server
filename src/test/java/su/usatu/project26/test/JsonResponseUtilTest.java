package su.usatu.project26.test;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

import com.google.gson.Gson;

import su.usatu.project26.model.JsonResponse;
import su.usatu.project26.util.JsonResponseUtil;

public class JsonResponseUtilTest {

	@Test
	public void testInvalidJson() {

		String invalidJsonResponse = JsonResponseUtil.formJsonResponse("success", "message");

		Gson gson = new Gson();
		JsonResponse jr = gson.fromJson(invalidJsonResponse, JsonResponse.class);
		
		assertFalse(jr.success);

	}

}
