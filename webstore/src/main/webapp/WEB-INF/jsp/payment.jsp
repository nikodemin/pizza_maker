<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="_header.jsp"/>
<section id="app">
    <h2>Payment</h2>
    <div v-if="cardEmpty==true" class="alert alert-danger">Cart is empty!</div>
    <div>
        <h5>Choose delivery method</h5>
        <input type="radio" value="courier" v-model="delivery" checked>
        <label>Courier</label>
        <input type="radio" value="pickUp" v-model="delivery">
        <label>Pick up</label>
    </div>
    <div v-if="addressError==true" class="alert alert-danger">Not valid address data</div>
    <div v-if="delivery=='courier'">
        <form:form id="deliveryForm" modelAttribute="address">
            <form:input path="country" type="text" required="true" placeholder="Country" cssClass="form-control"/>
            <form:input path="city" type="text" required="true" placeholder="City" cssClass="form-control"/>
            <form:input path="street" type="text" required="true" placeholder="Street" cssClass="form-control"/>
            <form:input path="house" type="text" required="true" placeholder="House" cssClass="form-control"/>
            <form:input path="flat" type="text" required="true" placeholder="Flat" cssClass="form-control"/>
        </form:form>
    </div>
    <div v-else>
        <p>Our address is USA, New-York 69 AVE. South</p>
    </div>
    <div>
        <h5>Choose payment method</h5>
        <input type="radio" value="cash" v-model="payment" checked>
        <label>Cash</label>
        <input type="radio" value="card" v-model="payment">
        <label>Card</label>
    </div>
    <div v-if="cardError==true" class="alert alert-danger">Not valid card data</div>
    <div v-if="payment=='card'">
        <form:form id="cardForm" modelAttribute="card">
            <form:input path="cardNumber" type="text" required="true" placeholder="Card number" cssClass="form-control"/>
            <form:input path="month" type="text" required="true" placeholder="Month" cssClass="form-control"/>
            <form:input path="year" type="text" required="true" placeholder="Year" cssClass="form-control"/>
            <form:input path="cvv" type="text" required="true" placeholder="cvv" cssClass="form-control"/>
        </form:form>
    </div>
    <div>
        <button class="btn btn-primary" @click="submitOrder">Submit</button>
    </div>
</section>
<style>
    #app{
        max-width: 500px;
        margin: auto;
    }
</style>
<c:import url="_footer.jsp"/>
<script src="<c:url value="/resources/js/cart.js"/>"></script>

