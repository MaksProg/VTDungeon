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
    double x = Double.parseDouble(req.getParameter("x"));
    double y = Double.parseDouble(req.getParameter("y"));
    double r = Double.parseDouble(req.getParameter("r"));

    Point point = new Point(x, y, r);
    boolean hit = AreaChecker.check(point);
    HitResult result = new HitResult(point, hit);

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
