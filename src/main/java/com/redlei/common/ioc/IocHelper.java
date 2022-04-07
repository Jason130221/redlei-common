package com.redlei.common.ioc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.stereotype.Service;

public class IocHelper {
	/**
	 * 根据包名获取包下面所有的类名
	 *
	 * @param pack
	 * @return
	 * @throws Exception
	 */
	public static Set<Class<?>> getClasses(String pack) throws Exception {

		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		boolean recursive = true;
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
					JarFile jar;
					try {
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						Enumeration<JarEntry> entries = jar.entries();
						findClassesInPackageByJar(packageName, entries, packageDirName, recursive, classes);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;

	}

	/**
	 * 以jar的形式来获取包下的所有Class
	 *
	 * @param packageName
	 * @param entries
	 * @param packageDirName
	 * @param recursive
	 * @param classes
	 */
	private static void findClassesInPackageByJar(String packageName, Enumeration<JarEntry> entries,
			String packageDirName, final boolean recursive, Set<Class<?>> classes) {
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			if (name.charAt(0) == '/') {
				name = name.substring(1);
			}
			if (name.startsWith(packageDirName)) {
				int idx = name.lastIndexOf('/');
				if (idx != -1) {
					packageName = name.substring(0, idx).replace('/', '.');
				}
				if ((idx != -1) || recursive) {
					if (name.endsWith(".class") && !entry.isDirectory()) {
						String className = name.substring(packageName.length() + 1, name.length() - 6);
						try {
							Class<?> clzz = Class.forName(packageName + '.' + className);
							if (isService(clzz)) {
								classes.add(clzz);
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 *
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	private static void findClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
			Set<Class<?>> classes) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] dirfiles = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				findClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
						classes);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					Class<?> clzz = Thread.currentThread().getContextClassLoader()
							.loadClass(packageName + '.' + className);
					if (isService(clzz)) {
						classes.add(clzz);
					}

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static Boolean isService(Class<?> clzz) {
		if (clzz == null) {
			return false;
		}
		Service service = (Service) clzz.getAnnotation(Service.class);
		if (service == null) {
			return false;
		}
		return true;
	}
}
