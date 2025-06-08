package common.network;

import common.data.Venue;

public class RequestBodyWithVenue extends RequestBody {
  private static final long serialVersionUID = 1234567890123456789L;

  Venue venue;

  public RequestBodyWithVenue(String[] args, Venue venue) {
    super(args);
    this.venue = venue;
  }

  public Venue getVenue() {
    return venue;
  }
}
