package com.plm;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main {

	private JFrame frmPlm;
	private JTextField startDate;
	private JTextField endDate;
	private JTextField mac;
	private JTextField user;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmPlm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPlm = new JFrame();
		frmPlm.setTitle("PLM-進攻學歷交流");
		frmPlm.setBounds(100, 100, 450, 300);
		frmPlm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPlm.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("開始時間");
		lblNewLabel.setBounds(43, 13, 58, 30);
		frmPlm.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("結束時間");
		lblNewLabel_1.setBounds(43, 67, 54, 15);
		frmPlm.getContentPane().add(lblNewLabel_1);
		
		JLabel lblmac = new JLabel("服務器MAC");
		lblmac.setBounds(43, 110, 54, 15);
		frmPlm.getContentPane().add(lblmac);
		
		JLabel label = new JLabel("用戶數");
		label.setBounds(43, 158, 54, 15);
		frmPlm.getContentPane().add(label);
		
		startDate = new JTextField();
		startDate.setBounds(111, 15, 260, 21);
		frmPlm.getContentPane().add(startDate);
		startDate.setColumns(10);
		
		endDate = new JTextField();
		endDate.setColumns(10);
		endDate.setBounds(111, 63, 260, 21);
		frmPlm.getContentPane().add(endDate);
		
		mac = new JTextField();
		mac.setColumns(10);
		mac.setBounds(111, 105, 260, 21);
		frmPlm.getContentPane().add(mac);
		
		user = new JTextField();
		user.setColumns(10);
		user.setBounds(111, 157, 260, 21);
		frmPlm.getContentPane().add(user);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setBounds(423, 155, 1, 1);
		frmPlm.getContentPane().add(horizontalGlue);

		JTextArea result = new JTextArea();
		result.setBounds(411, 221, -367, 40);
		frmPlm.getContentPane().add(result);
		
		JButton button = new JButton("生成授權");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String startDateStr = startDate.getText();
				String endDateStr = endDate.getText();
				String macStr = mac.getText();
				String userStr = user.getText();
				
				BuildLicenseUtil buildLicenseUtil = new BuildLicenseUtil();
				ModuleVo module = new ModuleVo();
				module.setMac(macStr);
				module.setStartDate(startDateStr);
				module.setEndDate(endDateStr);
				module.setUsers(userStr);
				
				File directory = new File("");//设定为当前文件夹
				String fileName = directory.getAbsolutePath()+File.separator+"src"+File.separator+"resources"+File.separator+"License.properties";//获取绝对路径
				
				buildLicenseUtil.genericLicenseFile(fileName, module);

				result.setText("生成授權文件成功，請到C盤根目錄下取得License.lic和License.properties");
				
				//System.out.print("輸入的數據為："+startDateStr+"---->"+endDateStr+"---->"+macStr+"---->"+userStr);
			}
		});
		button.setBounds(97, 188, 234, 23);
		frmPlm.getContentPane().add(button);
		
		
	}
	
}
