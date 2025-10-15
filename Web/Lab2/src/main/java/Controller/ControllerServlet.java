package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    if (req.getParameter("x") != null
        && req.getParameter("y") != null
        && req.getParameter("r") != null) {
      req.getRequestDispatcher("/areaCheck").forward(req, resp);
    } else {
      req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
  }
}
