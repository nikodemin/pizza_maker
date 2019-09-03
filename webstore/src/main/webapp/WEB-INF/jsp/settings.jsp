<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="_header.jsp"></c:import>
<section>
    <c:url value="/changeSettings" var="postUrl"/>
    <form:form modelAttribute="user" method="post" action="${postUrl}">
        <h4>Settings</h4>
        <table>
            <tr>
                <td>
                    <form:input type="email" class="form-control form-control-sm" placeholder="Email" path="email"/>
                </td>
                <td>
                    <form:errors path="email" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:input type="text" class="form-control form-control-sm" placeholder="Username"
                                path="username"/>
                </td>
                <td>
                    <form:errors path="username" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:input type="password" class="form-control form-control-sm" placeholder="Password"
                                path="password"/>
                </td>
                <td>
                    <form:errors path="password" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:input type="text" class="form-control form-control-sm" placeholder="First name"
                                path="firstName"/>
                </td>
                <td>
                    <form:errors path="firstName" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:input type="text" class="form-control form-control-sm" placeholder="Last name"
                                path="lastName"/>
                </td>
                <td>
                    <form:errors path="lastName" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:input type="date" class="form-control form-control-sm" placeholder="Date of birth"
                                path="dateOfBirth"/>
                </td>
                <td>
                    <form:errors path="dateOfBirth" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <h5>For delivery:</h5>
                </td>
            </tr>
            <tr>
                <td>
                    <form:input type="text" class="form-control form-control-sm" placeholder="Country" path="country"/>
                </td>
                <td>
                    <form:errors path="country" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:input type="text" class="form-control form-control-sm" placeholder="City" path="city"/>
                </td>
                <td>
                    <form:errors path="city" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:input type="text" class="form-control form-control-sm" placeholder="Street" path="street"/>
                </td>
                <td>
                    <form:errors path="street" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:input type="text" class="form-control form-control-sm" placeholder="House number"
                                path="house"/>
                </td>
                <td>
                    <form:errors path="flat" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:input type="text" class="form-control form-control-sm" placeholder="Flat number" path="flat"/>
                </td>
                <td>
                    <form:errors path="flat" cssClass="alert alert-danger" element="div"/>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="hidden"
                           name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" class="btn btn-primary">
                </td>
            </tr>
        </table>
    </form:form>
</section>
<style>
    form {
        max-width: 350px;
        margin: 100px auto;
    }
</style>
<c:import url="_footer.jsp"></c:import>

