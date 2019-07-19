package com.bb.filefixer.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.bb.filefixer.data.file.BBFileContent;

public class BBFileReadUtil {
	
	
	public static BBFileContent readFile(String filePath, String encode) throws Exception {
		if (filePath == null || filePath.length() == 0) {
			return null;
		}
		
		return readFile(new File(filePath), encode);
	}
	
	
	public static BBFileContent readFile(File file, String encode) throws Exception {
		if (file == null || !file.exists()) {
			return null;
		}
		
		if (encode == null || encode.length() == 0) {
			encode = "UTF-8";
		}

		BBFileContent fileContent = new BBFileContent(file);

		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream, encode);
			bufferedReader = new BufferedReader(inputStreamReader);

			String oneLine = null;
			while ((oneLine = bufferedReader.readLine()) != null) {
				fileContent.add(oneLine);
			}
			
		} catch (IOException e) {
			throw e;

		} catch (Exception e) {
			throw e;

		} finally {
			close(bufferedReader);
			close(inputStreamReader);
			close(fileInputStream);
		}

		return fileContent;
	}
	
	
	public static void close(FileInputStream obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {
			
		} finally {
			obj = null;
		}
	}
	
	
	public static void close(InputStreamReader obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {
			
		} finally {
			obj = null;
		}
	}
	
	
	public static void close(BufferedReader obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {
			
		} finally {
			obj = null;
		}
	}
	
	
	/**
	 * 특정 파일의 엔터키 값(리턴값)을 찾아서 String 형태로 리턴한다.
	 * 첫 번째로 발견한 엔터키 값을 리턴한다.
	 * @param file
	 * @return
	 */
	public String getReturnCode(File file) {
		String returnCode = "\n";
		
		FileReader ioFileReader = null;
		
		try {
			
			// 파일 밸리드 체크
			boolean validFile = checkFileValidToRead(file);
			if (!validFile) {
				return null;
			}
			
			// 엔터값(캐리지 리턴값) 알아내기
			ioFileReader = new FileReader(file);
			
			int preInt = -1;
			int oneInt = -1;
			while ((oneInt = ioFileReader.read()) > -1) {
				
				if (oneInt == 10) {
					if (preInt == 13) {
						// rn 발견
						returnCode = "\r\n";
						
					} else {
						// n 발견
						returnCode = "\n";
					}
					
					// 엔터 한 번이라도 발견하면 빠져나감
					break;
				}
				preInt = oneInt;
			}
			
			// 객체 닫기
			if (ioFileReader != null) {
				ioFileReader.close();
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			// 객체 닫기
			close(ioFileReader);
		}
		
		return returnCode;
	}
	
	
	/**
	 * 파일 밸리드 체크
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public boolean checkFileValidToRead(File file) throws Exception {
		
		if (file == null) {
			System.out.println("파일 객체가 존재하지 않습니다.");
			return false;
		}
		
		if (!file.exists()) {
			System.out.println("파일이 존재하지 않습니다. (" + file.getAbsolutePath() + ")");
			return false;
		}
		
		if (!file.isFile()) {
			System.out.println("파일이 디렉토리이거나 손상되었습니다. (" + file.getAbsolutePath() + ")");
			return false;
		}
		
		if (!file.canRead()) {
			System.out.println("파일을 읽을 수 없는 상태입니다. (" + file.getAbsolutePath() + ")");
			return false;
		}
		
		return true;
	}
}