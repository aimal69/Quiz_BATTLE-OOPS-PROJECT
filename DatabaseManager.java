import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private Connection conn;

    public DatabaseManager() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quizdb", "root", "Islamian0903");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getQuestions() {
        List<Question> list = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM questions");
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
