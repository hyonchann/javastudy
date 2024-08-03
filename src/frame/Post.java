package frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import util.DbConn;

public class Post extends JFrame {

    public Post() {
        setTitle("学習記録アプリ");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        JPanel mainP = new JPanel();
        mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS));
        mainP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel t1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel l1 = new JLabel("タイトル:");
        JTextField f1 = new JTextField(30);
        t1.add(l1);
        t1.add(f1);
        mainP.add(t1);
        mainP.add(Box.createRigidArea(new Dimension(0, 10)));


        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel l2 = new JLabel("日付:");
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setPreferredSize(new Dimension(150, 30));
        p2.add(l2);
        p2.add(dateSpinner);
        mainP.add(p2);
        mainP.add(Box.createRigidArea(new Dimension(0, 10)));


        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
        JLabel l3 = new JLabel("コンテンツ:");
        JTextArea contentArea = new JTextArea(10, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        p3.add(l3);
        p3.add(scrollPane);
        mainP.add(p3);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton b1 = new JButton("保存");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = f1.getText().trim();
                String content = contentArea.getText().trim();
                Date selectedDate = (Date) dateSpinner.getValue();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

                if (title.isEmpty() || content.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"すべてのフィールドに入力してください。");
                    return;
                }

                try (Connection conn = DbConn.connect();
                     PreparedStatement ps =
                    conn.prepareStatement("INSERT INTO Posts (title, content, date) VALUES (?, ?, ?)")) {

                    ps.setString(1, title);
                    ps.setString(2, content);
                    ps.setString(3, date);

                    int result = ps.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(null,"成功");
                        dispose();
                        new Main();
                    } else {
                    	JOptionPane.showMessageDialog(null,"失敗");
                    }
                } catch (SQLException ex) {
                    System.out.println("Database error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });



        JButton b2 = new JButton("戻り");
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Main();
            }
        });

        buttonPanel.add(b1);

        buttonPanel.add(b2);
        mainP.add(Box.createRigidArea(new Dimension(0, 10)));
        mainP.add(buttonPanel);

        add(mainP);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Post();
    }
}
