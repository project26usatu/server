package su.usatu.project26.dao;

import java.io.IOException;

import su.usatu.project26.model.*;

public interface Project26DAO {
	public Rates getRatesById(int id);

	public void editRates(Rates updatingRates, String editorName);

	public boolean addUser(User user, String tableName);

	public User getUserInfo(String token, String tableName);
	
	public boolean updateUserInfo(String token, User user);

	public boolean checkDbValueIfUnique(String rowLabel, String value, String tableName);

	public boolean checkLoginPasswordMatch(User user, String tableName);

	public boolean assignApiToken(User user, String tableName);

	public int getUserGroupByToken(String token, String tableName);

	public String createPdfReport(ReportData dataForPDF) throws IllegalStateException, IOException;
	
	public boolean assignPdfReportToUser(String token, String documentName);
	
	public String[] getUserPdfFiles(int userId);
	
	public boolean deletePdfReport(String token, String documentName);
}
