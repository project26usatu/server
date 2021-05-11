package su.usatu.project26.dao;

import su.usatu.project26.model.*;

public interface Project26DAO {
	public Rates selectRates(String tableName);
	public void editRates(Rates updatingRates, String tableName, String userName);
	public void addUser(User user, String tableName);
	public User getUserInfo(String token, String tableName);
	public boolean checkUsernamePassword(User user, String tableName);
	public boolean assignApiToken(User user, String tableName);
	public int getUserGroupByToken(String token, String tableName);
}
