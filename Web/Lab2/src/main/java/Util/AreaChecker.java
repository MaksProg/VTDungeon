package Util;

import Model.Point;

public class AreaChecker {
  public static boolean check(Point p) {
    double x = p.getX();
    double y = p.getY();
    double r = p.getR();

    // Прямоугольник (верхняя левая четверть)
    if (x <= 0 && y >= 0 && x >= -r && y <= r) return true;

    // Четверть круга (верхняя правая)
    if (x >= 0 && y >= 0 && (x * x + y * y) <= (r / 2.0) * (r / 2.0)) return true;

    // Прямоугольный треугольник (нижняя правая)
    if (x >= 0 && y <= 0 && y >= (x / 2.0 - r / 2.0)) return true;

    return false;
  }
}
