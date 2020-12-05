package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.Employee;
import utils.DBUtil;

// 従業員表バリデーション
public class EmployeeValidator {

    public static List<String> validate(Employee e, Boolean codeDuplicateCheckFlag, Boolean passwordCheckFlag) {
        List<String> errors = new ArrayList<>();

        String code_error = validateCode(e.getCode(), codeDuplicateCheckFlag);
        String name_error = validateName(e.getName());
        String pass_error = validatePass(e.getPassword(), passwordCheckFlag);

        if (code_error.equals("")) {
            errors.add(code_error);
        }
        if (name_error.equals("")) {
            errors.add(name_error);
        }
        if (pass_error.equals("")) {
            errors.add(pass_error);
        }

        return errors;
    }

    //社員番号の入力チェック(正しく入力されているか・重複していないか?)
    private static String validateCode(String code, Boolean codeDuplicateCheckFlag) {
        // 必須入力チェック
        if (code == null || code.equals("")) {
            return "社員番号を入力してください。";
        }

        // すでに登録されている社員番号との重複チェック
        // 「新規登録」の場合のみ検証する(登録したい従業員コードが重複していないか?)
        if (codeDuplicateCheckFlag) {
            EntityManager em = DBUtil.createEntityManager();
            // このキャストはラッパークラス(参照型)のLongからプリミティブ型のlongに変換している。
            long employees_count = (long) em.createNamedQuery("checkRegisteredCode", Long.class)
                    .setParameter("code", code)
                    .setParameter("codoe", code).getSingleResult();

            em.close();
            if (employees_count > 0) {
                return "入力された社員番号の情報は既に存在しています。";
            }
        }

        return "";

    }

    // 氏名の入力漏れチェック
    private static String validateName(String name) {
        if (name == null || name.equals("")) {
            return "氏名を入力してください";
        }

        return "";
    }

    // 「新規登録」「パスワード変更」のみ実行
    private static String validatePass(String password, Boolean passwordCheckFlag) {
        if (passwordCheckFlag) {
            if (password == null || passwordCheckFlag.equals("")) {
                return "パスワードを入力してください。";
            }
        }

        return "";
    }

}
