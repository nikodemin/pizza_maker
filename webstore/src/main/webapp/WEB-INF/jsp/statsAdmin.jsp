<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="_header.jsp"></c:import>
<section id="app">
    <a class="btn btn-primary" href="<c:url value="/admin"/>">Back to products</a>
   <table class="table table-striped">
        <tr class="thead-dark">
            <th>Username</th>
            <th>Total</th>
            <th>Payment method</th>
            <th>Delivery method</th>
            <th>Date</th>
            <th>Status</th>
            <th>Change status</th>
            <th>Items</th>
        </tr>
        <tr v-for="order in orders" :data-id="order.id">
            <td>{{order.username}}</td>
            <td>{{calcPrice(order.price)}}</td>
            <td>{{order.paymentMethod}}</td>
            <td>{{order.deliveryMethod}}</td>
            <td>{{order.date}}</td>
            <td>{{order.status}}</td>
            <td>
                <select @change="changeStatus">
                    <c:forEach items="${statusList}" var="status">
                        <option>${status}</option>
                    </c:forEach>
                </select>
            </td>
            <td><ul><li v-for="item in order.items">{{item.name}}</li></ul></td>
        </tr>
        <tr class="thead-dark">
            <th>Total for month</th>
            <th>Total for week</th>
        </tr>
        <tr>
            <td>{{calcPrice(total.month)}}</td>
            <td>{{calcPrice(total.week)}}</td>
        </tr>
        <tr class="thead-dark">
            <th><h3>Top clients</h3></th>
        </tr>
        <tr class="thead-dark">
            <th>First name</th>
            <th>Last name</th>
            <th>Username</th>
            <th>Email</th>
        </tr>
       <tr v-for="client in topClients">
           <td>{{client.firstName}}</td>
           <td>{{client.lastName}}</td>
           <td>{{client.username}}</td>
           <td>{{client.email}}</td>
       </tr>
   </table>
</section>
<style>

</style>
<c:import url="_footer.jsp"></c:import>
<script src="<c:url value="/resources/js/statsAdmin.js"/>"></script>

