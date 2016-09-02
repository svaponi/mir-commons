package it.miriade.commons.web.resources.ui;

public class UiMessage extends UiResource {

	private static final long serialVersionUID = 1L;
	private String content;
	private MessageType type;

	public enum MessageType {
		SUCCESS, WARNING, INFO, ERROR;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	public UiMessage() {
		super();
	}

	public UiMessage(String content, MessageType type) {
		super();
		this.content = content;
		this.type = type;
	}

	public String getContent() {
		return parse(content);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return parse(type);
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.content;
	}

	public String log() {
		return String.format("Message: %s [%s]", this.content, this.type);
	}

}
