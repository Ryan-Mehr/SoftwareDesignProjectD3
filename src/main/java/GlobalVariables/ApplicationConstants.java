package GlobalVariables;

public class ApplicationConstants {
    private ApplicationConstants() {
        throw new AssertionError();
    }

    public static final String db_link = "jdbc:mysql://localhost:3306/my_application_db";
    public static final String db_user = "root";
    public static final String db_password = "admin12345";

    public static final int totalCampaignRooms = 3;
}
