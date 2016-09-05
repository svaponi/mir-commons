package it.miriade.commons.web.resources.ui;

public class Page extends Resource {

	private static final long serialVersionUID = 1L;
	private String title;
	private String subtitle;
	private String tabtitle;

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public void setTabtitle(String tabtitle) {
		this.tabtitle = tabtitle;
	}

	public String getTitle() {
		return parse(title);
	}

	public String getSubtitle() {
		return parse(subtitle);
	}

	public String getTabtitle() {
		return parse(tabtitle);
	}

	public Page(String title) {
		super();
		this.title = title;
		this.tabtitle = title;
	}

	public Page(String title, String subtitle) {
		super();
		this.title = title;
		this.subtitle = subtitle;
		this.tabtitle = title;
	}

	public Page(String title, String subtitle, String tabtitle) {
		super();
		this.title = title;
		this.subtitle = subtitle;
		this.tabtitle = tabtitle;
	}

}
