package controllers.employees;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesShowServlet
 */
@WebServlet("/employees/show")
public class EmployeesShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //リクエストスコープからidを受け取り、DBから該当レコードを1件取得して、show.jspへフォワードする
        int id = Integer.parseInt(request.getParameter("id"));

        EntityManager em = DBUtil.createEntityManager();
        Employee e = em.find(Employee.class, id);
        em.close();

        request.setAttribute("employee", e);//リクエストスコープにレコード情報をセット

        System.out.println("レコード1件取得:show.jspへフォワード");
        request.getRequestDispatcher("/WEB-INF/views/employees/show.jsp").forward(request, response);
    }

}
