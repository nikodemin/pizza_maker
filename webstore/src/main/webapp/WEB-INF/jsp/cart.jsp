<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="_header.jsp"/>
<section id="app">
    <h2>Cart</h2>
    <table class="table">
        <tr class="thead-dark">
            <th>Name</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Change quantity</th>
        </tr>
        <tr v-for="tuple in products">
            <td class="productName">{{tuple.key.name}}</td>
            <td>{{calcPrice(tuple.key.price)}}$</td>
            <td>{{tuple.value}}</td>
            <td>
                <button @click="removeProduct" class="btn btn-primary">-</button>
                <button @click="addProduct" class="btn btn-primary">+</button>
                <button @click="deleteProduct" class="btn btn-danger">&times;</button>
            </td>
        </tr>
        <tr class="thead-dark">
            <th>Total</th>
        </tr>
        <tr>
            <td>{{calcPrice(total)}}$</td>
        </tr>
    </table>
    <div>
        <button :disabled="total == 0" @click="redirectToPayment" class="float-right btn btn-primary">To payment</button>
    </div>
</section>
<c:import url="_footer.jsp"/>
<script src="<c:url value="/resources/js/cart.js"/>"></script>

