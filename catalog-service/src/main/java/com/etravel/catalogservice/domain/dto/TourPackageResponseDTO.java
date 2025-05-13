package com.etravel.catalogservice.domain.dto;
import java.util.List;

public class TourPackageResponseDTO {
    private String id;
    private String destination;
    private String price;
    private String startDate;
    private String endDate;
    private List<String> images;
    private List<ReviewResponseDTO> reviews;

    public TourPackageResponseDTO() { }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public List<ReviewResponseDTO> getReviews() { return reviews; }
    public void setReviews(List<ReviewResponseDTO> reviews) { this.reviews = reviews; }
}
