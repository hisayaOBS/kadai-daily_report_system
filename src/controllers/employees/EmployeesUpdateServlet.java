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
 * Servlet implementation class EmployeesUpdateServlet
 */
@WebServlet("/employees/update")
public class EmployeesUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //送信元をトークンとセッションIDで確認
        //セッションスコープからIDを取得し、DBからレコード1件取得
        //モデルオブジェクトにリクエストスコープ(フォーム入力値)の値をセット(従業員番号、氏名、パスワード、権限)
        //レコードを更新  (パスワードは入力があるときのみ更新) (社員番号が変更されていれば重複検査を実行)
        //従業員一覧へリダイレクトする

        HttpSession session = request.getSession();
        String _token = request.getParameter("_token");
        if (_token != null && _token.equals(session.getId())) {

            int employee_id = (Integer) session.getAttribute("employee_id");

            EntityManager em = DBUtil.createEntityManager();

            Employee e = em.find(Employee.class, employee_id); //レコード1件取得

            Boolean passwordCheckFlag = true; // パスワード入力があればTrue
            Boolean codeDuplicateCheckFlag = true; // 現在の従業員番号と変更があった倍にTrue

            // 従業員番号に変更ありか?
            if (e.getCode().equals(request.getParameter("code"))) { //従業員番号に変更があるか?
                //変更が無い場合の処理
                codeDuplicateCheckFlag = false; //バリデーションチェック不要
            } else {
                //変更がある場合の処理  Employeeオブジェクトのcodeフィールドを更新
                e.setCode(request.getParameter("code"));
            }

            //パスワードの入力有りか?
            String password = request.getParameter("password");
            if (password == null || password.equals("")) {
                //パスワード入力なし(変更不要)
                passwordCheckFlag = false; //バリデーションチェック不要
            } else {
                //パスワード入力有り
                //アプリケーションスコープのpepperと入力パスワードを引数として、暗号化メソッドをコール

                ServletContext context = this.getServletContext(); //サーブレットコンテキストを取得
                String pepper = (String) context.getAttribute("pepper"); //アプリケーションスコープのペッパーを取得
                String new_password = EncryptUtil.getPasswordEncrypt(password, pepper); //新パスワードのハッシュ値を生成

                e.setPassword(new_password);
            }

            e.setName(request.getParameter("name"));
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            e.setUpdated_at(currentTime);

            //バリデーション実行
            List<String> errors = EmployeeValidator.validate(e, codeDuplicateCheckFlag, passwordCheckFlag);

            if (errors.size() > 0) {
                //エラー有り edit.jspへフォワード
                em.close();

                request.setAttribute("errors", errors);
                request.setAttribute("_token", _token);
                request.setAttribute("employee", e);

                request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp").forward(request, response);

            } else {
                //エラーなし テーブルを更新し、indexサーブレットへリダイレクト
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                em.persist(e);
                transaction.commit();
                em.close();
                // フラッシュメッセージをセッションに格納
                session.setAttribute("flush", "更新が完了しました。");

                //更新が完了したので、セションスコープのemployee_idを削除
                session.removeAttribute("employee_id");

                // indexサーブレットへリダイレクト
                response.sendRedirect(request.getContextPath() + "/employees/index");

            }

        }

    }

}
