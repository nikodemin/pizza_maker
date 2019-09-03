<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>PizzaMaker</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/common.css"/>">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<body>
<section id="topMenu">
    <nav class="navbar navbar-light fixed-top px-4 bg-light">
        <a class="navbar-brand" href="<c:url value="/"/>">
            <img src="<c:url value="/resources/img/logo.png"/>" id="logo">
            <h3>Pizza Maker</h3>
        </a>
        <ul class="list-unstyled inline-list float-right d-none d-md-block">
            <li class="list-inline-item">
                <a href="<c:url value="/#menu"/>">
                    <i class="fas fa-book-open"></i> Menu
                </a>
            </li>
            <c:if test="${sessionScope.username == null}">
                <li class="list-inline-item">
                    <a href="<c:url value="/login"></c:url>">
                        <i class="fas fa-user"></i> Sign in
                    </a>
                </li>
                <li class="list-inline-item">
                    <a href="<c:url value="/register"></c:url>">
                        <i class="fas fa-user-plus"></i> Sign up
                    </a>
                </li>
            </c:if>
            <c:if test="${sessionScope.username != null}">
                <li class="list-inline-item">
                    <a href="<c:url value="/settings"></c:url>">
                        <i class="fas fa-cog"></i> Settings
                    </a>
                </li>
                <li class="list-inline-item">
                    <a href="<c:url value="/logout"></c:url>">
                        <i class="fas fa-sign-out-alt"></i> Sign out
                    </a>
                </li>
                <li class="list-inline-item">
                    <a href="<c:url value="/orders"></c:url>">
                        <i class="fas fa-folder"></i> My orders
                    </a>
                </li>
            </c:if>
            <li class="list-inline-item">
                <a href="<c:url value="/cart"/>">
                    <i class="fas fa-shopping-cart"></i> Cart
                </a>
            </li>
        </ul>
        <div class="dropdown d-md-none">
            <button class="btn btn-secondary dropdown-toggle" type="button"
                    id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true"
                    aria-expanded="false">
                Menu
            </button>
            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
                <a class="dropdown-item" href="<c:url value="/#menu"/>"><i class="fas fa-book-open"></i> Menu</a>
                <c:if test="${sessionScope.username == null}">
                    <a class="dropdown-item" href="<c:url value="/login"/>"><i class="fas fa-user"></i> Sign in</a>
                    <a class="dropdown-item" href="<c:url value="/register"/>"><i class="fas fa-user-plus"></i> Sign up</a>
                </c:if>
                <c:if test="${sessionScope.username != null}">
                    <a class="dropdown-item" href="<c:url value="/settings"/>"><i class="fas fa-cog"></i> Settings</a>
                    <a class="dropdown-item" href="<c:url value="/logout"/>"><i class="fas fa-sign-out-alt"></i> Sign out</a>
                    <a class="dropdown-item" href="<c:url value="/orders"/>"><i class="fas fa-folder"></i> My orders</a>
                </c:if>
                <a class="dropdown-item" href="<c:url value="/cart"/>"><i class="fas fa-shopping-cart"></i> Cart</a>
            </div>
        </div>
    </nav>
</section>
