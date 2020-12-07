package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        //ログインしているかどうかの確認をするフィルター
        //ログインしていいない場合はログイン画面へリダイレクト
        //ログインしている場合は、基本的には何もしない
        //従業員管理機能(employyes/*)については、管理者のみアクセスできるようにする。
        //管理者以外のアクセスは、全て、トップページへリダイレクト

        String context_path = ((HttpServletRequest) request).getContextPath(); //daiy_report_system
        String servlet_path = ((HttpServletRequest) request).getServletPath(); //employee等(フォルダ名

        if (!servlet_path.matches("/css.*")) {
            HttpSession session = ((HttpServletRequest) request).getSession();

            //セッションスコープのログイン情報を取得
            Employee e = (Employee) session.getAttribute("login_employee");

            if (!servlet_path.equals("/login")) { //ログイン画面以外へのアクセスか?
                //ログイン画面以外へのアクセスの場合の処理

                if (e == null) { //ログインしているか?
                    //ログインしていないので、ログイン画面へリダイレクト
                    session.setAttribute("flush", "ログインしてください");
                    ((HttpServletResponse) response).sendRedirect(context_path + "/login");
                    return;
                }

                //管理者以外が従業員管理機能にアクセスした場合、トップページへリダイレクト
                if (servlet_path.matches("/employees.*") && e.getAdmin_flag() == 0) {
                    session.setAttribute("flush", "管理者権限が有りません。");
                    ((HttpServletResponse) response).sendRedirect(context_path + "/");
                    return;
                }
            } else { //ログイン画面へのアクセス
                // ログインしているのにログイン画面にアクセスしようとした場合、トップページへリダイレクト
                if (e != null) {
                    session.setAttribute("flush", "ログイン済みです");
                    ((HttpServletResponse) response).sendRedirect(context_path + "/");
                    return;
                }
            }

        }
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
