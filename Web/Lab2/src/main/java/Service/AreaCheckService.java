package Service;

import Model.HitResult;
import Model.Point;
import Util.AreaChecker;
import java.util.ArrayList;
import java.util.List;

public class AreaCheckService {

  public List<HitResult> checkPoints(double x, double y, List<Double> rList) {
    List<HitResult> results = new ArrayList<>();
    long start = System.currentTimeMillis();

    for (double r : rList) {
      Point point = new Point(x, y, r);
      boolean hit = AreaChecker.check(point);
      long execTime = System.currentTimeMillis() - start;
      results.add(new HitResult(point, hit, execTime));
    }

    return results;
  }
}
