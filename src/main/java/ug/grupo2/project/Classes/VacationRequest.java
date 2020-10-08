package ug.grupo2.project.Classes;

import ug.grupo2.project.enums.RequestStatus;

public class VacationRequest {
  String dateFrom;
  String dateTo;
  String reason;
  String user;
  RequestStatus status;

  public VacationRequest(String dateFrom, String dateTo, String reason, String user, RequestStatus requestStatus) {
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.reason = reason;
    this.user = user;
    this.status = requestStatus;
  }

  public String getDateFrom() {
    return this.dateFrom;
  }

  public void setDateFrom(String dateFrom) {
    this.dateFrom = dateFrom;
  }

  public String getDateTo() {
    return this.dateTo;
  }

  public void setDateTo(String dateTo) {
    this.dateTo = dateTo;
  }

  public String getReason() {
    return this.reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getUser() {
    return this.user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public RequestStatus getStatus() {
    return this.status;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }

}
