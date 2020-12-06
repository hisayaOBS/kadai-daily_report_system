package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesDestroyServlet
 */
@WebServlet("/employees/destroy")
public class EmployeesDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesDestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //正しいリクエスト元かトークンとサーバに保存されたセッションIDを照合
        //セッションスコープからemployee_idを取得し、該当レコードを1件取得。
        //該当レコードの"delete_flag"を1(Integer)に変更し、論理的に退職とする。
        //セッションスコープにフラッシュメッセージをセットし、employee_idを削除

        String _token = request.getParameter("_token");
        HttpSession session = request.getSession();

        if (_token != null && _token.equals(session.getId())) {

            int employee_id = (Integer) session.getAttribute("employee_id");
            EntityManager em = DBUtil.createEntityManager();

            Employee e = em.find(Employee.class, employee_id); //該当レコード取得

            e.setDelete_flag(1); // 退職コード1をセット
            e.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.persist(e); //insert・update
            transaction.commit();
            em.close();

            //フラッシュメッセージをセッションスコープにセットし、employee_idを削除
            session.removeAttribute("employee_id");
            session.setAttribute("flush", "削除が完了しました。");

            response.sendRedirect(request.getContextPath() + "/employees/index");

        }

    }

}
