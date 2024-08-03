package frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import util.DbConn;

public class PostsByDateFrame extends JFrame {

    private DefaultListModel<String> listModel;
    private JList<String> postList;
    private JSpinner dateSpinner;

    public PostsByDateFrame() {
        setTitle("学習記録アプリ");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        JPanel mainP = new JPanel();
        mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS));
        mainP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setPreferredSize(new Dimension(150, 30));
        datePanel.add(dateSpinner);

        JButton loadButton = new JButton("確認");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPostsForSelectedDate();
            }
        });

        datePanel.add(loadButton);
        mainP.add(datePanel);
        mainP.add(Box.createRigidArea(new Dimension(0, 10)));

        // List Panel
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

        JScrollPane listScrollPane = new JScrollPane(postList);
        mainP.add(listScrollPane);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("戻り");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Main();
            }
        });

        buttonPanel.add(backButton);
        mainP.add(buttonPanel);

        add(mainP);
        setVisible(true);
    }

    private void loadPostsForSelectedDate() {
        listModel.clear();
        Date selectedDate = (Date) dateSpinner.getValue();
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

        try (Connection conn = DbConn.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT title FROM Posts WHERE date = ?")) {

            ps.setString(1, formattedDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                listModel.addElement(title);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "データベースのエラー:" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        new PostsByDateFrame();
    }
}
