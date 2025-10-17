package Controller;

import Model.HitResult;
import Service.AreaCheckService;
import Util.RequestUtils;
import Util.RequestUtils.InputParams;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AreaCheckServlet extends HttpServlet {

  private final AreaCheckService service = new AreaCheckService();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    RequestUtils.setupCommonAttributes(req);
    InputParams params = (InputParams) req.getAttribute("validatedParams");
    if (params == null) {
      req.getRequestDispatcher("/index.jsp").forward(req, resp);
      return;
    }

    List<HitResult> results = service.checkPoints(params.x(), params.y(), params.rList());

    ServletContext context = getServletContext();
    synchronized (context) {
      @SuppressWarnings("unchecked")
      List<HitResult> contextResults = (List<HitResult>) context.getAttribute("results");
      if (contextResults == null) {
        contextResults = new java.util.ArrayList<>();
        context.setAttribute("results", contextResults);
      }
      contextResults.addAll(results);
    }

    req.setAttribute("results", results);
    req.getRequestDispatcher("/result.jsp").forward(req, resp);
  }
}
