package chatClient;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import java.awt.Font;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldname;
	// private JTextField textFieldpass;
	private JPasswordField textFieldpass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 300, 662, 623);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel pic = new JLabel();
		pic.setBounds(0, 0, 640, 256);
		pic.setIcon(new ImageIcon(LoginFrame.class.getResource("/chatclient/login.jpg")));
		contentPane.add(pic);

		JLabel lblNewLabel = new JLabel("\u7528\u6237\u540D\uFF1A");
		lblNewLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 20));
		lblNewLabel.setBounds(98, 291, 89, 33);
		contentPane.add(lblNewLabel);

		textFieldname = new JTextField();
		textFieldname.setBounds(213, 292, 189, 33);
		contentPane.add(textFieldname);
		textFieldname.setColumns(10);

		JLabel lblPassword = new JLabel("\u5BC6\u7801\uFF1A");
		lblPassword.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 20));
		lblPassword.setBounds(98, 367, 89, 33);
		contentPane.add(lblPassword);

		textFieldpass = new JPasswordField();
		textFieldpass.setColumns(10);
		textFieldpass.setBounds(213, 368, 189, 33);
		contentPane.add(textFieldpass);

		JButton btnNewButton = new JButton("Login");
		btnNewButton.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 24));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// make a commucation protocol between sever and client
				// 101|name,pass -- login to sever
				// 102| -- ask sever for user list
				// 103|from,to.message -- talk to other user
				try {
					Socket s = new Socket("localhost", 4001);
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					DataInputStream dis = new DataInputStream(s.getInputStream());
					dos.writeUTF("101|" + textFieldname.getText() + "," + textFieldpass.getText());
					dos.flush();
					String feedback = dis.readUTF();
					if (feedback.equals("success")) {
						// show MainFrame ,hide LoginFrame
						MainFrame mainFrame = new MainFrame(s, textFieldname.getText());
						LoginFrame.this.setVisible(false);
						mainFrame.setVisible(true);
					} else if (feedback.equals("failure")) {
						JOptionPane.showMessageDialog(null, "Login failed,please check your username and password");
					} else {
						JOptionPane.showMessageDialog(null, "Login failed£¬you have already logged in");
					}

				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		btnNewButton.setBounds(184, 485, 282, 54);
		contentPane.add(btnNewButton);

		JCheckBox chckbxNewCheckBox = new JCheckBox("\u8BB0\u4F4F\u5BC6\u7801");
		chckbxNewCheckBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 18));
		chckbxNewCheckBox.setBounds(184, 433, 145, 33);
		contentPane.add(chckbxNewCheckBox);

		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("\u81EA\u52A8\u767B\u9646");
		chckbxNewCheckBox_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 18));
		chckbxNewCheckBox_1.setBounds(350, 435, 116, 29);
		contentPane.add(chckbxNewCheckBox_1);

		JButton btnSignIn = new JButton("\u6CE8\u518C\u8D26\u53F7");
		btnSignIn.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 20));
		btnSignIn.setBounds(463, 293, 123, 29);
		contentPane.add(btnSignIn);

		btnSignIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					URI uriSignIn = new URI("http://zc.qq.com/chs/index.html");
					Desktop.getDesktop().browse(uriSignIn);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		JButton btnForgot = new JButton("\u627E\u56DE\u5BC6\u7801");
		btnForgot.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 20));
		btnForgot.setBounds(463, 369, 123, 29);
		contentPane.add(btnForgot);

		btnForgot.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					URI uriForgot = new URI(
							"https://aq.qq.com/cn2/findpsw/pc/pc_find_pwd_input_account?source_id=1035");
					Desktop.getDesktop().browse(uriForgot);
				} catch (URISyntaxException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}

			}
		});

	}
}
