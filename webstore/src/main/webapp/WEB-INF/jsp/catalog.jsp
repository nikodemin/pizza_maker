<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="_header.jsp"/>
<section id="app">
    <div class="tags d-block my-4 ml-2">
        <c:forEach var="tag" items="${tags}">
            <button @click="filterTags" class="btn btn-outline-warning filterTagsBtn" data-toggle="button" aria-pressed="false">
                <i class="fas fa-plus-circle"></i> ${tag.name}
            </button>
        </c:forEach>
    </div>
    <div class="products">
        <div v-for="product in products" class="productItem card">
            <div class="hot">
                <i v-for="hot in product.spicy" class="fas fa-pepper-hot"></i>
            </div>
            <img class="card-img-top" :src="getImgUrl(product.image)"/>
            <div class="card-body">
                <h4 class="card-title productName">{{product.name}}</h4>
                <h5>{{calcPrice(product.price)}} $</h5>
                <p class="card-text">
                    <template v-for="ingredient in product.subListIngredients">
                        {{ingredient.name}},
                    </template>
                    <template>{{product.lastIngredient.name}}</template>
                </p>
                <button @click="addToCart" class="btn btn-primary btn-warning addToCart">
                    <i class="fas fa-shopping-cart"></i>Add to cart
                </button>
            </div>
        </div>
    </div>
</section>
<c:import url="_footer.jsp"/>
<script src="<c:url value="/resources/js/catalog.js"/>"></script>

