package org.mill.data;

/**
 * 字符串匹配
 * 
 * 
 */
public class StringMatchPattern {

	public static void main(String[] args) {
		String source = "goodgoogle";
		String target = "google";
		int targetIndex = -1;
		for (int i = 0; i < source.length(); i++) {
			int remainLength = source.length() - i;
			if (remainLength < target.length()) {
				System.out.println("没有找到匹配的字符串");
				break;
			}
			char sourceFirst = source.charAt(i);
			char targetFirst = target.charAt(0);
			if (sourceFirst == targetFirst) {
				// 比较余下的
				boolean find = true;
				for (int j = 1; j < target.length(); j++) {
					if (source.charAt(i + j) == target.charAt(j)) {

					} else {
						find = false;
						break;
					}
				}
				if (find) {
					targetIndex = i;
					break;
				}
			}
		}
		System.out.println("target index:" + targetIndex);
	}
}
