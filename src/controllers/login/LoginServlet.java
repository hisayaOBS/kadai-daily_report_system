package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // _token・hasError(パスワード不一致flg)をfalseでセッションスコープへセット
        // LogoutServletからのフラッシュがセッションスコープにあれば、リクエストスコープに移す
        // login.jspへフォワード

        HttpSession session = request.getSession();

        request.setAttribute("_token", session.getId());
        request.setAttribute("hasError", false);

        // ログアウト成功のフラッシュメッセージがあれば、リクエストスコープへ移動
        if (session.getAttribute("flush") != null) {
            request.setAttribute("flush", session.getAttribute("flush"));
            session.removeAttribute("flush");
        }

        request.getRequestDispatcher("/WEB-INF/views/login/login.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //リクエストスコープからID、パスを取得
        //アプリケーションスコープのpepperを取得し、パスワードをハッシュ化してDBに保存してあるレコードを検索する
        //一致すれば、そのレコードとフラッシュメッセージをセッションスコープに保存し、indexにリダイレクト
        //不一致の場合は、_token、haErrorにtrue、codeをリクエストスコープにセットし、login.jspにフォワード

        Boolean check_result = false; //認証結果を格納する変数

        String code = request.getParameter("code"); //入力されたIDを取得
        String plain_pass = request.getParameter("password"); //入力されたパスワードを取得

        Employee e = null;
        //ID、パスワード共に入力されているか?

        if (code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")) {
            //アプリケーションスコープを取得(ServletContext)
            ServletContext context = getServletContext(); //アプリケーションスコープオブジェクト取得
            String pepper = (String) context.getAttribute("pepper"); //pepper文字列を取得

            EntityManager em = DBUtil.createEntityManager();

            // ハッシュ化パスワード生成
            String password = EncryptUtil.getPasswordEncrypt(plain_pass, pepper);

            //社員番号とパスワードの照合
            try {
                TypedQuery<Employee> login_query = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)
                        .setParameter("code", code)
                        .setParameter("pass", password);
                e = login_query.getSingleResult();
            } catch (NoResultException ex) {
            }
            em.close();

            //ログイン成功?
            if (e != null) {
                check_result = true;
            }
        }
        if (!check_result) {
            //ログイン失敗 ログイン画面へフォワード
            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("hasError", true);
            request.setAttribute("code", code);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
            rd.forward(request, response);

        } else {
            //ログイン成功 フラッシュメッセージ・ログイン情報をセッションスコープにセット
            HttpSession session = request.getSession();
            session.setAttribute("flush", "ログインしました。");
            session.setAttribute("login_employee", e);

            //トップページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/");
            System.out.println("リダイレクトごに出力できるか");
        }

    }

}
