package com.zgx.http.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class WebAppClassLoader extends URLClassLoader {	
	// System class loader, or so called application class loader.
	private ClassLoader system;
	// The working directory of this class loader. Recources and classes
	// are loaded from here.
	private String workDir;
	// Loaded class cache
	private class Entry{
		String name;
		Class loadedClass;
		long modifiedDate;
		String path;
	}
	private Map<String, Entry> cache;
	
	public WebAppClassLoader(String workDir) {
        super(new URL[0]);
        this.workDir = workDir;
        this.system = this.getSystemClassLoader();
        this.cache = new HashMap<String, Entry>();
    }
	
	@Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> clazz = null;

		// (0) Check our previously loaded local class cache
        clazz = findLoadedClass0(name);
        if (clazz != null) {
            return (clazz);
        }

        // (0.1) Check our previously loaded class cache
        clazz = findLoadedClass(name);
        if (clazz != null) {
            return (clazz);
        }
        
        // (0.2) Try loading the class with the system class loader, to prevent
        //       the webapp from overriding J2SE classes
        try {
            clazz = system.loadClass(name);
            if (clazz != null) {
                return (clazz);
            }
        } catch (ClassNotFoundException e) {
            // Ignore
        }
        
        // (1) Search local repositories
        clazz = findClass(name);
        
        return clazz;
    }
	
	// search all classes under <workDir>/classes
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> clazz;
		// search classes first.
		String path = this.workDir+"classes/"+name.replace('.', '/')+".class";
		File file = new File(path);
		if (file.exists()) {
			ByteBuffer buffer = ByteBuffer.allocate((int)file.length()+1000);
			try {
				new FileInputStream(file).getChannel().read(buffer);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			buffer.flip();
			
			clazz = this.defineClass(name, buffer.array(), 0, (int)file.length());
			Entry entry = new Entry();
			entry.loadedClass = clazz;
			entry.name = name;
			entry.modifiedDate = file.lastModified();
			entry.path = file.getAbsolutePath();
			// put it into cache.
			this.cache.put(name,  entry);
			return clazz;
		}
		
		throw new ClassNotFoundException();
	}
	
	protected Class<?> findLoadedClass0(String name) {
        Entry entry = cache.get(name);
        if (entry != null && !this.isModified(entry)) {
            return entry.loadedClass;
        }
        return (null); 
    }

	private boolean isModified(Entry entry) {
		if (new File(entry.path).lastModified() != entry.modifiedDate) return true;
		return false;
	}
	
}