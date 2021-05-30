package su.usatu.project26.model;

public class User {

	private int id;
	private String username;
	private String password;
	private String salt;
	private String email;
	private String fullName;
	private int groupId;
	private String apiToken;
	private int meterMode;
	private int ratesSetId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String lastName) {
		this.fullName = lastName;
	}
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	
	public int getMeterMode() {
		return meterMode;
	}
	public void setMeterMode(int meterMode) {
		this.meterMode = meterMode;
	}
	
	public int getRatesSetId() {
		return ratesSetId;
	}
	public void setRatesSetId(int ratesSetId) {
		this.ratesSetId = ratesSetId;
	}
		
}
