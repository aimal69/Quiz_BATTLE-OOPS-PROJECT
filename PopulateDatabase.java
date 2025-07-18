import java.sql.*;

public class PopulateDatabase {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quiz_database?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root", "Islamian0903");
            
            // Create users table if not exists
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) UNIQUE NOT NULL, password VARCHAR(100) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            // Add subject column to questions table if not exists
            try {
                stmt.execute("ALTER TABLE questions ADD COLUMN subject VARCHAR(50) DEFAULT 'General'");
            } catch (SQLException e) {
                // Column might already exist
            }
            
            // Insert test user
            PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO users (username, password) VALUES (?, ?)");
            ps.setString(1, "test");
            ps.setString(2, "test123");
            ps.executeUpdate();
            
            // Insert sample questions
            ps = conn.prepareStatement("INSERT IGNORE INTO questions (question, option_a, option_b, option_c, option_d, correct_option, subject) VALUES (?, ?, ?, ?, ?, ?, ?)");
            
            // Mathematics questions
            addQuestion(ps, "What is 15 + 27?", "42", "41", "43", "40", "A", "Mathematics");
            addQuestion(ps, "What is the square root of 64?", "6", "7", "8", "9", "C", "Mathematics");
            addQuestion(ps, "What is 12 × 8?", "94", "95", "96", "97", "C", "Mathematics");
            addQuestion(ps, "What is 144 ÷ 12?", "11", "12", "13", "14", "B", "Mathematics");
            addQuestion(ps, "What is 2³?", "6", "8", "9", "10", "B", "Mathematics");
            
            // Science questions
            addQuestion(ps, "What is the chemical symbol for water?", "H2O", "CO2", "O2", "H2", "A", "Science");
            addQuestion(ps, "How many bones are in the human body?", "205", "206", "207", "208", "B", "Science");
            addQuestion(ps, "What planet is closest to the Sun?", "Venus", "Earth", "Mercury", "Mars", "C", "Science");
            addQuestion(ps, "What gas do plants absorb from the atmosphere?", "Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen", "C", "Science");
            addQuestion(ps, "What is the speed of light?", "300,000 km/s", "150,000 km/s", "450,000 km/s", "200,000 km/s", "A", "Science");
            
            // History questions
            addQuestion(ps, "In which year did World War II end?", "1944", "1945", "1946", "1947", "B", "History");
            addQuestion(ps, "Who was the first President of the United States?", "Thomas Jefferson", "John Adams", "George Washington", "Benjamin Franklin", "C", "History");
            addQuestion(ps, "Which empire was ruled by Julius Caesar?", "Greek Empire", "Roman Empire", "Persian Empire", "Egyptian Empire", "B", "History");
            addQuestion(ps, "In which year did the Titanic sink?", "1910", "1911", "1912", "1913", "C", "History");
            addQuestion(ps, "Who painted the Mona Lisa?", "Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Michelangelo", "C", "History");
            
            // Geography questions
            addQuestion(ps, "What is the capital of Australia?", "Sydney", "Melbourne", "Canberra", "Perth", "C", "Geography");
            addQuestion(ps, "Which is the longest river in the world?", "Amazon", "Nile", "Mississippi", "Yangtze", "B", "Geography");
            addQuestion(ps, "What is the smallest country in the world?", "Monaco", "Vatican City", "San Marino", "Liechtenstein", "B", "Geography");
            addQuestion(ps, "Which mountain range contains Mount Everest?", "Andes", "Rocky Mountains", "Alps", "Himalayas", "D", "Geography");
            addQuestion(ps, "What is the largest ocean on Earth?", "Atlantic", "Indian", "Arctic", "Pacific", "D", "Geography");
            
            // English questions
            addQuestion(ps, "Who wrote Romeo and Juliet?", "Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain", "B", "English");
            addQuestion(ps, "What is the plural of child?", "childs", "childes", "children", "child", "C", "English");
            addQuestion(ps, "Which word is a synonym for happy?", "sad", "angry", "joyful", "tired", "C", "English");
            addQuestion(ps, "What type of word is quickly?", "noun", "verb", "adjective", "adverb", "D", "English");
            addQuestion(ps, "Who wrote Pride and Prejudice?", "Emily Bronte", "Charlotte Bronte", "Jane Austen", "Virginia Woolf", "C", "English");
            
            // Computer questions
            addQuestion(ps, "What does CPU stand for?", "Computer Processing Unit", "Central Processing Unit", "Central Program Unit", "Computer Program Unit", "B", "Computer");
            addQuestion(ps, "Which programming language is known for web development?", "C++", "Java", "JavaScript", "Python", "C", "Computer");
            addQuestion(ps, "What does HTML stand for?", "Hypertext Markup Language", "High Tech Modern Language", "Home Tool Markup Language", "Hyperlink and Text Markup Language", "A", "Computer");
            addQuestion(ps, "Which company developed the Windows operating system?", "Apple", "Google", "Microsoft", "IBM", "C", "Computer");
            addQuestion(ps, "What is the binary representation of the decimal number 8?", "1000", "1001", "1010", "1100", "A", "Computer");
            
            System.out.println("Database populated successfully!");
            conn.close();
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static void addQuestion(PreparedStatement ps, String question, String a, String b, String c, String d, String correct, String subject) throws SQLException {
        ps.setString(1, question);
        ps.setString(2, a);
        ps.setString(3, b);
        ps.setString(4, c);
        ps.setString(5, d);
        ps.setString(6, correct);
        ps.setString(7, subject);
        ps.executeUpdate();
    }
}
