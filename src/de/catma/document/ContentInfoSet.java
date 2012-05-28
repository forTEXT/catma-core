package de.catma.document;


public class ContentInfoSet {

	private String author;
	private String description;
	private String publisher;
	private String title;
    
	public ContentInfoSet(String author, String description, String publisher,
			String title) {
		super();
		this.author = author;
		this.description = description;
		this.publisher = publisher;
		this.title = title;
	}

	public ContentInfoSet() {
		this.author = "";
		this.description = "";
		this.title = "";
		this.publisher = "";
	}

	public ContentInfoSet(String title) {
		this("", "", "",title);
	}

	public String getAuthor() {
		return author;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
