package Model;

import java.time.LocalDateTime;

public class HitResult {
  private Point point;
  private boolean hit;
  private LocalDateTime serverTime;

  public HitResult(Point point, boolean hit) {
    this.point = point;
    this.hit = hit;
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
}
