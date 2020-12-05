package controllers.employees;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;

/**
 * Servlet implementation class EmployeesNewServlet
 */
@WebServlet("/employees/new")
public class EmployeesNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesNewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //リクエスト元が正しいかサーバ側で認証するために、トークンを生成
        //リクエストスコープにセットする(CSRF対策) トークンには今回はセッションIDを利用する
        request.setAttribute("_token", request.getSession().getId());

        //フォーム内には、レコード情報等をデフォルトで入力済みにする機能をもたせたいので
        //新規登録の際も、空のインスタンスを生成し、リクエストスコープにセットする
        //この記述を省略すると、NullNotPointer例外が発生してしまう
        request.setAttribute("employee", new Employee());

        System.out.println("new.jspへフォワード");
        request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp").forward(request, response);
    }

}
