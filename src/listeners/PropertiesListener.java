package listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class PropertiesListener
 *
 */

//このリスナーは、アプリケーションスコープに、ペッパー文字列もセットする。(アプリケーション起動時に実行)

@WebListener
public class PropertiesListener implements ServletContextListener {

    /**
     * Default constructor.
     */
    public PropertiesListener() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {

        //サーブレットコンテキストを取得
        //※サーブレットコンテキスト・・・Webコンテナ(tomcat等)から提供されるオブジェクトで
        // 1つのWebアプリケーションに対して1つ割り当てられるコンテキスト(アプリケーションスコープはサーブレットコンテキストを利用する)
        ServletContext context = arg0.getServletContext();

        //仮想パスを物理パス(システム上のパス)に変更 (pepper保存ファイルの物理パス取得)
        String path = context.getRealPath("/META-INF/application.properties");

        try {
            InputStream is = new FileInputStream(path); //ペッパーの入力ストリームを開く
            //空のプロパティリストを生成(キーと値が対となったファイルからリストを生成できる)
            Properties properties = new Properties();
            properties.load(is); //キーと値が対となったデータを読み込む
            is.close(); //ストリームを閉じる(メモリを開放するため)

            // stringPropertyNamesメソッドの返却値はプロパティリストのキーのみSet<String>型、そしてその反復子をiteratorメソッドで取得
            Iterator<String> pit = properties.stringPropertyNames().iterator();
            System.out.println("Iteratorの内容:" + pit);

            while (pit.hasNext()) { //hasNextメソッドは次の要素があればtrueを返す
                String pname = pit.next(); //要素を取得
                System.out.println(pname); //pnameには"pepper" ※プロパティリストのキー

                //プロパティリストのキー"pepper"を属性名、バリュー"ペッパー文字列"を属性にセット
                context.setAttribute(pname, properties.getProperty(pname));
                //サーブレットコンテキストのオブジェクトはアプリケーションスコープなので、属性をセットすれば意図的に削除しない限り消えない

            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

}
