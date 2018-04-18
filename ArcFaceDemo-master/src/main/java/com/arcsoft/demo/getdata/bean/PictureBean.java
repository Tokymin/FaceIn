package com.arcsoft.demo.getdata.bean;

public class PictureBean {
	private String title;// item中的文字内容
	private String pic_url;// 图片的地址
	private String web_url;// 跳转页面的url

	public PictureBean(String title, String pic_url, String web_url) {
		super();
		this.title = title;
		this.pic_url = pic_url;
		this.web_url = web_url;
	}

	public PictureBean() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getWeb_url() {
		return web_url;
	}

	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}

	@Override
	public String toString() {
		return "PictureBean [title=" + title + ", pic_url=" + pic_url
				+ ", web_url=" + web_url + "]";
	}

}
