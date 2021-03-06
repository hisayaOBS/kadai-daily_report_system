package models.validators;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import models.Report;

public class ReportValidator {

    public static List<String> validate(Report r) {

        List<String> errors = new ArrayList<>();

        String title_error = _validateTitle(r.getTitle());
        String content_error = _validateContent(r.getContent());
        String report_date_error = _validateReportDate(r.getReport_date());

        if (!title_error.equals("")) {
            errors.add(title_error);
        }

        if (!content_error.equals("")) {
            errors.add(content_error);
        }

        if (!report_date_error.equals("")) {
            errors.add(report_date_error);
        }

        return errors;
    }

    private static String _validateTitle(String title) {

        if (title == null || title.equals("")) {
            return "タイトルを入力してください。";
        }
        return "";
    }

    private static String _validateContent(String content) {

        if (content == null || content.equals("")) {
            return "内容を入力してください";
        }
        return "";
    }

    private static String _validateReportDate(Date report_date) {

        if (report_date == null) {
            return "日付を入力してください";
        }
        return "";
    }
}
