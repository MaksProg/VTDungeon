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
    String[] rParams = req.getParameterValues("r");

    if (xParam == null
        || xParam.isEmpty()
        || yParam == null
        || yParam.isEmpty()
        || rParams == null
        || rParams.length == 0) {
      req.setAttribute("errorMessage", "Все параметры должны быть заданы!");
      req.getRequestDispatcher("/index.jsp").forward(req, resp);
      return;
    }

    double x = Double.parseDouble(xParam);
    double y = Double.parseDouble(yParam);

    List<HitResult> results = new ArrayList<>();
    long start = System.currentTimeMillis();

    for (String rStr : rParams) {
      double r = Double.parseDouble(rStr);
      Point point = new Point(x, y, r);
      boolean hit = AreaChecker.check(point);
      long execTime = System.currentTimeMillis() - start;
      results.add(new HitResult(point, hit, execTime));
    }

    ServletContext context = getServletContext();
    synchronized (context) {
      @SuppressWarnings("unchecked")
      List<HitResult> contextResults = (List<HitResult>) context.getAttribute("results");
      if (contextResults == null) {
        contextResults = new ArrayList<>();
        context.setAttribute("results", contextResults);
      }
      contextResults.addAll(results);
    }

    req.setAttribute("results", results);
    req.getRequestDispatcher("/result.jsp").forward(req, resp);
  }
}
