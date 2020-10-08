package ug.grupo2.project.Classes;

import java.util.ArrayList;
import java.util.List;

public final class DataStore {
  private Personal user;
  private List<VacationRequest> requests;
  private AppSettings appSettings;
  boolean dataWasAdded;

  private final static DataStore INSTANCE = new DataStore();

  private DataStore() {
  }

  public static DataStore getInstance() {
    return INSTANCE;
  }

  public AppSettings getAppSettings() {
    return this.appSettings;
  }

  public void setAppSettings(AppSettings appSettings) {
    this.appSettings = appSettings;
  }

  public void setUser(Personal u) {
    this.user = u;
  }

  public Personal getUser() {
    return this.user;
  }

  public List<VacationRequest> getRequests() {
    return this.requests;
  }

  public boolean getDataWasAdded() {
    return this.dataWasAdded;
  }

  public void setDataWasAdded(boolean dataWasAdded) {
    this.dataWasAdded = dataWasAdded;
  }

  public void setRequests(List<VacationRequest> request) {
    this.requests = request;
  }

  public boolean addRequest(VacationRequest request) {
    return this.requests.add(request);
  }

  public boolean removeRequest(VacationRequest request) {
    return this.requests.remove(request);
  }

  public List<VacationRequest> getUserRequest(String userName) {
    List<VacationRequest> userRequests = new ArrayList<>();
    for (VacationRequest vacationRequest : this.requests) {
      if (vacationRequest.getUser().equals(userName))
        userRequests.add(vacationRequest);
    }
    return userRequests;
  }

}
