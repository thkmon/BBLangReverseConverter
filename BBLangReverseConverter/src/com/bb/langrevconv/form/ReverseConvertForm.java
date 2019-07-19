package com.bb.langrevconv.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.bb.filefixer.data.text.StringList;
import com.bb.filefixer.util.str.FileUtil;
import com.bb.filefixer.util.str.PropertiesUtil;
import com.bb.filefixer.util.str.StringUtil;
import com.bb.langrevconv.common.CommonConst;
import com.bb.langrevconv.convert.ReverseConvertController;

public class ReverseConvertForm extends JFrame {

	
	private String defaultFolderPath = "";
	private String defaultExtension = "";
	private String defaultLangFilePath = "";
	
	
	public ReverseConvertForm() {
		
		HashMap<String, String> defaultPropertiesMap = PropertiesUtil.readPropertiesFile("default.properties");
		defaultFolderPath = "";
		defaultExtension = "";
		defaultLangFilePath = "";
		
		if (defaultPropertiesMap != null) {
			defaultFolderPath = StringUtil.emptyToDefault(defaultPropertiesMap.get("defaultFolderPath"), "C:\\");
			defaultExtension = StringUtil.emptyToDefault(defaultPropertiesMap.get("defaultExtension"), "*");
			defaultLangFilePath = StringUtil.emptyToDefault(defaultPropertiesMap.get("defaultLangFilePath"), "C:\\flow_ko_KR.txt");
			
		} else {
			defaultFolderPath = "C:\\";
			defaultExtension = "*";
			defaultLangFilePath = "C:\\flow_ko_KR.txt";
		}
		
		final String dirPath = defaultFolderPath;
				
		int textFieldHeight = 25;
		
		this.setResizable(false);
		this.setTitle("BBLangReverseConverter_" + CommonConst.VERSION);
		this.setBounds(0, 0, 560, 320);
		this.setLayout(null);
		
		JLabel label1 = new JLabel("Folder Path (File Path)");
		label1.setBounds(20, 15, 140, textFieldHeight);
		this.getContentPane().add(label1);
		
		final JTextField textField1 = new JTextField();
		textField1.setBounds(20, 40, 450, textFieldHeight);
		textField1.setText(defaultFolderPath);
		this.getContentPane().add(textField1);
		
		JButton targetFileButton = new JButton("...");
		targetFileButton.setBounds(480, 40, 40, textFieldHeight);
		this.getContentPane().add(targetFileButton);
		
		targetFileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 파일 선택 다이얼로그 정의
				JFileChooser fileDialog = new JFileChooser();

				// 폴더만 선택
				// fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				// 파일만 선택
				// fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);

				// 기본 폴더 위치 변경
				// 텍스트 필드 값을 기본 폴더 위치로 다이얼로그 열고, 여의치 않다면 기본 폴더 위치로 다이얼로그를 열자.
				File driveC = new File(textField1.getText());
				if (!driveC.exists()) {
					driveC = new File(dirPath);
				}
				
				if (driveC.exists()) {
					fileDialog.setCurrentDirectory(driveC);
				}

				// 파일 선택 다이얼로그 열기
				int returnVal = fileDialog.showOpenDialog(null);
				if (returnVal == 0) {
					// 파일 선택
					textField1.setText(fileDialog.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		final JCheckBox checkBox1 = new JCheckBox("Include Subfolders");
		checkBox1.setBounds(20, 65, 500, textFieldHeight);
		checkBox1.setSelected(true);
		this.getContentPane().add(checkBox1);
		
		JLabel label2 = new JLabel("File Extension");
		label2.setBounds(20, 95, 500, textFieldHeight);
		this.getContentPane().add(label2);
		
		final JTextField textField2 = new JTextField();
		textField2.setBounds(20, 120, 500, textFieldHeight);
		textField2.setText(defaultExtension);
		this.getContentPane().add(textField2);
		
		JLabel label3 = new JLabel("Language File Path");
		label3.setBounds(20, 155, 500, textFieldHeight);
		this.getContentPane().add(label3);
		
		final JTextField textField3 = new JTextField();
		textField3.setBounds(20, 180, 450, textFieldHeight);
		textField3.setText(defaultLangFilePath);
		this.getContentPane().add(textField3);
		
		JButton langFileButton = new JButton("...");
		langFileButton.setBounds(480, 180, 40, textFieldHeight);
		this.getContentPane().add(langFileButton);
		
		langFileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 파일 선택 다이얼로그 정의
				JFileChooser fileDialog = new JFileChooser();

				// 폴더만 선택
				// fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				// 파일만 선택
				fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);

				// 기본 폴더 위치 변경
				// 텍스트 필드 값을 기본 폴더 위치로 다이얼로그 열고, 여의치 않다면 기본 폴더 위치로 다이얼로그를 열자.
				File driveC = new File(textField3.getText());
				if (!driveC.exists()) {
					driveC = new File(dirPath);
				}
				
				if (driveC.exists()) {
					fileDialog.setCurrentDirectory(driveC);
				}

				// 파일 선택 다이얼로그 열기
				int returnVal = fileDialog.showOpenDialog(null);
				if (returnVal == 0) {
					// 파일 선택
					textField3.setText(fileDialog.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		final JCheckBox checkBox2 = new JCheckBox("Ignore Case");
		checkBox2.setBounds(20, 205, 500, textFieldHeight);
		checkBox2.setSelected(true);
		this.getContentPane().add(checkBox2);
		
		JButton button = new JButton("Reverse Convert");
		button.setBounds(20, 235, 500, 30);
		this.getContentPane().add(button);
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = textField1.getText();
				if (path == null || path.trim().length() == 0) {
					System.out.println("파일 또는 폴더 경로를 입력해주세요.");
					return;
				} else {
					path = path.trim();
				}
				
				File parentDir = new File(path);
				if (!parentDir.exists()) {
					System.out.println("존재하지 않는 경로입니다. [" + parentDir.getAbsolutePath() + "]");
					return;
				}
				
				boolean bGetSubFolders = checkBox1.isSelected();
				String extensions = textField2.getText();
				
				boolean bIgnoreCase = checkBox2.isSelected();
				String langFilePath = textField3.getText();
				if (langFilePath == null || langFilePath.length() == 0) {
					System.out.println("언어 파일 위치를 입력해주세요.");
					return;
				}
				
				System.out.println(parentDir);
				
				ReverseConvertController reverseConvertCtrl = new ReverseConvertController();
				reverseConvertCtrl.reverseConvert(parentDir, bGetSubFolders, extensions, langFilePath, bIgnoreCase);
			}
		});
		
		this.setVisible(true);
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				String str1 = "defaultFolderPath=" + defaultFolderPath;
				String str2 = "defaultExtension=" + defaultExtension;
				String str3= "defaultLangFilePath=" + defaultLangFilePath;
				
				String dirPath = textField1.getText();
				if (dirPath != null && dirPath.length() > 0) {
					File dir = new File(dirPath);
					if (dir.exists()) {
						str1 = "defaultFolderPath=" + dir.getAbsolutePath();
					}
				}
				
				String ext = textField2.getText();
				if (ext != null && ext.trim().length() > 0) {
					str2 = "defaultExtension=" + ext.trim();
				} else {
					str2 = "defaultExtension=*";
				}
				
				String langFilePath = textField3.getText();
				if (langFilePath != null && langFilePath.length() > 0) {
					File langFile = new File(langFilePath);
					if (langFile.exists() && langFile.isFile()) {
						str3 = "defaultLangFilePath=" + langFile.getAbsolutePath();
					}
				}
				
				StringList strList = new StringList();
				strList.add(str1);
				strList.add(str2);
				strList.add(str3);
				
				FileUtil.writeFile("default.properties", strList, false);
				
				System.out.println("사용자 명령으로 종료합니다.");
				System.exit(0);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
	}
}
