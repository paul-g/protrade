package src.service;

public class FracsoftMatchData {
  private String Timestamp, InplayDelay, MarkeStatus, SelectionId, SelectionName, BP1,
      BV1, LP1, LV1, TotalMatched, LPM;
  
  public FracsoftMatchData(String timestamp, String selctionName, String BV1 ){
    this.Timestamp = timestamp;
    this.SelectionName = selctionName;
    this.BV1 = BV1;
  }

  public FracsoftMatchData(String timestamp, String inplayDelay,
      String markeStatus, String selectionId, String selectionName, String bP1,
      String bV1, String lP1, String lV1, String totalMatched, String lPM) {
    super();
    Timestamp = timestamp;
    InplayDelay = inplayDelay;
    MarkeStatus = markeStatus;
    SelectionId = selectionId;
    SelectionName = selectionName;
    BP1 = bP1;
    BV1 = bV1;
    LP1 = lP1;
    LV1 = lV1;
    TotalMatched = totalMatched;
    LPM = lPM;
  }

  public String getTimestamp() {
    return Timestamp;
  }

  public String getInplayDelay() {
    return InplayDelay;
  }

  public String getMarkeStatus() {
    return MarkeStatus;
  }

  public String getSelectionId() {
    return SelectionId;
  }

  public String getSelectionName() {
    return SelectionName;
  }

  public String getBP1() {
    return BP1;
  }

  public String getBV1() {
    return BV1;
  }

  public String getLP1() {
    return LP1;
  }

  public String getLV1() {
    return LV1;
  }

  public String getTotalMatched() {
    return TotalMatched;
  }

  public String getLPM() {
    return LPM;
  }
  
  
}
