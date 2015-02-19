package edu.ucla.cs.cs144;

public class ItemInfo {
	private String itemId;
	private String name;
    private String sellerId;
    private String buyPrice;
    private String endTime;
    private String location;
    private String latitude;
    private String longitude;
    private String description;
    
	public ItemInfo() {}
    
	/*public ItemInfo( String itemId, String name, String sellerId, 
                     String buyPrice, String endTime, String description ) {
		this.itemId = itemId;
		this.name = name;
        this.sellerId = sellerId;
        this.buyPrice = buyPrice;
        this.endTime = endTime;
        this.description = description;
	}*/
    
	public String getItemId() {
		return itemId;
	}
	
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }
    
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
