package com.etravel.reservationservice.domain.dto;

public class ReservationResponseDTO {
    private String id;
    private String clientId;
    private String tourPackageId;
    private String reservedAt;
    private String status;

    public ReservationResponseDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getTourPackageId() { return tourPackageId; }
    public void setTourPackageId(String tourPackageId) { this.tourPackageId = tourPackageId; }

    public String getReservedAt() { return reservedAt; }
    public void setReservedAt(String reservedAt) { this.reservedAt = reservedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
