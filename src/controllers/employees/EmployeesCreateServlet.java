package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class EmployeesCreateServlet
 */
@WebServlet("/employees/create")
public class EmployeesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String _token = (String) request.getParameter("_token");
        HttpSession session = request.getSession(); //セッションオブジェクトを取得

        //リクエスト元がただし以下チェック
        if (_token != null && _token.equals(session.getId())) {

            EntityManager em = DBUtil.createEntityManager();
            Employee e = new Employee();

            //getServletContextメソッドはServletConfigインタフェースのメソッド
            //ServletConfigインタフェースはHttpServletインタフェースに継承されており
            //HttpServletは必ずサーブレットに継承する。 つまり、'this'は自分自身(EmployeesServletCreate)を指している。
            ServletContext context = this.getServletContext();

            //現在日時を取得
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            System.out.println(request.getParameter("admin_flag"));
            //Employeeインスタンスにフォーム入力内容をセット
            e.setCode(request.getParameter("code"));
            e.setName(request.getParameter("name"));
            e.setPassword(
                    //パスワード文字+ペッパー文字を組み合わせて64文字の暗号分をを生成し、セットする
                    EncryptUtil.getPasswordEncrypt(request.getParameter("password"),
                            (String) context.getAttribute("pepper")));
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));

            e.setCreated_at(currentTime);
            e.setUpdated_at(currentTime);
            e.setDelete_flag(0); //従業員登録なので、現役の社員フラグを設定

            // Employeeオブジェクトeについてバリデーション実行
            List<String> errors = EmployeeValidator.validate(e, true, true); //新規登録につき、従業員番号の重複チェク、パスワードの必須入力有り

            if (errors.size() > 0) {
                //エラー処理
                em.close();

                request.setAttribute("_token", _token);
                request.setAttribute("employee", e);
                request.setAttribute("errors", errors);
                //もう一度新規登録入力画面へフォワード
                System.out.println("従業員新規登録入力エラー:new.jspへフォワード");
                request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp").forward(request, response);
            } else {
                //正常処理 DBへレコード追加
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();

                em.persist(e); //insertやupdate処理実行
                transaction.commit(); //DBへコミット
                em.close();

                request.setAttribute("flush", "登録が完了しました。");

                //従業員一覧画面へリダイレクト
                System.out.println("従業員テーブルにレコード追加・indexサーブレットにフォワード");
                response.sendRedirect(request.getContextPath() + "/employees/index");

            }
        }

    }

}
