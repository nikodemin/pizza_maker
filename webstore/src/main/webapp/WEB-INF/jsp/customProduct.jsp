<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="_header.jsp"></c:import>
<section id="app">
    <h3 class="center my-2">Create your own product!</h3>
    <h4 class="center my-2">New design for <select v-model="category" v-on:change="categoryChanged" class="category">
        <option>Pizza</option>
        <option>Burgers</option>
    </select></h4>
    <div class="float-left col-md-3 center">
        <h4>Your masterpieces:</h4>
        <div v-for="product in products" class="product">
            <span class="name">{{product.name}} {{calcPrice(product.price)}}$</span>
            <button class="btn btn-warning" @click="addToCart"><i class="fas fa-shopping-cart"></i></button>
            <button class="btn btn-danger" @click="deleteProduct">&times;</button>
        </div>
    </div>
    <div class="float-right col-md-9">
        <div class="ingredients">
            <h4>Ingredients:</h4><button v-for="ing in ingredients" @click="addIngredient" class="btn btn-primary ml-3 mt-1">{{ing.name}} {{calcPrice(ing.price)}}$</button>
        </div>
        <div v-if="error!=null" class="alert alert-danger">{{error}}</div>
        <div class="nameInput mt-3">
            <input v-model="name" placeholder="Enter name for your creation" class="form-control">
            <p>You will create "{{name}}" with: <span v-for="ing in chosenIngs"><span class="name">{{ing.name}} </span><button @click="deleteIngredient" class="btn btn-danger">&times;</button></span> that will cost <strong>{{calcPrice(total)}}$</strong></p>
            <button @click="submit" class="btn btn-primary mt-2">Add</button>
        </div>
    </div>
</section>
<c:import url="_footer.jsp"></c:import>
<script src="<c:url value="/resources/js/customProduct.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/customProduct.css"/>">

