package controllers.employees;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesEditServlet
 */
@WebServlet("/employees/edit")
public class EmployeesEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //トークン・レコード情報をリクエストスコープへ
        //idをセッションスコープへセット (入力エラー等でリダイレクトされた際、リクエストスコープだと消滅してしまうため

        EntityManager em = DBUtil.createEntityManager();
        int id = Integer.parseInt(request.getParameter("id"));
        Employee e = em.find(Employee.class, id); //該当レコード1件取得

        HttpSession session = request.getSession(); //セッションインタフェースオブジェクト取得

        request.setAttribute("_token", session.getId()); //トークンをリクエストスコープにセット
        request.setAttribute("employee", e); //レコード情報をリクエストスコープにセット
        session.setAttribute("employee_id", id); //idをセッションスコープへ

        System.out.println("ID:" + id + "edit.jspへフォワード");
        request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp").forward(request, response);

    }

}
