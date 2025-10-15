package Controller;

import Model.HitResult;
import Model.Point;
import Util.AreaChecker;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AreaCheckServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String xParam = req.getParameter("x");
    String yParam = req.getParameter("y");
    String rParam = req.getParameter("r");

    if (xParam == null
        || xParam.isEmpty()
        || yParam == null
        || yParam.isEmpty()
        || rParam == null
        || rParam.isEmpty()) {
      req.setAttribute("errorMessage", "Все параметры должны быть заданы!");
      req.getRequestDispatcher("/index.jsp").forward(req, resp);
      return;
    }

    double x = Double.parseDouble(xParam);
    double y = Double.parseDouble(yParam);
    double r = Double.parseDouble(rParam);

    long start = System.currentTimeMillis();
    Point point = new Point(x, y, r);
    boolean hit = AreaChecker.check(point);
    long execTime = System.currentTimeMillis() - start;
    HitResult result = new HitResult(point, hit, execTime);

    ServletContext context = getServletContext();
    synchronized (context) {
      @SuppressWarnings("unchecked")
      List<HitResult> results = (List<HitResult>) context.getAttribute("results");
      if (results == null) {
        results = new ArrayList<>();
        context.setAttribute("results", results);
      }
      results.add(result);
    }

    req.setAttribute("result", result);
    req.getRequestDispatcher("/result.jsp").forward(req, resp);
  }
}
