package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

//従業員テーブル

@Table(name = "employees") // テーブル名employees(複数形)作成
@NamedQueries({ //このSQLはモデルへのアクセス(表名が単数形になっているのはモデル名だから)
        //全レコード抽出
        @NamedQuery(name = "getAllEmployees", query = "select e from Employee as e order by e.id desc"),
        //全レコード数抽出
        @NamedQuery(name = "getEmployeesCount", query = "select count(e) from Employee as e"),
        //社員番号が既に登録済みかどうかチェック(:codeはプレースホルダーなので、後で他の文字列にい置き換わる)
        @NamedQuery(name = "checkRegisteredCode", query = "select count(e) from Employee as e where e.code = :code"),
        //ログインチェック(現役の従業員&社員番号・パスワードの入力が正しいか?
        @NamedQuery(name = "checkLoginCodeAndPassword", query = "select e from Employee as e where e.delete_flag = 0 and e.code = :code and e.password = :pass")
})
@Entity //実態(テーブル及びカラムを列挙)
public class Employee { //クラス名 = テーブル名の単数形(慣例)

    @Id //主キー
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "admin_flag", nullable = false)
    private Integer admin_flag;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    @Column(name = "delete_flag", nullable = false)
    private Integer delete_flag;

    // 以下セッター・ゲッター
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdmin_flag() {
        return admin_flag;
    }

    public void setAdmin_flag(Integer admin_flag) {
        this.admin_flag = admin_flag;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(Integer delete_flag) {
        this.delete_flag = delete_flag;
    }

}
