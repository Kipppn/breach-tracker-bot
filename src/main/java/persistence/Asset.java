package persistence;

import java.util.Date;

public class Asset {
     // Data Class Variables Mapped From JSON
     private String name;
     private String title;
     private String domain;
     private String breachData;
     private String addedData;
     private String modifiedDate;
     private String pwnCount;
     private String description;
     private String dataClasses;
     private String logoPath;
     private Boolean isVerified;
     private Boolean isFabricated;
     private Boolean isSensitive;
     private Boolean isRetired;
     private Boolean isSpamList;

     public Asset(String name, String title, String domain, String breachData, String addedData, String modifiedDate,
                  String pwnCount, String description, String dataClasses, String logoPath, Boolean isVerified,
                  Boolean isFabricated, Boolean isSensitive, Boolean isRetired, Boolean isSpamList) {
          this.name = name;
          this.title = title;
          this.domain = domain;
          this.breachData = breachData;
          this.addedData = addedData;
          this.modifiedDate = modifiedDate;
          this.pwnCount = pwnCount;
          this.description = description;
          this.dataClasses = dataClasses;
          this.logoPath = logoPath;
          this.isVerified = isVerified;
          this.isFabricated = isFabricated;
          this.isSensitive = isSensitive;
          this.isRetired = isRetired;
          this.isSpamList = isSpamList;
     }

     public void setName(String name) {
          this.name = name;
     }

     public String getName() {
          return name;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public String getTitle() {
          return title;
     }

     public void setDomain(String domain) {
          this.domain = domain;
     }

     public void setBreachData(String breachData) {
          this.breachData = breachData;
     }

     public String getBreachData() {
          return breachData;
     }

     public String getDomain() {
          return domain;
     }

     public void setAddedData(String addedData) {
          this.addedData = addedData;
     }

     public String getAddedData() {
          return addedData;
     }

     public void setModifiedDate(String modifiedDate) {
          this.modifiedDate = modifiedDate;
     }

     public String getModifiedDate() {
          return modifiedDate;
     }

     public void setPwnCount(String pwnCount) {
          this.pwnCount = pwnCount;
     }

     public String getPwnCount() {
          return pwnCount;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public String getDescription() {
          return description;
     }

     public void setDataClasses(String dataClasses) {
          this.dataClasses = dataClasses;
     }

     public String getDataClasses() {
          return dataClasses;
     }

     public void setLogoPath(String logoPath) {
          this.logoPath = logoPath;
     }

     public String getLogoPath() {
          return logoPath;
     }

     public void setVerified(Boolean verified) {
          isVerified = verified;
     }

     public Boolean getVerified() {
          return isVerified;
     }

     public void setFabricated(Boolean fabricated) {
          isFabricated = fabricated;
     }

     public Boolean getFabricated() {
          return isFabricated;
     }

     public void setSensitive(Boolean sensitive) {
          isSensitive = sensitive;
     }

     public Boolean getSensitive() {
          return isSensitive;
     }

     public void setRetired(Boolean retired) {
          isRetired = retired;
     }

     public Boolean getRetired() {
          return isRetired;
     }

     public void setSpamList(Boolean spamList) {
          isSpamList = spamList;
     }

     public Boolean getSpamList() {
          return isSpamList;
     }
}
