package com.example.wp.Data;

public class WpPOJO {

	String title = "dummy";
	String content = "dummy";
	String id = "dummy";
	String date = "dummy";

	public WpPOJO(String title, String content, String id, String date) {
		super();
		this.title = title;
		this.content = content;
		this.id = id;
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
