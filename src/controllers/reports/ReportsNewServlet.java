package controllers.reports;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;

/**
 * Servlet implementation class ReportsNewServlet
 */
@WebServlet("/reports/new")
public class ReportsNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsNewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //トークンを生成し、リクエストスコープへ
        //新規登録できなかった際に、前回の入力状況を再現するため、Reportクラスのオブジェクトをリクエストスコープへ
        //日付はデフォルトで本日の日付が入力済みにしておく

        request.setAttribute("_token", request.getSession().getId());

        Report r = new Report();
        r.setReport_date(new Date(System.currentTimeMillis()));

        request.getRequestDispatcher("/WEB-INF/views/reports/new.jsp").forward(request, response);

    }

}
