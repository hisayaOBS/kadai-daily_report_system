<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・
        </c:forEach>
    </div>
</c:if>



<label for="code">社員番号</label><br />
<input type="text" name="code" value="${employee.code}" />
<br /><br />

<label for="name">氏名</label><br />
<input type="text" name="name" value="${employee.name}" />
<br /><br />

<label for="password">パスワード</label><br />
<input type="password" name="password" />
<br /><br />

<label for="admin_flag">権限</label><br />
<select name="admin_flg" id="">
    <option value="0" <c:if test="${$employee.admin_flg == 0}">selected</c:if> >一般</option>
    <option value="0" <c:if test="${$employee.admin_flg == 1}">selected</c:if> >管理者</option>
</select>
<br /><br />

<!-- リクエストスコープはリクエスト発行で上書きされるので、トークンを再セット -->
<input type="hidden" name="_token" value="${_token}" />
