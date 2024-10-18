package two_cs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Client extends JFrame {
    String url = "jdbc:mysql://localhost:3306/test?useSSL=false&&characterEncoding=UTF-8";
    String user = "root";
    String password = "123";

    private JButton addButton;
    private JButton deleteButton;
    private DefaultListModel<String> listModel;
    private JList<String> userList;

    public Client() {
        setTitle("个人通讯录");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        add(new JScrollPane(userList), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        add(panel, BorderLayout.SOUTH);


        addButton = new JButton("添加");
        addButton.addActionListener(this::addButtonActionPerformed);
        panel.add(addButton);
        ;

        addButton = new JButton("修改");
        addButton.addActionListener(this::updataButtonActionPerformed);
        panel.add(addButton);

        deleteButton = new JButton("删除");
        deleteButton.addActionListener(this::deleteButtonActionPerformed);
        panel.add(deleteButton);

        ArrayList<String> results = query();
        SwingUtilities.invokeLater(() -> {
            for (String user : results) {
                listModel.addElement(user);
            }
        });
    }

    private void updataButtonActionPerformed(ActionEvent e) {
        int id = userList.getSelectedIndex();
        if (id != -1) {
            // 创建一个面板来包含输入字段
            JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
            JTextField nameField = new JTextField();
            JTextField addressField = new JTextField();
            JTextField phoneField = new JTextField();

            panel.add(new JLabel("姓名:"));
            panel.add(nameField);
            panel.add(new JLabel("地址:"));
            panel.add(addressField);
            panel.add(new JLabel("电话:"));
            panel.add(phoneField);

            // 显示对话框并获取用户输入
            int result = JOptionPane.showConfirmDialog(Client.this, panel, "修改联系人信息", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText();

                String contactInfo = name + "        " + address + "        " + phone;
                // 将联系人信息发送到服务器
                update(id, name, address, phone);
                listModel.clear();
                ArrayList<String> results = query();
                SwingUtilities.invokeLater(() -> {
                    for (String user : results) {
                        listModel.addElement(user);
                    }
                });
            }
        }
    }

    private void addButtonActionPerformed(ActionEvent e) {
        int id = userList.getLastVisibleIndex();

        id++;
        // 创建一个面板来包含输入字段
        JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();

        panel.add(new JLabel("姓名:"));
        panel.add(nameField);
        panel.add(new JLabel("地址:"));
        panel.add(addressField);
        panel.add(new JLabel("电话:"));
        panel.add(phoneField);

        // 显示对话框并获取用户输入
        int result = JOptionPane.showConfirmDialog(Client.this, panel, "请输入联系人信息", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();

            String contactInfo = name + "        " + address + "        " + phone;
            listModel.addElement(contactInfo);
            // 将联系人信息发送到服务器
            add(id, name, address, phone);
        }
    }

    private void deleteButtonActionPerformed(ActionEvent e) {
        int selectedIndex = userList.getSelectedIndex();
        if (selectedIndex != -1) {
            listModel.remove(selectedIndex);
            delete(selectedIndex);
        }
    }

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
        java.util.List<String> updates = new ArrayList<>();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client frame = new Client();
            frame.setVisible(true);
        });
    }
}