package com.redlei.common.response;

public class HtmlResult extends Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3221859311835204499L;

	private String head;
	private String contentType = "text/html";
	private String content;

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
