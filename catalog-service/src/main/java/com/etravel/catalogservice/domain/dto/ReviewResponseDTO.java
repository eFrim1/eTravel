package com.etravel.catalogservice.domain.dto;

public class ReviewResponseDTO {
    private String id;
    private String clientId;
    private String rating;
    private String comment;
    private String createdAt;

    public ReviewResponseDTO() { }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}