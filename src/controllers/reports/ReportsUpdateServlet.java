package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsUpdateServlet
 */
@WebServlet("/reports/update")
public class ReportsUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //トークンを検証
        //フォームの入力値をリクエストから取得し、Reportにセットする
        //バリデーション
        //問題がなければインデックスへリダイレクト(フラッシュメッセージも忘れずに)

        String _token = (String) request.getParameter("_token");
        System.out.println("トークン検証前");
        System.out.println(_token);
        System.out.println(request.getSession().getId());
        if (_token != null && _token.equals(request.getSession().getId())) {

            System.out.println("トークン検証クリア");
            EntityManager em = DBUtil.createEntityManager();

            int report_id = (Integer) (request.getSession().getAttribute("report_id"));

            Report r = em.find(Report.class, report_id);

            Date report_date;
            String check_date = request.getParameter("report_date");
            if (check_date != null && !check_date.equals("")) {
                report_date = Date.valueOf(check_date);
            } else {
                report_date = null;
            }

            String title = request.getParameter("title");
            String content = request.getParameter("content");
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            r.setTitle(title);
            r.setContent(content);
            r.setUpdated_at(currentTime);
            r.setReport_date(report_date);
            List<String> errors = ReportValidator.validate(r);
            if (errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("report", r);
                request.setAttribute("errors", errors);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "更新が完了しました。");

                request.getSession().removeAttribute("report_id");

                response.sendRedirect(request.getContextPath() + "/reports/index");
            }

        }

    }

}
