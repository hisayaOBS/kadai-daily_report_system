<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- レコード情報がリクエストにあれば詳細を表示し、なければエラー表示 -->

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${employee != null}">
            <h2>id:${employee.id} の従業員情報 詳細ページ</h2>
                <table>
                    <tr>
                        <th>社員番号</th>
                        <td>${employee.code}</td>
                    </tr>
                    <tr>
                        <th>氏名</th>
                        <td>${employee.name}</td>
                    </tr>
                    <tr>
                        <th>権限</th>
                        <td>
                            <c:choose>
                               <c:when test="${employee.admin_flag == 1}">管理者</c:when>
                               <c:otherwise>一般</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>登録日時</th>
                        <td><fmt:formatDate value="${employee.created_at}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                    </tr>
                    <tr>
                        <th>更新日時</th>
                        <td><fmt:formatDate value="${employee.created_at}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                    </tr>
                </table>

                <p><a href="<c:url value='/employees/edit?id=${employee.id}' />">編集・削除</a></p>
            </c:when>
            <c:otherwise>
            <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>
        <p><a href="<c:url value='/employees/index' />">一覧へ戻る</a></p>
    </c:param>
</c:import>
