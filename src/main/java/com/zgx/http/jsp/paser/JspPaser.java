package com.zgx.http.jsp.paser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.zgx.catserver.connector.SimpleInputStream;
import com.zgx.tools.StringTools;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class JspPaser {

	public static boolean paserJspTojava(String jspPath, String webappUri, String jsp_servlet_name) {
		JspJavaCode jspJavaCode = new JspJavaCode();
		boolean result = false;
		File file = new File(jspPath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			int readByte;
			ByteBuf byteBuf = Unpooled.buffer();
			String readStr = "";
			while ((readByte = fis.read()) != -1) {
				byteBuf.writeByte(readByte);
				byte[] readBytes = new byte[byteBuf.readableBytes()];
				byteBuf.readBytes(readBytes);
				byteBuf.resetReaderIndex();
				readStr = new String(readBytes);
				if (readStr.contains("<%")) {
					int tagBeginIndex = readStr.indexOf("<%");
					int tagBufBeginIndex = readStr.substring(0, tagBeginIndex).getBytes().length;
					String outStr = readStr.substring(0, tagBeginIndex);
					String[] outs = outStr.split("\r\n|\n|\r");
					// 将非标签内容输出
					for (int i = 0; i < outs.length; i++) {
						if (i != outs.length - 1)
							jspJavaCode.getCodeSpace()
									.add("out.write(\"" + outs[i].replaceAll("\"", "\\\\\"") + "\\r\\n" + "\");\r\n");
						else
							jspJavaCode.getCodeSpace()
									.add("out.write(\"" + outs[i].replaceAll("\"", "\\\\\"") + "\");\r\n");
					}
					// 设置读指针到tagBufIndex
					// System.out.println(tagBeginIndex);
					byteBuf.readerIndex(tagBufBeginIndex);
					byteBuf.discardReadBytes();
					// 继续读文件，匹配到标签的结束
					readStr = "";
					while ((readByte = fis.read()) != -1) {
						// System.out.println("JspPaser:52");
						byteBuf.writeByte(readByte);
						readBytes = new byte[byteBuf.readableBytes()];
						byteBuf.readBytes(readBytes);
						byteBuf.resetReaderIndex();
						readStr = new String(readBytes);
						if (readStr.contains("%>")) {
							// 是否在%>出现的时候<%只出现一次
							// System.out.println("JspPaserpaserJspTojava"+readStr.lastIndexOf("<%"));
							if (readStr.lastIndexOf("<%") != 0) {
								System.out.println(byteBuf.readerIndex());
								System.out.println(readStr);
								tagBeginIndex = readStr.lastIndexOf("<%");
								outStr = readStr.substring(0, tagBeginIndex);
								outs = outStr.split("\r\n|\n|\r");
								// 将非标签内容输出
								for (int i = 0; i < outs.length; i++) {
									if (i != outs.length - 1)
										jspJavaCode.getCodeSpace().add("out.write(\""
												+ outs[i].replaceAll("\"", "\\\\\"") + "\\r\\n" + "\");\r\n");
									else
										jspJavaCode.getCodeSpace()
												.add("out.write(\"" + outs[i].replaceAll("\"", "\\\\\"") + "\");\r\n");
								}
							}
							// 匹配最后一次出现的<%
							readStr = readStr.substring(readStr.lastIndexOf("<%"));
							if (readStr.contains("<%!")) {
								String stateStr = readStr.substring(readStr.indexOf("<%") + 3, readStr.indexOf("%>"));
								jspJavaCode.getStatementSpace().add(stateStr);
							} else if (readStr.contains("<%=")) {
								String codeStr = readStr.substring(readStr.indexOf("<%") + 3, readStr.indexOf("%>"));
								jspJavaCode.getCodeSpace().add("out.write(" + codeStr + ");");
							} else if (readStr.contains("<%--")) {
								String note;
								while ((readByte = fis.read()) != -1) {
									byteBuf.writeByte(readByte);
									readBytes = new byte[byteBuf.readableBytes()];
									readStr = readStr + new String(readBytes);
									if (readStr.contains("--%>")) {
										break;
									}
								}
								String codeStr = "";
								if (readStr.contains("--%>")) {
									codeStr = readStr.substring(readStr.indexOf("<%--") + 4, readStr.indexOf("--%>"));
								} else {
									//需要进行继续读操作
									codeStr = readStr.substring(readStr.indexOf("<%--") + 4);
								}
								jspJavaCode.getCodeSpace().add("//" + codeStr);
							} else if (readStr.contains("<%@")) {
								String orderTagPagStr = readStr.substring(readStr.indexOf("<%@") + 3, readStr.indexOf("%>"));
								JspPaser.orderTagToCode(orderTagPagStr, jspJavaCode);

							} else {
								String codeStr = readStr.substring(readStr.indexOf("<%") + 2, readStr.indexOf("%>"));
								jspJavaCode.getCodeSpace().add(codeStr);
							}
							// 一个标签被处理完毕，break
							readStr = "";
							byteBuf.clear();

							break;
						}
					}

				} else if (readStr.contains("<jsp:")) {
					/**
					 * 待实现
					 */
				}

			}
			// 最后将所有内容输出
			String outStr = readStr;
			String[] outs = outStr.split("\r\n|\n|\r");
			// 将非标签内容输出
			for (int i = 0; i < outs.length; i++) {
				if (i != outs.length - 1)
					jspJavaCode.getCodeSpace()
							.add("out.write(\"" + outs[i].replaceAll("\"", "\\\\\"") + "\\r\\n" + "\");\r\n");
				else
					jspJavaCode.getCodeSpace().add("out.write(\"" + outs[i].replaceAll("\"", "\\\\\"") + "\");\r\n");
			}
			// 将jsp翻译为java的代码输出并编译
			jspJavaCode.outTojavaServletFile(webappUri, jsp_servlet_name);
			result = true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return result;
	}

	private static void orderTagToCode(String tagStr, JspJavaCode jspJavaCode) {
		/**
		 * 待完成所有
		 */
		if (tagStr != null) {
			// 将多空格转为单空格并将第一个空格去除
			tagStr = StringTools.trimSeriesBlankSpace((" " + tagStr));
			tagStr = tagStr.substring(1, tagStr.length());
			String codeStr = tagStr.substring(0, tagStr.indexOf(" "));

			if (codeStr.equals("page")) {
				OrderTagPage orderTagPage = OrderTagPage.transToOrderTagPage(tagStr);
				if(orderTagPage.get_import()!=null) {
					String _import = orderTagPage.get_import();
					String importCodes[] = _import.split(",");
					for (int i = 0; i < importCodes.length; i++) {
						jspJavaCode.getImportSpace().add("import "+importCodes[i]+";");
					}
				}
			} else if (codeStr.equals("include")) {

			} else if (codeStr.equals("taglib")) {

			}
		}

	}

	public static boolean includeFile(String uri, String webappPath, ByteBuf byteBuf) {
		/**
		 * 待完成
		 */
		String fileName = uri + webappPath;
		boolean result = false;
		File file = new File(fileName);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			int readByte;
			String readStr = "";
			while ((readByte = fis.read()) != -1) {
				byteBuf.writeByte(readByte);
				byte[] readBytes = new byte[byteBuf.readableBytes()];
				readStr = new String(readBytes);
				if (readStr.contains("<%@")) {

					ByteBuf byteBuf2 = Unpooled.buffer();
					while ((readByte = fis.read()) != -1) {
						byteBuf2.writeByte(readByte);
						readBytes = new byte[byteBuf.readableBytes()];
						readStr = new String(readBytes);
						if (readStr.contains("%>")) {
							readStr = readStr.replaceAll("\r\n|\r|\n", " ");
							readStr = readStr.replaceAll("[\\s]{2,}", " ");
						}
						if (readStr.contains("<%@ include file"))
							;
					}
					byteBuf.writerIndex(byteBuf.readableBytes() - 3);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;

	}


	// 测试
	public static void main(String[] args) {

		// File file = new File("E:\\Users\\userzgx\\Desktop\\web-test\\register.jsp");
		//
		// //File file = new File("E:\\Users\\userzgx\\Desktop\\a.jsp");
		// FileInputStream fis = null;
		// try {
		// fis = new FileInputStream(file);
		// byte[] bytes = new byte[512];
		// ByteBuf buf = Unpooled.buffer();
		// System.out.println("begin");
		// int by;
		// while ((by=fis.read())!=-1) {
		// buf.writeByte(by);
		// bytes = new byte[buf.readableBytes()];
		// buf.readBytes(bytes);
		// buf.resetReaderIndex();
		// System.out.println(new String(bytes).indexOf("<%"));
		// }
		//
		// int readByte;
		// }catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }

		// JspPaser.paserJspTojava("E:\\Users\\userzgx\\Desktop\\web-test\\register.jsp",
		// "da", "");
		// String t = "张<%";
		// t = t.substring(0, t.indexOf("<%")).getBytes().length + "";
		// System.out.println(t.length());

		// System.out.println("<%".indexOf("<%"));
		// System.out.println("\\r\\n;");
		// String readStr = "a \r\n b \n c \r d";
		// readStr = readStr.replaceAll("\r\n|\r|\n", " ");
		// readStr = readStr.replaceAll("[\\s]{2,}", " ");
		// System.out.println(readStr);
		// SAXBuilder sb = new SAXBuilder();
		// System.out.println("as<%d".substring(0, "as<%d".indexOf("<%")));
		// String str = "a\r\nb\nc\r";
		// System.out.println(str.split("\r\n|\n|\r").length);
		// System.out.println("//");

		// try {
		// URL url =new
		// URL("/E:/eclipse-workspace/netty-catserver/target/classes/webapps/ROOT/WEB-INF/web.xml");
		// InputStream in = url.openStream();
		// ByteBuf byteBuf = Unpooled.buffer();
		// int read = 0;
		// while((read = in.read())!=-1) {
		// byteBuf.writeByte(read);
		// }
		// byte[] bytes = new byte[byteBuf.readableBytes()];
		//
		// System.out.println(new String(bytes));
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		System.out.println("\"".replaceAll("\"", "\\\\\""));
	}

}
