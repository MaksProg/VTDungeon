package common.network;

import common.data.Ticket;

public class RequestBodyWithTicket extends RequestBody {
  private static final long serialVersionUID = -5519361223588780166L;
  Ticket ticket;

  public RequestBodyWithTicket(String[] args, Ticket ticket) {
    super(args);
    this.ticket = ticket;
  }

  public Ticket getTicket() {
    return ticket;
  }
}
