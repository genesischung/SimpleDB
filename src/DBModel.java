import com.google.gson.*;

import java.sql.*;

public class DBModel {

    // Process a SQL query
    // return the results as a JSON string
    // The default user name is "root" and default password is "password"
    public static JsonArray processSQL(String query) throws SQLException {
        String user_name = "root";
        String pw = "password";
        String conn_url = "jdbc:mysql://localhost:3306/employees?user=" +
                user_name + "&password=" + pw;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(conn_url);
            ResultSet rs = null;
            Statement st = conn.createStatement();
            st.execute(query);
            return rsToJson(st.getResultSet());
        } finally{
            if (conn != null)
                conn.close();
        }
    }

    // Process ResultSet and store data into JsonArray
    public static JsonArray rsToJson(ResultSet rs) throws SQLException {
        JsonArray jsonArray = new JsonArray();
        ResultSetMetaData rsMeta = rs.getMetaData();
        int columnCount = rsMeta.getColumnCount();
        final Gson gson = new Gson();
        while (rs.next()) {
            JsonObject obj = new JsonObject();
            for (int i = 0; i < columnCount; i++) {
                obj.add(rsMeta.getColumnLabel(i + 1),
                        gson.toJsonTree(rs.getObject(i + 1)));
            }
            jsonArray.add(obj);
        }
        return jsonArray;
    }
}
