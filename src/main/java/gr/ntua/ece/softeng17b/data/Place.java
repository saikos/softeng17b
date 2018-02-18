package gr.ntua.ece.softeng17b.data;

public class Place extends SimpleLocation {

	private long id;
	private String name;
	private String description;

	public Place(long id, String name, String description, double latitude, double longitude) {
		super(latitude, longitude);
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}