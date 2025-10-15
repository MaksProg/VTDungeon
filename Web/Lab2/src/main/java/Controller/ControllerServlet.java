package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ControllerServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    List<Integer> xValues = List.of(-5, -4, -3, -2, -1, 0, 1, 2, 3);
    List<Integer> rValues = List.of(1, 2, 3, 4, 5);

    req.setAttribute("xValues", xValues);
    req.setAttribute("rValues", rValues);

    String xParam = req.getParameter("x");
    String yParam = req.getParameter("y");
    String[] rParams = req.getParameterValues("r");

    if (xParam != null && yParam != null && rParams != null && rParams.length > 0) {
      req.setAttribute("rList", rParams);
      req.getRequestDispatcher("/areaCheck").forward(req, resp);
    } else {
      req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
  }
}
