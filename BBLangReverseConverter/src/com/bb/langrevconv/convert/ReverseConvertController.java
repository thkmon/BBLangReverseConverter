package com.bb.langrevconv.convert;

import java.io.File;
import java.util.logging.Logger;

import com.bb.filefixer.data.file.BBFileContent;
import com.bb.filefixer.data.lang.PropFile;
import com.bb.filefixer.data.text.StringList;
import com.bb.filefixer.log.FileLogManager;
import com.bb.filefixer.util.file.BBFileReadUtil;
import com.bb.filefixer.util.file.BBFileWriteUtil;
import com.bb.filefixer.util.str.FileUtil;
import com.bb.filefixer.util.str.StringUtil;

public class ReverseConvertController {
	
	
	public void reverseConvert(File parentDir, boolean bGetSubFolders, String extensions, String langFilePath, boolean bIgnoreCase) {
		if (parentDir == null) {
			return;
		}
		
		if (!parentDir.exists()) {
			return;
		}
		
		FileLogManager logger = null;
		
		try {
			logger = new FileLogManager("log");
			
			logger.debug("언어파일 경로 : " + langFilePath);
			PropFile langKoFile = new PropFile(langFilePath, "UTF-8");
			if (!langKoFile.isbSuccess()) {
				if (new File(langFilePath).exists()) {
					logger.debug("언어파일을 객체화할 수 없습니다.");
				} else {
					logger.debug("언어파일이 존재하지 않습니다.");
				}
				return;
			}
			
			logger.debug("시작");
			if (parentDir.isDirectory()) {
				logger.debug("대상 폴더 : " + parentDir.getAbsolutePath());
				logger.debug("하위 폴더 검색여부 : " + (bGetSubFolders ? "YES" : "NO"));
				
			} else if (parentDir.isFile()) {
				logger.debug("대상 파일 : " + parentDir.getAbsolutePath());
			}
			
			StringList extensionList = null;
			if (extensions != null && extensions.trim().length() > 0 && !extensions.equals("*")) {
				extensionList = StringUtil.splitWithTrim(extensions, ",");
			}
			
			logger.debug("대상 확장자 : " + (extensionList == null ? "*" : extensionList));
			
			StringList pathList = new StringList();
			addChildrenPaths(pathList, parentDir, bGetSubFolders, extensionList);
			
			if (pathList == null || pathList.size() == 0) {
				logger.debug("대상 파일 없음");
				return;
			}
			
			int fileCount = pathList.size();
			logger.debug("대상 파일 개수 : " + fileCount);
			// logger.debug("대상 파일 패스 : " + pathList);
			
			int modifiedCount = 0;
			int percent = 0;
			System.out.println("진행율 : 0%");
			
			String onePath = "";
			
			for (int i=0; i<fileCount; i++) {
				onePath = pathList.get(i);
				
				float pp = (float) (i+1) / fileCount * 100;
				if (Math.round(pp) > percent) {
					percent = Math.round(pp);
					System.out.println("진행율 : " + percent + "%");
				}
				
				try {
					boolean bModified = fixOneFile(langKoFile, onePath);
					if (bModified) {
						modifiedCount++;
						logger.debug("파일 수정함 : " + onePath);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			
			logger.debug("총 " + fileCount + " 개 중 " + modifiedCount + " 개 파일 수정됨");
			logger.debug("끝");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			logger.closeLogFile();
		}
	}
	
	
	public void addChildrenPaths(StringList listToAdd, File parentDir, boolean bGetSubFolders, StringList extensionList) {
		if (listToAdd == null) {
			return;
		}
		
		if (!parentDir.exists()) {
			return;
		}

		// 파일이면 경로 추가
		if (parentDir.isFile()) {
			String onePath = parentDir.getAbsolutePath();
			String oneExt = FileUtil.getFileExtension(onePath);
			
			if (extensionList == null || extensionList.findIgnoreCase(oneExt)) {
				listToAdd.add(onePath);
			}
			return;
		}
		
		if (!parentDir.isDirectory()) {
			return;
		}
		
		File[] fileArr = parentDir.listFiles();
		if (fileArr == null || fileArr.length == 0) {
			return;
		}
		
		String onePath = "";
		String oneExt = "";
		File oneFile = null;
		int count = fileArr.length;
		for (int i=0; i<count; i++) {
			oneFile = fileArr[i];
			if (!oneFile.exists()) {
				continue;
			}

			if (oneFile.isFile()) {
				onePath = oneFile.getAbsolutePath();
				if (onePath == null || onePath.length() == 0) {
					continue;
				}
				
				oneExt = FileUtil.getFileExtension(onePath);
				
				if (extensionList == null || extensionList.findIgnoreCase(oneExt)) {
					listToAdd.add(onePath);
				}
				
			} else if (oneFile.isDirectory()) {
				if (bGetSubFolders) {
					addChildrenPaths(listToAdd, oneFile, bGetSubFolders, extensionList);
				}
			}
		}
	}
	
	
	private boolean fixOneFile(PropFile langKoFile, String filePath) throws Exception {
		
		boolean bModified = false;
		
		File oneFile = new File(filePath);
		BBFileContent oneFileContent = BBFileReadUtil.readFile(oneFile, "UTF-8");
		if (oneFileContent == null || oneFileContent.size() == 0) {
			return false;
		}
		
		String extension = FileUtil.getFileExtension(filePath);
		
		// logger.debug("라인수 : " + oneFileContent.size());
		String oneLine = "";
		int fileLineCount = oneFileContent.size();
		for (int i=0; i<fileLineCount; i++) {
			oneLine = oneFileContent.get(i);
			if (oneLine == null || oneLine.length() == 0) {
				continue;
			}
			
			if (checkIsValidExtension(extension)) {
				if (oneLine.indexOf("XFLocale.getString") < 0) {
					continue;
				}
				
				// 예를 들면, XFLocale.getString("lang.flow", "MC1203") 를 "문자열" 로 변경
				int idxForSearch = 0;
				
				while (true) {
					int idx1 = oneLine.indexOf("XFLocale.getString", idxForSearch);
					if (idx1 < 0) {
						break;
					}
					
					int idx2 = oneLine.indexOf("(", idx1 + 1);
					int idx3 = oneLine.indexOf("\"lang.flow\"", idx2 + 1);
					int idx4 = oneLine.indexOf(",", idx3 + 1);
					int idx5 = oneLine.indexOf("\"", idx4 + 1);
					int idx6 = oneLine.indexOf("\"", idx5 + 1);
					int idx7 = oneLine.indexOf(")", idx6 + 1);
					if (idx1 > -1 && idx2 > -1 && idx3 > -1 && idx4 > -1 && idx5 > -1 && idx6 > -1 && idx7 > -1) {
						// logger.debug("[AS-IS] " + oneLine);
						String code = oneLine.substring(idx5 + 1, idx6);
						if (code == null || code.length() == 0) {
							// 코드값(ex : XX0000)이 없는 경우 다음 XFLocale 을 찾는다.
							idxForSearch = idx7 + 1;
							continue;
						}
						
						String value = langKoFile.getKeyMap().get(code);
						if (value == null || value.length() == 0) {
							// 밸류값(ex : "결재")이 없는 경우 다음 XFLocale 을 찾는다.
							idxForSearch = idx7 + 1;
							continue;
						}
						
						oneLine = oneLine.substring(0, idx1) + "\"" + value + "\"" + oneLine.substring(idx7 + 1);
						// logger.debug("[TO-BE] " + oneLine);
						oneFileContent.set(i, oneLine);
						
						// 수정함
						if (!bModified) {
							bModified = true;
						}
						
						// idx1 : 역변환 성공한 문자열의 여는 따옴표
						// + value.length() : 역변환 성공한 문자열의 길이
						// + 1 : 닫는 따옴표
						int idxLastDoubleQuote = idx1 + value.length() + 1;
						
						// 닫는 따옴표 다음부터 XFLocale.getString 문자열이 존재하는지 찾는다. 한 줄에 XFLocale.getString 2개 이상 있는 경우 계속 진행하기 위함.
						idxForSearch = idxLastDoubleQuote + 1;
						
					} else {
						// 비정상적인 XFLocale.getString 이 발견되었을 경우 그만둔다.
						break;
					}
				}
			}
		}
		
		if (bModified) {
			BBFileWriteUtil.writeFile(filePath, oneFileContent, "UTF-8", false);
		}
		
		return bModified;
	}
	
	
	// java 외의 확장자도 지원하도록 개선
	private boolean checkIsValidExtension(String extension) {
		if (extension == null || extension.length() == 0) {
			return false;
		}
		
		if (extension.equalsIgnoreCase("java") ||
			extension.equalsIgnoreCase("jsp") ||
			extension.equalsIgnoreCase("js")) {
			return true;
		}
		
		return false;
	}
}