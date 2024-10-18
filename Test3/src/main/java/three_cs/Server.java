package three_cs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    String url = "jdbc:mysql://localhost:3306/test?useSSL=false&&characterEncoding=UTF-8";
    String user = "root";
    String password = "123";

    public ArrayList<String> query() {
        ArrayList<String> results = new ArrayList<>();
        // 建立连接
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // 创建Statement对象
            try (Statement stmt = conn.createStatement()) {
                // 执行查询
                String sql = "SELECT * FROM user";
                ResultSet rs = stmt.executeQuery(sql);

                // 处理结果
                while (rs.next()) {
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    String phone = rs.getString("phone");

                    results.add(name + "        " + address + "        " + phone);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public void delete(int id) {
        // 删除指定ID的数据
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // 创建PreparedStatement对象
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM user WHERE id = ?")) {
                // 设置参数
                pstmt.setInt(1, id);

                // 执行删除
                int affectedRows = pstmt.executeUpdate();
                System.out.println("Deleted rows: " + affectedRows);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(int id, String name, String address, String phone) {
        String sql = "INSERT INTO user (id,name, address, phone) VALUES (?,?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, address);
            pstmt.setString(4, phone);
            // 执行添加
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Add rows: " + affectedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, String name, String address, String phone) {
        // 构建基础 SQL 更新语句
        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            updates.add("name = ?");
            params.add(name);
        }
        if (address != null && !address.isEmpty()) {
            updates.add("address = ?");
            params.add(address);
        }
        if (phone != null && !phone.isEmpty()) {
            updates.add("phone = ?");
            params.add(phone);
        }

        // 如果没有任何更新，则直接返回
        if (updates.isEmpty()) {
            System.out.println("No updates to perform.");
            return;
        }

        // 构建完整的 SQL 更新语句
        String sql = "UPDATE user SET " + String.join(", ", updates) + " WHERE id = ?";
        params.add(id); // 添加 id 参数

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置 SQL 语句的参数
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            // 执行更新
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Update rows: " + affectedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}