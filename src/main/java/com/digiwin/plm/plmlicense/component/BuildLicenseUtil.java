package com.digiwin.plm.plmlicense.component;

import Rijndael.Rijndael_Algorithm;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BuildLicenseUtil {

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	public File genericLicenseFile(String licFilePath, ModuleVo module) throws IOException {
		// 读取licence.properties文件
		Properties properties = readLicenseFile();
		List<ModuleVo> listModuleVo = conversionToListModule(properties, module);
		// 对listModule进行加密
		encryption(listModuleVo);
		// 构建licenseFile
		return buildLicenseFile(licFilePath, listModuleVo);

	}

	/**
	 * 构建licenseFile
	 * 
	 * @param listModuleVo
	 * @throws IOException
	 */
	private File buildLicenseFile(String licFilePath, List<ModuleVo> listModuleVo) throws IOException {

		File file = new File(licFilePath);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("创建文件失败");
			}
		} 
        // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
		try (FileWriter writer = new FileWriter(file, true)){

			for(ModuleVo moduleVo:listModuleVo){
	        	writer.write(moduleVo.getType()+"\r\n");  
	        	writer.write(moduleVo.getProduct()+"\r\n");  
	        	writer.write(moduleVo.getModule()+"\r\n");  
	        	writer.write(moduleVo.getVersion()+"\r\n");  
	        	writer.write(moduleVo.getCipher()+"\r\n");  
	        }
			writer.flush();
		}

		return file;
		

	}

	/**
	 * 将license.properties文件转换成List<ModuleVo>对象
	 * 
	 * @return
	 */
	private List<ModuleVo> conversionToListModule(Properties properties, ModuleVo module) {

		String[] listProduct = properties.getProperty("license.product").split(",");
		List<ModuleVo> listModules = new ArrayList<ModuleVo>();

		for (String product : listProduct) {
			String productt = properties.getProperty("license." + product + ".product");
			String modules = properties.getProperty("license." + product + ".module");
			String version = properties.getProperty("license." + product + ".version");
			String type = properties.getProperty("license." + product + ".type");
			ModuleVo moduleVo = new ModuleVo(module.getStartDate(),module.getEndDate());
			moduleVo.setMac(module.getMac());
			moduleVo.setUsers(module.getUsers());
			moduleVo.setProduct(productt);
			moduleVo.setModule(modules);
			moduleVo.setVersion(version);
			moduleVo.setType(type);

			listModules.add(moduleVo);
		}

		return listModules;
	}

	/**
	 * 计算出所有模块对应的密文
	 * 
	 * @param listModules
	 * @return
	 */
	private void encryption(List<ModuleVo> listModules) {

		// 进行循环对模块加密
		for (ModuleVo moduleVo : listModules) {
			String key = moduleVo.getType() + moduleVo.getMac() + moduleVo.getProduct() + moduleVo.getModule()
					+ moduleVo.getVersion();
			String cipher = getFrontSecret(key) + getbehindSecret(moduleVo, key);
			moduleVo.setCipher(cipher);
		}

	}

	/**
	 * 这是取得前半部分加密
	 *
	 * @param key
	 * @return
	 */
	private String getFrontSecret(String key) {
		byte[] kb = new byte[16];
		HexStr2CharStr(key, kb, 15);

		long sum = 0L;
		for (int j = 0; j < 15; ++j) {
			sum += kb[j];
		}
		kb[15] = (byte) (int) (sum % 256L);
		Object obj = getSessionKey(key);
		byte[] ctd = Rijndael_Algorithm.blockEncrypt(kb, 0, obj, 16);

		return toString(ctd);
	}

	/**
	 * 取得后半部分加密
	 * 
	 * @param moduleVo
	 * @param keys
	 * @return
	 */
	private String getbehindSecret(ModuleVo moduleVo, String keys) {
		byte[] kb = new byte[16];
		String key = moduleVo.getUsers() + moduleVo.getStartDate() + moduleVo.getEndDate() + "3131000000";
		HexStr2CharStr(key, kb, 15);

		long sum = 0L;
		for (int j = 0; j < 15; ++j) {
			sum += kb[j];
		}
		kb[15] = (byte) (int) (sum % 256L);
		Object obj = getSessionKey(keys);
		//System.out.println(toString(kb));
		byte[] ctd = Rijndael_Algorithm.blockEncrypt(kb, 0, obj, 16);
		return toString(ctd);
	}

	/**
	 * 将加密后的byte数组转换成对应的16进制字符串
	 * 
	 * @param ba
	 * @return
	 */
	private static String toString(byte[] ba) {
		int length = ba.length;
		char[] buf = new char[length * 2];

		int i = 0;
		for (int j = 0; i < length;) {
			int k = ba[(i++)];
			buf[(j++)] = HEX_DIGITS[(k >>> 4 & 0xF)];
			buf[(j++)] = HEX_DIGITS[(k & 0xF)];
		}
		return new String(buf);
	}

	/**
	 * 取得对应得加密密钥
	 * 
	 * @param key
	 * @return
	 */
	private Object getSessionKey(String key) {

		byte[] kb = new byte[16];
		HexStr2CharStr(key, kb, 15);

		long sum = 0L;
		for (int j = 0; j < 15; ++j) {
			sum += kb[j];
		}
		kb[15] = (byte) (int) (sum % 256L);

		Object sessionKey = null;
		try {
			sessionKey = Rijndael_Algorithm.makeKey(kb, 16);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return sessionKey;
	}

	/**
	 * 读取对应的license.properties文件
	 *
	 * @return
	 */
	private Properties readLicenseFile() throws IOException {
		Properties properties = new Properties();
		try (InputStream inputStream = new ClassPathResource("License.properties").getInputStream()){
			properties.load(inputStream);
		}
		return properties;
	}

	private static void HexStr2CharStr(String HexStr, byte[] CharStr, int iSize) {
		byte ch = 0;

		for (int i = 0; i < iSize; ++i) {
			ch = Hex2Char(HexStr, i * 2);
			CharStr[i] = ch;
		}
	}

	private static byte Hex2Char(String HexStr, int index) {
		byte rch = 0;

		for (int i = 0; i < 2; ++i) {
			if ((HexStr.charAt(index + i) >= '0') && (HexStr.charAt(index + i) <= '9')) {
				rch = (byte) ((rch << 4) + HexStr.charAt(index + i) - '0');
			} else if ((HexStr.charAt(index + i) >= 'A') && (HexStr.charAt(index + i) <= 'F')) {
				rch = (byte) ((rch << 4) + HexStr.charAt(index + i) - 'A' + 10);
			} else {
				return rch;
			}
		}
		return rch;
	}

}
