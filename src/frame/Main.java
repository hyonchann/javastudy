package frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {

    public Main() {
        setTitle("学習記録アプリ");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainP = new JPanel(new GridLayout(5, 1));

        JPanel p1 = new JPanel();
        JButton b1 = new JButton("学習内容追加");
        b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Post();

			}
		});
        p1.add(b1);
        mainP.add(p1);

        JPanel p2 = new JPanel();
        JButton b2 = new JButton("学習内容表示");
        b2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();
				new PostsListFrame();

			}
		});
        p2.add(b2);
        mainP.add(p2);

        JPanel p3 = new JPanel();
        JButton b3 = new JButton("特定学習内容表示");
        b3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();
				new PostsByDateFrame();

			}
		});
        p3.add(b3);
        mainP.add(p3);

        JPanel p4 = new JPanel();
        JButton b4 = new JButton("学習内容編集&削除");
        b4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {


				dispose();
				new EditDeletePostFrame();

			}
		});
        p4.add(b4);
        mainP.add(p4);

        JPanel p5 = new JPanel();
        JButton b5 = new JButton("戻り");
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login();
            }
        });
        p5.add(b5);
        mainP.add(p5);

        add(mainP);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
