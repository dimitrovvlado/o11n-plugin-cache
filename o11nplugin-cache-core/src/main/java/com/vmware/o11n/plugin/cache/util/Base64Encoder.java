package com.vmware.o11n.plugin.cache.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class Base64Encoder {
	
	
	public String encodeBase64(String fileName) {
		try {
			File reader = new File(fileName);
			FileInputStream fis = new FileInputStream(reader);
			byte[] bbuf = new byte[fis.available()];
			fis.read(bbuf);
			fis.close();
			return Base64.getEncoder().encodeToString(bbuf);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
