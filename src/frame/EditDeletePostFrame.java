package frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.DbConn;

public class EditDeletePostFrame extends JFrame {

    private DefaultListModel<String> listModel;
    private JList<String> postList;
    private JTextField oldTitleField;
    private JTextField newTitleField;
    private JTextArea contentArea;
    private JButton saveButton;
    private JButton deleteButton;

    public EditDeletePostFrame() {
        setTitle("学習記録アプリ");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainP = new JPanel();
        mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS));
        mainP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Post List Panel
        JPanel l1 = new JPanel();
        l1.setLayout(new BoxLayout(l1, BoxLayout.Y_AXIS));
        listModel = new DefaultListModel<>();
        postList = new JList<>(listModel);
        postList.addListSelectionListener(e -> loadSelectedPost());

        JScrollPane listScrollPane = new JScrollPane(postList);
        l1.add(new JLabel("ポスト選択:"));
        l1.add(listScrollPane);
        mainP.add(l1);
        mainP.add(Box.createRigidArea(new Dimension(0, 10)));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        oldTitleField = new JTextField();
        oldTitleField.setPreferredSize(new Dimension(400, 30));
        oldTitleField.setEditable(false);

        newTitleField = new JTextField();
        newTitleField.setPreferredSize(new Dimension(400, 30));

        contentArea = new JTextArea(10, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);

        formPanel.add(new JLabel("以前のタイトル:"));
        formPanel.add(oldTitleField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(new JLabel("新しいタイトル:"));
        formPanel.add(newTitleField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(new JLabel("コンテンツ:"));
        formPanel.add(contentScrollPane);
        mainP.add(formPanel);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("変更保存");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePost();
            }
        });

        deleteButton = new JButton("ポスト削除");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePost();
            }
        });

        JButton backButton = new JButton("戻り");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Main();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        mainP.add(Box.createRigidArea(new Dimension(0, 10)));
        mainP.add(buttonPanel);

        add(mainP);
        setVisible(true);
        loadPosts();
    }

    private void loadPosts() {
        listModel.clear();
        try (Connection conn = DbConn.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT title FROM Posts")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                listModel.addElement(title);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "データベースのエラー: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadSelectedPost() {
        String selectedTitle = postList.getSelectedValue();
        if (selectedTitle != null) {
            try (Connection conn = DbConn.connect();
                 PreparedStatement ps = conn.prepareStatement("SELECT content FROM Posts WHERE title = ?")) {

                ps.setString(1, selectedTitle);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String content = rs.getString("content");
                    oldTitleField.setText(selectedTitle);
                    newTitleField.setText(selectedTitle);
                    contentArea.setText(content);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "データベースのエラー: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void updatePost() {
        String oldTitle = oldTitleField.getText().trim();
        String newTitle = newTitleField.getText().trim();
        String content = contentArea.getText().trim();

        if (newTitle.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "タイトルを入力してください。", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DbConn.connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE Posts SET title = ?, content = ? WHERE title = ?")) {

            ps.setString(1, newTitle);
            ps.setString(2, content);
            ps.setString(3, oldTitle);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "成功");
                loadPosts();
            } else {
                JOptionPane.showMessageDialog(this, "失敗");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "データベースのエラー: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deletePost() {
        String title = oldTitleField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "タイトルは空欄にできません。", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "本当に削除しますか？", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DbConn.connect();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM Posts WHERE title = ?")) {

                ps.setString(1, title);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "成功");
                    oldTitleField.setText("");
                    newTitleField.setText("");
                    contentArea.setText("");
                    loadPosts();
                } else {
                    JOptionPane.showMessageDialog(this, "失敗");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "データベースのエラー: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new EditDeletePostFrame();
    }
}
