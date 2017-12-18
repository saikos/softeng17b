package gr.ntua.ece.softeng17b.data;

public class Place {

	private long id;
	private String name;
	private String description;
	private double latitude;
	private double longitude;

	public Place(long id, String name, String description, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
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


	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

}