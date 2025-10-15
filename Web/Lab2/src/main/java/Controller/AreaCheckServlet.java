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
    String[] rParams = (String[]) req.getAttribute("rValuesSelected");

    List<HitResult> allResults = new ArrayList<>();

    for (String rStr : rParams) {
      double r = Double.parseDouble(rStr);
      Point point = new Point(x, y, r);
      boolean hit = AreaChecker.check(point);
      HitResult result = new HitResult(point, hit);
      allResults.add(result);

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
    }

    if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
      resp.setContentType("application/json;charset=UTF-8");
      StringBuilder sb = new StringBuilder();
      sb.append("{\"result\":[");
      for (int i = 0; i < allResults.size(); i++) {
        HitResult r = allResults.get(i);
        sb.append("{\"x\":")
            .append(r.getPoint().getX())
            .append(",\"y\":")
            .append(r.getPoint().getY())
            .append(",\"r\":")
            .append(r.getPoint().getR())
            .append(",\"hit\":")
            .append(r.getHit())
            .append("}");
        if (i < allResults.size() - 1) sb.append(",");
      }
      sb.append("]}");
      resp.getWriter().write(sb.toString());
      return;
    }

    // Иначе показываем HTML через JSP
    req.setAttribute("resultsBatch", allResults); // можно использовать для показа на странице
    req.getRequestDispatcher("/result.jsp").forward(req, resp);
  }
}
