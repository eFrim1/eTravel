package com.etravel.reservationservice.domain.dto;

public class ReservationRequestDTO {
    private String clientId;
    private String tourPackageId;

    public ReservationRequestDTO() {}

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getTourPackageId() { return tourPackageId; }
    public void setTourPackageId(String tourPackageId) { this.tourPackageId = tourPackageId; }
}
