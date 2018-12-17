package com.zgx.http.jsp.paser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("page")
public class OrderTagPage {
	@XStreamAsAttribute
	@XStreamAlias("buffer")
	private String _buffer = null;
	@XStreamAsAttribute
	@XStreamAlias("autoFlush")
	private String _autoFlush = null;
	@XStreamAsAttribute
	@XStreamAlias("contentType")
	private String _contentType = null;
	@XStreamAsAttribute
	@XStreamAlias("errorPage")
	private String _errorPage = null;
	@XStreamAsAttribute
	@XStreamAlias("isErrorPage")
	private String _isErrorPage = null;
	@XStreamAsAttribute
	@XStreamAlias("extends")
	private String _extends = null;
	@XStreamAsAttribute
	@XStreamAlias("import")
	private String _import = null;
	@XStreamAsAttribute
	@XStreamAlias("info")
	private String _info = null;
	@XStreamAsAttribute
	@XStreamAlias("isThreadSafe")
	private String _isThreadSafe = null;
	@XStreamAsAttribute
	@XStreamAlias("language")
	private String _language = null;
	@XStreamAsAttribute
	@XStreamAlias("session")
	private String _session = null;
	@XStreamAsAttribute
	@XStreamAlias("isELIgnored")
	private String _isELIgnored = null;
	@XStreamAsAttribute
	@XStreamAlias("isScriptingEnabled")
	private String _isScriptingEnabled = null;

	public String get_buffer() {
		return _buffer;
	}

	public void set_buffer(String _buffer) {
		this._buffer = _buffer;
	}

	public String get_autoFlush() {
		return _autoFlush;
	}

	public void set_autoFlush(String _autoFlush) {
		this._autoFlush = _autoFlush;
	}

	public String get_contentType() {
		return _contentType;
	}

	public void set_contentType(String _contentType) {
		this._contentType = _contentType;
	}

	public String get_errorPage() {
		return _errorPage;
	}

	public void set_errorPage(String _errorPage) {
		this._errorPage = _errorPage;
	}

	public String get_isErrorPage() {
		return _isErrorPage;
	}

	public void set_isErrorPage(String _isErrorPage) {
		this._isErrorPage = _isErrorPage;
	}

	public String get_extends() {
		return _extends;
	}

	public void set_extends(String _extends) {
		this._extends = _extends;
	}

	public String get_import() {
		return _import;
	}

	public void set_import(String _import) {
		this._import = _import;
	}

	public String get_info() {
		return _info;
	}

	public void set_info(String _info) {
		this._info = _info;
	}

	public String get_isThreadSafe() {
		return _isThreadSafe;
	}

	public void set_isThreadSafe(String _isThreadSafe) {
		this._isThreadSafe = _isThreadSafe;
	}

	public String get_language() {
		return _language;
	}

	public void set_language(String _language) {
		this._language = _language;
	}

	public String get_session() {
		return _session;
	}

	public void set_session(String _session) {
		this._session = _session;
	}

	public String get_isELIgnored() {
		return _isELIgnored;
	}

	public void set_isELIgnored(String _isELIgnored) {
		this._isELIgnored = _isELIgnored;
	}

	public String get_isScriptingEnabled() {
		return _isScriptingEnabled;
	}

	public void set_isScriptingEnabled(String _isScriptingEnabled) {
		this._isScriptingEnabled = _isScriptingEnabled;
	}

	private static XStream xstream = null;
	static {
		xstream = new XStream();
		xstream.processAnnotations(OrderTagPage.class);
	}

	public static OrderTagPage transToOrderTagPage(String tagStr) {
		tagStr = "<" + tagStr + "/>";
		return (OrderTagPage) xstream.fromXML(tagStr);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String xml = "<page import=\"com.cwy.bean.Site,com.cwy.bean.Site\"/>";
		System.out.println(xml);
		XStream xstream = new XStream();
		xstream.processAnnotations(OrderTagPage.class);
		OrderTagPage orderTagObject = (OrderTagPage) xstream.fromXML(xml);
		System.out.println(orderTagObject.get_import() + orderTagObject.get_autoFlush());
		OrderTagPage orderTagPageObject = new OrderTagPage();
		orderTagPageObject.set_import("COM.ZGX.TEST");
		// OrderTagPageObject orderTagPageObject = new OrderTagPageObject();
		// orderTagPageObject.set_import("COM.ZGX.TEST");
		// XStream xStream = new XStream();
		// xStream.processAnnotations(OrderTagPageObject.class);
		String result = xstream.toXML(orderTagPageObject);
		System.out.println(result.split("y").length);
		//System.out.println("fasdf".substring(2, 0));
	}
}
