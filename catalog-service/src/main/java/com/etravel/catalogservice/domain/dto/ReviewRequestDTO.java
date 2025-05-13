package com.etravel.catalogservice.domain.dto;

public class ReviewRequestDTO {
    private String clientId;
    private String rating;
    private String comment;

    public ReviewRequestDTO() {
    }

    public ReviewRequestDTO(String clientId,
                            String rating,
                            String comment) {
        this.clientId = clientId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}