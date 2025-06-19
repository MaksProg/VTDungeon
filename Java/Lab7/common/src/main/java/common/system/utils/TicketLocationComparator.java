package common.system.utils;

import common.data.Ticket;
import java.util.Comparator;

public class TicketLocationComparator implements Comparator<Ticket> {
  @Override
  public int compare(Ticket t1, Ticket t2) {
    int xCompare = Double.compare(t1.getCoordinates().getX(), t2.getCoordinates().getX());
    if (xCompare != 0) {
      return xCompare;
    }
    return Integer.compare(t1.getCoordinates().getY(), t2.getCoordinates().getY());
  }
}
