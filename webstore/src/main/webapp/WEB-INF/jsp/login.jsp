<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="_header.jsp"></c:import>
<section>
    <form action="<c:url value="/login"></c:url>" method="post">
        <c:if test="${param.error != null}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span>&times;</span>
                </button>
                <span>Wrong login or password</span>
            </div>
        </c:if>
        <div class="input-group mb-2">
            <input name="username" type="text" class="form-control" placeholder="Login">
        </div>
        <div class="input-group mb-2">
            <input name="password" type="password" class="form-control" placeholder="Password">
        </div>
        <div class="input-group">
            <button type="submit" class="btn btn-primary">Sign in</button>
        </div>
        <input type="hidden"
               name="${_csrf.parameterName}"
               value="${_csrf.token}"/>
    </form>
</section>
<style>
    form {
        max-width: 350px;
        margin: 100px auto;
    }
</style>
<c:import url="_footer.jsp"></c:import>

