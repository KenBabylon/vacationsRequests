package ug.grupo2.project.Classes;

import ug.grupo2.project.enums.AccessLevel;

public class Personal {
  private String User;
  private String Password;
  private String Name;
  private String ProfilePicture;
  private AccessLevel AccessLevel;

  public Personal(String User, String Password, String Name, String ProfilePicture, AccessLevel AccessLevel) {
    this.User = User;
    this.Password = Password;
    this.Name = Name;
    this.ProfilePicture = ProfilePicture;
    this.AccessLevel = AccessLevel;
  }

  public String getUser() {
    return this.User;
  }

  public void setUser(String User) {
    this.User = User;
  }

  public String getPassword() {
    return this.Password;
  }

  public void setPassword(String Password) {
    this.Password = Password;
  }

  public String getName() {
    return this.Name;
  }

  public void setName(String Name) {
    this.Name = Name;
  }

  public String getProfilePicture() {
    return this.ProfilePicture;
  }

  public void setProfilePicture(String ProfilePicture) {
    this.ProfilePicture = ProfilePicture;
  }

  public AccessLevel getAccessLevel() {
    return this.AccessLevel;
  }

  public void setAccessLevel(AccessLevel AccessLevel) {
    this.AccessLevel = AccessLevel;
  }

}
