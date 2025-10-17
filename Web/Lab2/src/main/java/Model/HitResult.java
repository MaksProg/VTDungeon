package Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitResult {
  private Point point;
  private boolean hit;
  private LocalDateTime serverTime;
  private long execTime;

  public HitResult(Point point, boolean hit, long execTime) {
    this.point = point;
    this.hit = hit;
    this.execTime = execTime;
    this.serverTime = LocalDateTime.now();
  }

  public Point getPoint() {
    return point;
  }

  public boolean getHit() {
    return hit;
  }

  public LocalDateTime getServerTime() {
    return serverTime;
  }

  public long getExecTime() {
    return execTime;
  }

  public String getFormattedServerTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    return serverTime.format(formatter);
  }
}
