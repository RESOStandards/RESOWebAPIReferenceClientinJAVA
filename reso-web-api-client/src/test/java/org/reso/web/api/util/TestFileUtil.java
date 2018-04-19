package org.reso.web.api.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestFileUtil {

	public static String readFile(String fileName) {
		try {
			Path path = Paths.get(TestFileUtil.class.getClassLoader().getResource(fileName).toURI());        
		    StringBuilder data = new StringBuilder();
		    Stream<String> lines = Files.lines(path);
		    lines.forEach(line -> data.append(line).append("\n"));
		    lines.close();
		    return data.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
