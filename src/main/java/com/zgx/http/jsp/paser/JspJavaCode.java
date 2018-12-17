package com.zgx.http.jsp.paser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.zgx.http.servlet.engine.ServletContextManager;
import com.zgx.tools.FileTools;
import com.zgx.tools.PathTools;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class JspJavaCode {
	private List<String> importSpace = new ArrayList<String>();
	private List<String> staticSpace = new ArrayList<String>();
	private List<String> statementSpace = new ArrayList<String>();
	private List<String> codeSpace = new ArrayList<String>();

	public JspJavaCode() {
		// TODO Auto-generated constructor stub
		// 初始化的import区
		importSpace.add("package com.zgx.jsp;\r\n");
		importSpace.add("import javax.servlet.*;");
		importSpace.add("import javax.servlet.http.*;");
		importSpace.add("import javax.servlet.jsp.*;");
		importSpace.add("import java.io.*;");

		// 初始化的static区

		// 初始化code区
		codeSpace.add("PrintWriter out = resp.getWriter();");
		codeSpace.add("ServletContext application = this.getServletContext();");
		codeSpace.add("HttpSession session=req.getSession();");
		codeSpace.add("HttpServletRequest request =req;");
		codeSpace.add("HttpServletResponse response = resp;");
		codeSpace.add("HttpServlet page = this;");
		codeSpace.add("ServletConfig config = this.getServletConfig();");

	}

	public void addImportSpace(String importStr) {
		this.importSpace.add(importStr);
	}

	public void addStaticSpace(String staticStr) {
		this.importSpace.add(staticStr);
	}

	public void addStatementSpace(String statement) {
		this.statementSpace.add(statement);
	}

	public void addCodeSpace(String codeStr) {
		this.importSpace.add(codeStr);
	}

	public List<String> getImportSpace() {
		return importSpace;
	}

	public void setImportSpace(List<String> importSpace) {
		this.importSpace = importSpace;
	}

	public List<String> getStaticSpace() {
		return staticSpace;
	}

	public void setStaticSpace(List<String> staticSpace) {
		this.staticSpace = staticSpace;
	}

	public List<String> getStatementSpace() {
		return statementSpace;
	}

	public void setStatementSpace(List<String> statementSpace) {
		this.statementSpace = statementSpace;
	}

	public List<String> getCodeSpace() {
		return codeSpace;
	}

	public void setCodeSpace(List<String> codeSpace) {
		this.codeSpace = codeSpace;
	}

	public void outTojavaServletFile(String webappUri, String jsp_servlet_name) {
		String javaSourceName = "/com/zgx/jsp/" + jsp_servlet_name.replaceAll("\\.", "_") + ".java";
		// System.out.println(javaSourceName);
		String fileName = Class.class.getClass().getResource("/").getPath() + "work" + webappUri + "/" + javaSourceName;
		FileTools.exitsOrCreateFile(fileName);
		ByteBuf byteBuf = Unpooled.buffer();
		for (int i = 0; i < this.importSpace.size(); i++) {
			byteBuf.writeBytes((this.importSpace.get(i) + "\r\n").getBytes());
		}
		String servletClassStatement = "public class " + jsp_servlet_name.replaceAll("\\.", "_")
				+ " extends HttpServlet{\r\n";
		byteBuf.writeBytes(servletClassStatement.getBytes());
		for (int i = 0; i < this.staticSpace.size(); i++) {
			byteBuf.writeBytes(("public static" + staticSpace.get(i) + "\r\n").getBytes());
		}

		for (int i = 0; i < this.statementSpace.size(); i++) {
			byteBuf.writeBytes(("	" + this.statementSpace.get(i) + "\r\n").getBytes());
		}

		String methodService = "	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {\r\n";
		byteBuf.writeBytes(methodService.getBytes());
		for (int i = 0; i < this.codeSpace.size(); i++) {
			byteBuf.writeBytes(("		" + this.codeSpace.get(i) + "\r\n").getBytes());
			// System.out.println(new String((" " + this.codeSpace.get(i) +
			// "\r\n").getBytes()));
		}
		String endServlet = "\r\n" + "	}\r\n" + "}";
		byteBuf.writeBytes(endServlet.getBytes());
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		FileTools.overrideFileByBytes(bytes, fileName);

		// 编译java文件
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		// 获取java文件管理类
		StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
		// 获取java文件对象迭代器
		Iterable<? extends JavaFileObject> it = manager.getJavaFileObjects(new String[] { fileName });
		// 设置编译参数
		ArrayList<String> ops = new ArrayList<String>();
		ops.add("-Xlint:unchecked");
		// 设置classpath
		ops.add("-classpath");
		ServletContext sc = ServletContextManager.getContext(webappUri);
		String webappClassPath = sc.getRealPath("") + "/WEB-INF/classes";
		String webappLibPath = sc.getRealPath("") + "/WEB-INF/lib/*";
		String classPath = PathTools.getClassPath() + ";" + webappClassPath + ";" + webappLibPath;
		ops.add(classPath);
		// System.out.println(classPath);
		// 获取编译任务
		JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, ops, null, it);
		// 执行编译任务
		System.out.println("JspJavaCode.outTojavaServletFile--complile"+fileName);
		task.call();
	}

	// 测试
	public static void main(String[] args) {
		JspJavaCode jspJavaCode = new JspJavaCode();
		jspJavaCode.getCodeSpace().add("out.println(\"this is a jsp\");");
		jspJavaCode.getCodeSpace().add("System.out.println(\"jsp running\");");
		jspJavaCode.getStatementSpace().add("int a = 0;");
		jspJavaCode.outTojavaServletFile("/ROOT", "test.jsp");

	}
}
