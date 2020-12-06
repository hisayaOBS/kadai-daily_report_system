package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesIndexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        int page = 1; //デフォルトページを1に設定

        //ページNoを取得(手入力で数字以外を入力できるため例外処理を記述する必要あり
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
        }
        //文字列を整数型に変換できない場合にスローされる(getメソッドで送信されるため

        //従業員レコードを全件取得(ページネイトにより1ページN件分)
        //型指定による、クエリを取得
        TypedQuery<Employee> select_all_query = em.createNamedQuery("getAllEmployees", Employee.class);
        select_all_query = select_all_query.setFirstResult(5 * (page - 1)); //レコード取得を開始する位置を指定するクエリをセット
        select_all_query = select_all_query.setMaxResults(5); //開始位置から何件レコードを取得するか指定するクエリをセット
        //クエリを実行、取得レコードが複数の可能性があるため結果をListで取得
        List<Employee> employees = select_all_query.getResultList();

        //従業員レコードレコード数取得
        TypedQuery<Long> count_query = em.createNamedQuery("getEmployeesCount", Long.class);
        //プリミティブ型からリファレンス型にキャスト
        long employees_count = (long) count_query.getSingleResult();//クエリ実行

        em.close(); //永続エンティティ破棄

        //ビューで表示するため、リクエストスコープに値をセット
        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);

        HttpSession session = request.getSession(); //セッションID取得(初回は生成)

        //セッションスコープにflushがあれば、リクエストスコープにセットし、セッションのflushは削除する
        if (session.getAttribute("flush") != null) {
            request.setAttribute("flush", session.getAttribute("flush"));
            session.removeAttribute("flush");
        }

        //従業員一覧ビューにフォワード
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);

    }

}
