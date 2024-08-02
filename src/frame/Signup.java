package frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import util.DbConn;

public class Signup extends JFrame {

    Signup() {
        setTitle("学習記録アプリ");
        setSize(300, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainP = new JPanel();


        JPanel p1 = new JPanel();
        JLabel l1 = new JLabel("ID");
        JTextField f1 = new JTextField(15);
        p1.add(l1);
        p1.add(f1);
        mainP.add(p1);


        JPanel p2 = new JPanel();
        JLabel l2 = new JLabel("PW");
        JPasswordField f2 = new JPasswordField(15);
        p2.add(l2);
        p2.add(f2);
        mainP.add(p2);


        JPanel p3 = new JPanel();
        JLabel l3 = new JLabel("CHECK PW");
        JPasswordField f3 = new JPasswordField(15);
        p3.add(l3);
        p3.add(f3);
        mainP.add(p3);


        JPanel p4 = new JPanel();
        JLabel l4 = new JLabel("GENDER");
        JRadioButton maleButton = new JRadioButton("Male");
        JRadioButton femaleButton = new JRadioButton("Female");
        ButtonGroup bg = new ButtonGroup();
        bg.add(maleButton);
        bg.add(femaleButton);
        p4.add(l4);
        p4.add(maleButton);
        p4.add(femaleButton);
        mainP.add(p4);


        JPanel p5 = new JPanel();
        JLabel l5 = new JLabel("NAME");
        JTextField f5 = new JTextField(15);
        p5.add(l5);
        p5.add(f5);
        mainP.add(p5);

        JPanel p6 = new JPanel();
        JLabel l6 = new JLabel("E-MAIL");
        JTextField f6 = new JTextField(20);
        p6.add(l6);
        p6.add(f6);
        mainP.add(p6);


        JPanel p7 = new JPanel();
        JButton b1 = new JButton("戻り");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login();
            }
        });

        JButton b2 = new JButton("確認");
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = f1.getText().trim();
                String pw = new String(f2.getPassword()).trim();
                String checkPw = new String(f3.getPassword()).trim();
                String gender = maleButton.isSelected() ? "Male" : (femaleButton.isSelected() ? "Female" : "");
                String name = f5.getText().trim();
                String email = f6.getText().trim();

                if (id.isEmpty() || pw.isEmpty() || checkPw.isEmpty() || gender.isEmpty() || name.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "すべての情報を入力してください。");
                    return;
                }

                if (!pw.equals(checkPw)) {
                    JOptionPane.showMessageDialog(null,"パスワードが一致しません。");
                    return;
                }

                try (Connection conn = DbConn.connect();
                     PreparedStatement ps = conn.prepareStatement("INSERT INTO Users (username, password, name, gender, email) VALUES (?, ?, ?, ?, ?)")) {

                    ps.setString(1, id);
                    ps.setString(2, pw);
                    ps.setString(3, name);
                    ps.setString(4, gender);
                    ps.setString(5, email);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("成功");
                        dispose();
                        new Login();
                    } else {
                        System.out.println("失敗");
                    }
                } catch (SQLException ex) {
                    System.out.println("データベースのエラー: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        p7.add(b1);
        p7.add(b2);
        mainP.add(p7);

        add(mainP);
        setVisible(true);
    }
}
