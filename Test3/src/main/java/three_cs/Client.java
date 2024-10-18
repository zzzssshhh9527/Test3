package three_cs;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Client extends JFrame {
    private JButton addButton;
    private JButton deleteButton;
    private DefaultListModel<String> listModel;
    private JList<String> userList;
    Server server;

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

        server = new Server();
        ArrayList<String> results = server.query();
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
                server.update(id, name, address, phone);
                listModel.clear();
                ArrayList<String> results = server.query();
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
            server.add(id, name, address, phone);
        }
    }

    private void deleteButtonActionPerformed(ActionEvent e) {
        int selectedIndex = userList.getSelectedIndex();
        if (selectedIndex != -1) {
            listModel.remove(selectedIndex);
            server.delete(selectedIndex);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client frame = new Client();
            frame.setVisible(true);
        });
    }
}