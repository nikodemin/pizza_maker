<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="_header.jsp"></c:import>
<section id="main">
    <h4><c:out value="${text}"/></h4>
</section>
<style>
    #main{
        height: 400px;
        text-align: center;
    }
    #main h4{
        line-height: 400px;
    }
</style>
<c:import url="_footer.jsp"></c:import>

