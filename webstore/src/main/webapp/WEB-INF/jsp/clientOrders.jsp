<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="_header.jsp"></c:import>
<section>
    <table class="table table-striped">
        <tr class="thead-dark">
            <th>Date</th>
            <th>Status</th>
            <th>Delivery method</th>
            <th>Payment method</th>
            <th>Items</th>
            <th>Total</th>
        </tr>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>${order.date}</td>
                <td>${order.status}</td>
                <td>${order.deliveryMethod}</td>
                <td>${order.paymentMethod}</td>
                <td>
                    <ul>
                        <c:forEach var="item" items="${order.uniqueProducts}">
                            <li>${item.key.name} X ${item.value}</li>
                        </c:forEach>
                    </ul>
                </td>
                <td>${order.price/100}</td>
            </tr>
        </c:forEach>
    </table>
</section>
<c:import url="_footer.jsp"></c:import>

