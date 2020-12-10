package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsEditServlet
 */
@WebServlet("/reports/edit")
public class ReportsEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //トークン生成しリクエストスコープへ
        //リクエストパラメータからidを取得レコード取得
        //エラーはビューで判定
        //ログインしているユーザと同じレコードを取得している場合のみ、リクエストスコープにトークン・

        EntityManager em = DBUtil.createEntityManager();
        int id = Integer.parseInt(request.getParameter("id"));

        Report r = em.find(Report.class, id);
        em.close();

        Employee login_employee = (Employee) request.getSession().getAttribute("login_employee");

        if (r != null && r.getEmployee().getId() == login_employee.getId()) {

            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("report", r);
            request.getSession().setAttribute("report_id", id);
        }

        request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp").forward(request, response);

    }

}
