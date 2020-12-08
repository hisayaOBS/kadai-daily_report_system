package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //DBから、全レコード取得
        //セッションにフラッシュがあればリクエストに移す
        //リクエストスコープに、レコード情報、レコード件数、現在のページ番号をセット
        //index.jspへフォワード

        int page;

        //ページ番号に正しい値がセットされていなければ、ページ番号に1を設定
        //最初にアクセスした場合・・・リクエストパラメータにpageなし
        //手打ちで、存在しないページ番号を入力した場合
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        EntityManager em = DBUtil.createEntityManager();

        List<Report> reports = em.createNamedQuery("getAllReports", Report.class)
                .setFirstResult((page - 1) * 15)
                .setMaxResults(15)
                .getResultList();

        long reports_count = (long) em.createNamedQuery("getReportsCount", Long.class).getSingleResult();

        em.close();

        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);

        request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp").forward(request, response);
    }

}
