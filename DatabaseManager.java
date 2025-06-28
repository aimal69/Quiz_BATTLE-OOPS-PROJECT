import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private Connection conn;

    public DatabaseManager() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Try different connection string options to resolve access issues
            String url = "jdbc:mysql://localhost:3306/Quiz_application?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            conn = DriverManager.getConnection(url, "root", "Aimalkhan123");
            System.out.println("Database connection successful!");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database connection failed!");
            if (e instanceof SQLException) {
                SQLException sqlEx = (SQLException) e;
                System.err.println("Error Code: " + sqlEx.getErrorCode());
                System.err.println("SQL State: " + sqlEx.getSQLState());
            }
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Question> getQuestions() {
        return getQuestionsBySubject("General");
    }

    public List<Question> getQuestionsBySubject(String subject) {
        List<Question> list = new ArrayList<>();
        try {
            if (conn == null) {
                System.err.println("Database connection is null!");
                return list;
            }

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM questions WHERE subject = ? ORDER BY RAND() LIMIT 5");
            ps.setString(1, subject);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Question(
                    rs.getString("question"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_option").charAt(0)
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving questions for subject: " + subject);
            e.printStackTrace();
        }
        return list;
    }

    public void saveScore(String name, int score) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO players (name, score) VALUES (?, ?)");
            ps.setString(1, name);
            ps.setInt(2, score);
            ps.executeUpdate();
            System.out.println("Score saved: " + name + " - " + score);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveScoreWithSubject(String name, int score, String subject) {
        try {
            // First save to the main players table
            saveScore(name, score);

            // Also save with subject information if we want to track subject-specific high scores
            PreparedStatement ps = conn.prepareStatement("INSERT INTO players (name, score) VALUES (?, ?)");
            ps.setString(1, name + " (" + subject + ")");
            ps.setInt(2, score);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getHighestScore() {
        String result = "No scores yet.";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, score FROM players ORDER BY score DESC LIMIT 1");
            if (rs.next()) {
                result = "Top Score: " + rs.getInt("score") + " by " + rs.getString("name");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
