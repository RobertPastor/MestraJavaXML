package test;

import java.io.File;
import java.io.IOException;

public class WindowsDrive {

	/**
	 * @param args
	 */
	public static void main(String[] args) {


		File[] roots = File.listRoots();
		for(int i = 0 ; i < roots.length ; i++) {
			File f = roots[i];
			String name = f.toString();
			System.out.println(name);
			File mestraToolDir = new File(name+"Mestra Tool");
			if (mestraToolDir.exists()) {
				System.out.println("Aboslute path: "+mestraToolDir.getAbsolutePath()+".....exists!!");
				try {
					System.out.println("Canonical path: "+mestraToolDir.getCanonicalPath()+".....exists!!");
				}
				catch (IOException e) {
					System.out.println("Canonical path: IO Exception "+mestraToolDir.getAbsolutePath()+".....error!! "+e.getMessage());

				}

			}
		}
	}
}
