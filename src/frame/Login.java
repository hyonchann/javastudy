package frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import util.DbConn;

public class Login extends JFrame {

    public Login() {



        setTitle("学習記録アプリ");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainP = new JPanel(new GridLayout(3,1));

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
        JButton b1 = new JButton("LOGIN");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = f1.getText().trim();
                String pw = new String(f2.getPassword()).trim();

                if (id.isEmpty() || pw.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"IDとPWを確認してください。");
                    return;
                }

                try (Connection conn = DbConn.connect();
                     PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?")) {

                    ps.setString(1, id);
                    ps.setString(2, pw);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,"成功");
                        dispose();
                        new Main();
                    } else {
                        JOptionPane.showMessageDialog(null, "IDまたはPWが間違えています。");
                    }
                } catch (SQLException ex) {
                    System.out.println("データベースのエラー");
                    ex.printStackTrace();
                }
            }
        });

        JButton b2 = new JButton("SIGNUP");
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Signup();
            }
        });

        p3.add(b1);
        p3.add(b2);
        mainP.add(p3);

        add(mainP);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}
