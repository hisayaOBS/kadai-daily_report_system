<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <h2>従業員 新規登録ページ</h2>
        <form action="<c:url value='/employees/create' />" method="POST">
            <c:import url="_form.jsp"></c:import>
            <button type="submit">登録</button>
        </form>
        <p><a href="<c:url value='/employees/index' />">一覧へ戻る</a></p>
    </c:param>
</c:import>
