package utils;

import javax.persistence.EntityManager;//DBへのアクセスに関する機能を提供
import javax.persistence.EntityManagerFactory;//EntityManagerのインスタンスを生成するためのクラス
import javax.persistence.Persistence;//EntityManagerFactoryのインスタンスを生成するクラスメソッドcreateEntityManagerFactoryを持っている

public class DBUtil {

    //DB名指定(永続エンティティ)
    private static final String PERSISTENCE_UNIT_NAME = "daily_report_system";
    //emfはクラス変数につき、共有変数となる
    //よって、一度呼び出されたらEntityManagerFactoryクラスのインスタンスが格納され、以降はそれを利用する
    private static EntityManagerFactory emf;

    //EntityManagerのインスタンスを生成し、呼び出し元に返却する
    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    private static EntityManagerFactory getEntityManagerFactory() {
        //初回のみ、EntityManagerFactoryクラスのインスタンスを生成し変数emfに格納
        if (emf == null) {
            //EntityManagerのインスタンスを取得するため、EntityManagerFactoryクラスのインスタンスを取得
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return emf;
    }

}
