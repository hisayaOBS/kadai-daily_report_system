package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsCreateServlet
 */
@WebServlet("/reports/create")
public class ReportsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //トークンの検証
        //入力内容をリクエストから取得
        //入力内容の検証(バリデーション)
        //エラーがあれば、トークン・入力内容・

        String _token = request.getParameter("_token");
        if (_token != null && _token.equals(request.getSession().getId())) {

            EntityManager em = DBUtil.createEntityManager();
            Report r = new Report();

            Date report_date = new Date(System.currentTimeMillis());//現在日を取得
            String rd_str = request.getParameter("report_date");//入力日付を取得

            //入力した日付があれば、report_dateにキャストして代入
            if (rd_str != null && !rd_str.equals("")) {
                report_date = Date.valueOf(rd_str);
            }

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            Employee e = (Employee) request.getSession().getAttribute("login_employee");

            //Reportオブジェクトにセット
            r.setTitle(request.getParameter("title"));
            r.setContent(request.getParameter("content"));
            r.setEmployee(e);
            r.setReport_date(report_date);
            r.setCreated_at(currentTime);
            r.setUpdated_at(currentTime);

            //バリデーション
            List<String> errors = ReportValidator.validate(r);

            if (errors.size() > 0) {
                //エラー有りにつき、トークン・Reportオブジェクトをリクエストスコープにセットしフォワード
                em.close();
                request.setAttribute("_token", _token);
                request.setAttribute("report", r);
                request.setAttribute("errors", errors);

                request.getRequestDispatcher("/WEB-INF/views/reports/new.jsp").forward(request, response);

            } else {
                //更新処理
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                em.persist(r);
                transaction.commit();
                em.close();

                request.setAttribute("flush", "登録が完了しました");

                response.sendRedirect(request.getContextPath() + "/reports/index");
            }
        }
    }

}
