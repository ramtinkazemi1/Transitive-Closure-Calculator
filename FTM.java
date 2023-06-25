import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FTM {

    private static final String CREATE_INFLUENCE_TABLE = "CREATE TABLE influence " +
        "(ID INT PRIMARY KEY ," +
        " Who VARCHAR(550), " +
        " Whom VARCHAR(550))";

    private static final String SELECT_FROM_DEPOSITOR = "SELECT * FROM depositor WHERE ano = ?";

    public static void main(String[] args) {
        String connURL = args[0];
        String userName = args[1];
        String password = args[2];

        connectAndTest(connURL, userName, password);
    }

    public static void connectAndTest(String connURL, String userName, String password) {
        try (Connection conn = DriverManager.getConnection(connURL, userName, password)) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "influence", null);

            if (tables.next()) {
                System.out.println("Table 'influence' exists");
                Statement stmt = conn.createStatement();
                String query = "DROP TABLE influence";
                stmt.execute(query);
            } else {
                System.out.println("Table 'influence' does not exist");
            }

            getInfluenceTableData(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getInfluenceTableData(Connection conn) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM transfer")) {

            System.out.println("Connected");

            List<String> whoList = new ArrayList<>();
            List<String> whomList = new ArrayList<>();

            while (rs.next()) {
                System.out.println("src: " + rs.getString("src"));

                try (PreparedStatement stmtForWho = conn.prepareStatement(SELECT_FROM_DEPOSITOR);
                     PreparedStatement stmtForWhom = conn.prepareStatement(SELECT_FROM_DEPOSITOR)) {

                    stmtForWho.setString(1, rs.getString("src"));
                    ResultSet rsForWho = stmtForWho.executeQuery();

                    while (rsForWho.next()) {
                        System.out.println("cname: " + rsForWho.getString("cname"));
                        whoList.add(rsForWho.getString("cname"));
                    }

                    stmtForWhom.setString(1, rs.getString("tgt"));
                    ResultSet rsForWhom = stmtForWhom.executeQuery();

                    while (rsForWhom.next()) {
                        System.out.println("cname: " + rsForWhom.getString("cname"));
                        whomList.add(rsForWhom.getString("cname"));
                    }
                }
                System.out.println(whoList);
                System.out.println(whomList);
            }

            createInfluenceTable(conn, whoList, whomList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createInfluenceTable(Connection conn, List<String> whoList, List<String> whomList) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_INFLUENCE_TABLE);

            for (int i = 0; i < whoList.size(); i++) {
                System.out.println(whoList.get(i));
                insertDataToInfluenceTable(conn, i + 1, whoList.get(i), whomList.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertDataToInfluenceTable(Connection conn, int id, String who, String whom) {
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO influence (ID, Who, Whom) VALUES (?, ?, ?)")) {
            st.setInt(1, id);
            st.setString(2, who);
            st.setString(3, whom);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
