/*
 * Prog for extracting values from a CSV string.
 */

package ssmith.util;
import java.util.*;

public class CSVString {
	
	//private String separator;
	private String str;
	private ArrayList elements;

	public CSVString(String newStr) {
		this(newStr, ",");
	}

	public CSVString(String newStr, String sep) {
		this.str = newStr;
		//this.separator = sep;
		this.elements = new ArrayList();
		StringTokenizer strtkn = new StringTokenizer(newStr, sep);
		while (strtkn.hasMoreElements()) {
			elements.add(strtkn.nextToken());
		}
	}
	
	public int getNoOfSections() {
		return elements.size();
	}

/*	public void setString(String str) {
		this.str = str;
	}
*/
	public String getString() {
		return this.str;
	}

	public String toString() {
		return this.str;
	}

	public String getFirstSection() {
		return getSection(1);
/*		String ret = new String(str);
		int x;
		for(x=0 ; x < str.length() ; x++) {
			if (str.substring(x,x+1).equalsIgnoreCase(separator) == true) {
				ret = str.substring(0, x);
				break;
			}
		}
		return ret;*/
	}
/*
	public String getLastSection() {
		return (String) elements.get(elements.size()-1);
		String ret = new String(str);
		int x;
		for(x=0 ; x < str.length()-1 ; x++) {
			if (str.substring(x,x+1).equalsIgnoreCase(separator) == true) {
				ret = str.substring(x+1, str.length());
			}
		}
		return ret;
	}
*/
	public String getSection(int section) {
		//System.out.println("getting section"+section);
		return (String) elements.get(section-1);
/*	if (section == 1) {
			return getFirstSection();
		} else {
			String ret = new String("null");
			int sec = 1;
			int x;
			for(x=0 ; x < str.length()-1 ; x++) {
				if (str.substring(x,x+1).equalsIgnoreCase(separator) == true) {
					sec++;
					if (sec >= section) {
						// Now find the next separator
						int y;
						for(y=x+1 ; y < str.length() ; y++) {
							if (str.substring(y,y+1).equalsIgnoreCase(separator) == true) {
								break;
							}
						}
						ret = str.substring(x+1, y);
						break;
					}
				}
			}
			return ret;
		}*/
	}

	public static void main(String args[]) {
		System.out.println(new ssmith.util.CSVString("thix#hello#me","#").getSection(2));
	}

}
