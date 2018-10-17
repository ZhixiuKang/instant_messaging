package chatClient;

import java.awt.BorderLayout;
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
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;

import java.awt.Point;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.awt.Font;
import java.awt.Component;
import java.awt.Color;
import java.awt.Panel;
import java.awt.SystemColor;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Socket socket = null;
	private JPanel contentPane = new JPanel();
	private String name; // 客户端人的名字
	private JComboBox Receiver = new JComboBox(); // 好友列表
	public JTextPane scannerbox = new JTextPane(); // 聊天输入框
	public JTextPane chatbox = new JTextPane();; // 聊天显示框
	public ArrayList<Index> indexs = new ArrayList<Index>();
	public String message;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;

	public static SimpleAttributeSet timeAttributeSet = new SimpleAttributeSet();
	public static SimpleAttributeSet textAttributeSet = new SimpleAttributeSet();

	public JTextPane getChatbox() {
		return chatbox;
	}

	public JComboBox getReceiver() {
		return Receiver;
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(Socket socket, String string) {

		this.socket = socket;

		try {
			dos = new DataOutputStream(MainFrame.this.socket.getOutputStream());
			dis = new DataInputStream(MainFrame.this.socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setTitle(string);
		name = string;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 300, 889, 673);
		contentPane.setBackground(SystemColor.inactiveCaptionBorder);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		/**
		 *
		 */
		// setUndecorated(true);

		StyleConstants.setFontSize(timeAttributeSet, 12);
		StyleConstants.setFontSize(textAttributeSet, 20);

		// 插入聊天表情
		// JButton expression = new JButton("");
		JButton expression = new JButton(new ImageIcon(MainFrame.class.getResource("/chatclient/expression.png")));
		/*
		 * 设置为透明
		 */
		expression.setOpaque(true);

		expression.setForeground(new Color(0, 0, 0));
		expression.setBackground(new Color(0, 0, 0));
		expression.setBounds(147, 330, 42, 42);
		expression.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		expression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Point xy = expression.getLocationOnScreen();
				double x = xy.getX();
				double y = xy.getY();
				int x1 = (int) x;
				int y1 = (int) y;
				new Expression(MainFrame.this, x1, y1); // 创建组件并设置显示位置
			}
		});

		// 发送
		JButton send = new JButton("");
		send.setIcon(new ImageIcon(MainFrame.class.getResource("/chatclient/send.png")));
		send.setBounds(670, 560, 96, 42);
		send.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// make a commucation protocol between sever and client
				// 101|name,pass -- login to sever
				// 102| -- ask sever for user list
				// 103|from,to.message -- talk to other user
				String from = MainFrame.this.name;
				String to = MainFrame.this.Receiver.getSelectedItem().toString();
				String content = MainFrame.this.scannerbox.getText();
				content = ConvertExpression.sendmessage(indexs, content);
				System.out.println(content);
				try {
					dos.writeUTF("103|" + from + "," + to + "," + content);
					dos.flush();
					System.out.println("103|" + from + "," + to + "," + content + "已发送给服务器");
					MainFrame.this.chatbox.setText(
							MainFrame.this.chatbox.getText() + "To " + to + "(" + new Date() + "):" + content + "\n");
					MainFrame.this.scannerbox.setText("");
					indexs.clear();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		// 发送文件
		JButton sendfile = new JButton("");
		sendfile.setIcon(new ImageIcon(MainFrame.class.getResource("/chatclient/file.png")));
		sendfile.setBounds(191, 330, 53, 42);
		sendfile.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		sendfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser addChooser = new JFileChooser();
				String from = MainFrame.this.name;
				String to = MainFrame.this.Receiver.getSelectedItem().toString();
				addChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				// 允许选择多个文件
				addChooser.setMultiSelectionEnabled(true);
				int returnval = addChooser.showOpenDialog(MainFrame.this);
				if (returnval == JFileChooser.APPROVE_OPTION) {
					File[] files = addChooser.getSelectedFiles();
					String str = "";
					for (File file : files) {
						try {
							dos.writeUTF("104|" + from + "," + to + "," + file.getName());
							dos.flush();
							new FileSendThread(file, MainFrame.this.socket).start();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		// 截屏
		JButton screenshot = new JButton("");
		screenshot.setIcon(new ImageIcon(MainFrame.class.getResource("/chatclient/screenShot.png")));
		screenshot.setBounds(244, 330, 42, 42);
		contentPane.add(screenshot);
		screenshot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ScreenShot s = new ScreenShot();
				try {
					// Date currentTime = new Date();
					// SimpleDateFormat formatter = new
					// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					// String dateString = formatter.format(currentTime);
					// tjp.captureScreen("f:\\" + dateString.replace(" ", "") +
					// ".png");
					// Timer timer=new Timer();
					Date timer = new Date();
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);// 获取年份
					int month = ca.get(Calendar.MONTH);// 获取月份
					int day = ca.get(Calendar.DATE);// 获取日
					int minute = ca.get(Calendar.MINUTE);// 分
					int hour = ca.get(Calendar.HOUR);// 小时
					int second = ca.get(Calendar.SECOND);// 秒
					s.captureScreen("f:\\" + year + month + day + minute + hour + second + ".png");
				} catch (Exception e_2) {
					e_2.printStackTrace();
				}
			}
		});
		// 窗口抖动
		JButton jitter = new JButton("");
		jitter.setIcon(new ImageIcon(MainFrame.class.getResource("/chatclient/jitter.png")));
		jitter.setBounds(283, 330, 42, 42);
		contentPane.add(jitter);
		jitter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int num = 40;// 抖动次数
				Point point = getLocation();// 窗体位置
				for (int i = 40; i > 0; i--) {
					for (int j = num; j > 0; j--) {
						point.y += i;
						setLocation(point);
						point.x += i;
						setLocation(point);
						point.y -= i;
						setLocation(point);
						point.x -= i;
						setLocation(point);

					}
				}
			}
		});

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(442, 460, 0, 0);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 96, 629);
		panel.setBackground(Color.DARK_GRAY);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton button = new JButton("");
		button.setForeground(Color.DARK_GRAY);
		button.setBounds(15, 15, 61, 48);
		panel.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setBackground(Color.DARK_GRAY);
		button.setIcon(new ImageIcon(MainFrame.class.getResource("/chatclient/pic1.png")));

		JButton btnNewButton = new JButton("");
		btnNewButton.setBounds(15, 87, 62, 42);
		panel.add(btnNewButton);
		btnNewButton.setIcon(new ImageIcon(MainFrame.class.getResource("/chatclient/pic2.png")));

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setBounds(15, 156, 58, 42);
		panel.add(btnNewButton_1);
		btnNewButton_1.setIcon(new ImageIcon(MainFrame.class.getResource("/chatclient/pic3.png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		JLabel pic = new JLabel();
		pic.setBounds(529, 15, 281, 260);
		pic.setIcon(new ImageIcon(MainFrame.class.getResource("/chatclient/mainframe.jpg")));
		contentPane.add(pic);

		contentPane.add(lblNewLabel_1);
		scannerbox.setBounds(145, 372, 621, 190);
		contentPane.add(scannerbox);
		contentPane.add(expression);
		contentPane.add(sendfile);
		Receiver.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		Receiver.setForeground(Color.BLACK);
		Receiver.setBackground(SystemColor.control);
		Receiver.setBounds(529, 273, 281, 42);
		contentPane.add(Receiver);
		chatbox.setBounds(134, 15, 354, 300);
		contentPane.add(chatbox);
		contentPane.add(send);

		// 窗口启动事件
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				lblNewLabel_1.setText("窗口已启动");
				try {
					dos.writeUTF("102|");
					dos.flush();
					new RecieveThread(MainFrame.this, MainFrame.this.socket).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}
}
