package frame;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import util.DbConn;

public class PostsListFrame extends JFrame {

    private DefaultListModel<String> listModel;
    private JList<String> postList;

    public PostsListFrame() {
        setTitle("学習記録アプリ");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel<>();
        postList = new JList<>(listModel);
        postList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedTitle = postList.getSelectedValue();
                    if (selectedTitle != null) {
                        showPostDetails(selectedTitle);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(postList);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("戻り");
        backButton.addActionListener(e -> {
            dispose();
            new Main();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadPosts();
        setVisible(true);
    }

    private void loadPosts() {
        try (Connection conn = DbConn.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT title FROM Posts");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String title = rs.getString("title");
                listModel.addElement(title);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "データベースのエラー: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void showPostDetails(String title) {
        try (Connection conn = DbConn.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT content, date FROM Posts WHERE title = ?")) {

            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String content = rs.getString("content");
                String date = rs.getString("date");

                JOptionPane.showMessageDialog(this,
                    "タイトル: " + title + "\n日付: " + date + "\n\nコンテンツ:\n" + content,
                    "内容",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "選択した投稿の詳細が見つかりません。", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "データベースのエラー: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PostsListFrame();
    }
}
