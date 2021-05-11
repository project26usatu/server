package su.usatu.project26.util;

import com.google.gson.Gson;

import su.usatu.project26.model.JsonResponse;

public class JsonResponseUtil {

	public JsonResponseUtil() {
	}

	// Form response on failure

	public static String formJsonResponse(String status, String message) {

		JsonResponse jsonObject = new JsonResponse();

		jsonObject.success = false;

		if (status == "failure") {
			jsonObject.errorMessage = message;
		} else {
			jsonObject.errorMessage = "Unknown JsonResponse status name";
		}

		String jsonString = new Gson().toJson(jsonObject);

		return jsonString;
	}

	// Form response on success

	public static String formJsonResponse(String status, String message, Object objectToPass) {

		JsonResponse jsonObject = new JsonResponse();

		if (status == "success") {
			jsonObject.success = true;
			jsonObject.errorMessage = message;
			jsonObject.responseBody = objectToPass;
		} else {
			jsonObject.success = false;
			jsonObject.errorMessage = "Unknown JsonResponse status name";
		}

		String jsonString = new Gson().toJson(jsonObject);

		return jsonString;
	}

}
