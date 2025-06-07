package common.system.utils;

import common.data.Ticket;
import java.util.Comparator;

public final class TicketComparator implements Comparator<Ticket> {
  @Override
  public int compare(Ticket ticket1, Ticket ticket2) {
    // Name Compare
    int nameCompare = ticket1.getName().compareTo(ticket2.getName());
    if (nameCompare != 0) return nameCompare;

    // Coordinates X Compare
    int coordinatesXCompare =
        Double.compare(ticket1.getCoordinates().getX(), ticket2.getCoordinates().getX());
    if (coordinatesXCompare != 0) return coordinatesXCompare;

    // Coordinates Y Compare
    int coordinatesYCompare =
        Long.compare(ticket1.getCoordinates().getY(), ticket2.getCoordinates().getY());
    if (coordinatesYCompare != 0) return coordinatesYCompare;

    // Price Compare
    int priceCompare = Double.compare(ticket1.getPrice(), ticket2.getPrice());
    if (priceCompare != 0) return priceCompare;

    // Ticket Type Compare
    int typeCompare = ticket1.getType().compareTo(ticket2.getType());
    if (typeCompare != 0) return typeCompare;

    // Venue Compare (null < other)
    if (ticket1.getVenue() == null && ticket2.getVenue() != null) return -1;
    if (ticket1.getVenue() != null && ticket2.getVenue() == null) return 1;
    if (ticket1.getVenue() != null && ticket2.getVenue() != null) {
      // Venue Name Compare
      int venueNameCompare = ticket1.getVenue().getName().compareTo(ticket2.getVenue().getName());
      if (venueNameCompare != 0) return venueNameCompare;

      // Venue Capacity Compare
      int venueCapacityCompare =
          Integer.compare(ticket1.getVenue().getCapacity(), ticket2.getVenue().getCapacity());
      if (venueCapacityCompare != 0) return venueCapacityCompare;

      // Venue Type Compare (null < other)
      if (ticket1.getVenue().getType() == null && ticket2.getVenue().getType() != null) return -1;
      if (ticket1.getVenue().getType() != null && ticket2.getVenue().getType() == null) return 1;
      if (ticket1.getVenue().getType() != null && ticket2.getVenue().getType() != null) {
        int venueTypeCompare = ticket1.getVenue().getType().compareTo(ticket2.getVenue().getType());
        if (venueTypeCompare != 0) return venueTypeCompare;
      }

      // Address Compare
      if (ticket1.getVenue().getAddress() == null && ticket2.getVenue().getAddress() != null)
        return -1;
      if (ticket1.getVenue().getAddress() != null && ticket2.getVenue().getAddress() == null)
        return 1;
      if (ticket1.getVenue().getAddress() != null && ticket2.getVenue().getAddress() != null) {
        // ZipCode Compare
        int zipCodeCompare =
            ticket1
                .getVenue()
                .getAddress()
                .getZipCode()
                .compareTo(ticket2.getVenue().getAddress().getZipCode());
        if (zipCodeCompare != 0) return zipCodeCompare;

        // Town Compare
        if (ticket1.getVenue().getAddress().getTown() == null
            && ticket2.getVenue().getAddress().getTown() != null) return -1;
        if (ticket1.getVenue().getAddress().getTown() != null
            && ticket2.getVenue().getAddress().getTown() == null) return 1;
        if (ticket1.getVenue().getAddress().getTown() != null
            && ticket2.getVenue().getAddress().getTown() != null) {
          // Location X Compare
          int locationXCompare =
              Float.compare(
                  ticket1.getVenue().getAddress().getTown().getX(),
                  ticket2.getVenue().getAddress().getTown().getX());
          if (locationXCompare != 0) return locationXCompare;

          // Location Y Compare
          int locationYCompare =
              Double.compare(
                  ticket1.getVenue().getAddress().getTown().getY(),
                  ticket2.getVenue().getAddress().getTown().getY());
          if (locationYCompare != 0) return locationYCompare;

          // Location Name Compare (null < other)
          if (ticket1.getVenue().getAddress().getTown().getName() == null
              && ticket2.getVenue().getAddress().getTown().getName() != null) return -1;
          if (ticket1.getVenue().getAddress().getTown().getName() != null
              && ticket2.getVenue().getAddress().getTown().getName() == null) return 1;
          if (ticket1.getVenue().getAddress().getTown().getName() != null
              && ticket2.getVenue().getAddress().getTown().getName() != null) {
            int locationNameCompare =
                ticket1
                    .getVenue()
                    .getAddress()
                    .getTown()
                    .getName()
                    .compareTo(ticket2.getVenue().getAddress().getTown().getName());
            if (locationNameCompare != 0) return locationNameCompare;
          }
        }
      }
    }

    // Tickets are equal
    return 0;
  }
}
