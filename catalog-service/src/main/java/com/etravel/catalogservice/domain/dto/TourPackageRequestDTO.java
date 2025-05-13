package com.etravel.catalogservice.domain.dto;

public class TourPackageRequestDTO {
    private String destination;
    private String price;
    private String startDate;
    private String endDate;
    private String image1;
    private String image2;
    private String image3;

    public TourPackageRequestDTO() { }

    public TourPackageRequestDTO(String destination,
                                 String price,
                                 String startDate,
                                 String endDate,
                                 String image1,
                                 String image2,
                                 String image3) {
        this.destination = destination;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getImage1() { return image1; }
    public void setImage1(String image1) { this.image1 = image1; }

    public String getImage2() { return image2; }
    public void setImage2(String image2) { this.image2 = image2; }

    public String getImage3() { return image3; }
    public void setImage3(String image3) { this.image3 = image3; }
}