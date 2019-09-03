<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="_header.jsp"></c:import>
<section id="app">
    <h4>Show products:</h4>
    <div>
        <button v-for="cat in categories" @click="getProductsByClick" class="btn btn-primary catBtn mt-1 ml-3">{{cat.name}}
        </button>
        <button @click="getTopProducts" class="btn btn-success ml-3 mt-1">Top products</button>
        <a href="<c:url value="/admin/stats"/>" class="btn btn-success ml-3 mt-1">Stats</a>
    </div>
    <h4>Delete category:</h4>
    <div>
        <button v-for="cat in categories" @click="deleteCat" class="btn btn-primary btn-danger catBtn mt-1 ml-3">
            {{cat.name}}
        </button>
    </div>
    <h4>Add new category</h4>
    <form:form id="addCatForm" modelAttribute="categoryDto" enctype="multipart/form-data">
        <table>
            <tr>
                <td><form:input path="name" type="text" placeholder="Category name"/></td>
                <td><form:input path="files" type="file" lang="en"/></td>
                <td>
                    <button type="button" @click="addCategory" class="btn btn-primary">Add</button>
                </td>
            </tr>
        </table>
    </form:form>
    <h4>Delete ingredient:</h4>
    <div>
        <button v-for="ing in ingredients" @click="deleteIng" class="btn btn-primary btn-danger catBtn mt-1 ml-3">
            {{ing.name}}
        </button>
    </div>
    <h4>Add Ingredient:</h4>
    <form:form id="addIngForm" modelAttribute="ingredientDto" enctype="multipart/form-data">
        <table>
            <tr>
                <td><form:input class="name" path="name" type="text" placeholder="Name"/></td>
                <td><form:input class="price" path="price" type="number" placeholder="Price"/></td>
                <td>
                    <button type="button" @click="addIng" class="btn btn-primary">Add</button>
                </td>
            </tr>
        </table>
    </form:form>
    <h4>To Categories:</h4>
    <div>
        <button v-for="cat in categories" class="btn btn-outline-warning ingCatToAddBtn mt-1 ml-3" data-toggle="button" aria-pressed="false">
            {{cat.name}}
        </button>
    </div>
    <h4>Delete Tags:</h4>
    <div>
        <button v-for="tag in tags" @click="deleteTag" class="btn btn-primary btn-danger catBtn mt-1 ml-3">{{tag.name}}
        </button>
    </div>
    <h4>Add tag:</h4>
    <form:form id="addTagForm" modelAttribute="tagDto" enctype="multipart/form-data">
        <table>
            <tr>
                <td><form:input path="name" type="text" placeholder="Name"/></td>
                <td>
                    <button type="button" @click="addTag" class="btn btn-primary">Add</button>
                </td>
            </tr>
        </table>
    </form:form>
    <h4>To Categories:</h4>
    <div>
        <button v-for="cat in categories" class="btn btn-outline-warning catToAddBtn mt-1 ml-3" data-toggle="button" aria-pressed="false">
            {{cat.name}}
        </button>
    </div>
    <h4>Products:</h4>
    <div class="products">
        <div v-for="product in products" class="product">
            <img v-if="product.isCustom == false" :src="getImgUrl(product.image)">
            <h5 class="name">{{product.name}}</h5>
            <h5>{{calcPrice(product.price)}}$</h5>
            <h5 v-if="product.isCustom == false">Spicy:{{product.spicy}}</h5>
            <h5 v-if="product.isCustom == false">Tags:
                <div v-for="tag in product.tags" :data-name="tag.name">{{tag.name}}
                    <button  @click="deleteTagFromProduct" class="btn btn-primary btn-danger">&times;</button>
                </div>
            </h5>
            <h5>Ingredients:
                <div v-for="ingredient in product.ingredients" :data-name="ingredient.name">{{ingredient.name}}
                    <button v-if="product.isCustom == false" @click="deleteIngredientFromProduct" class="btn btn-primary btn-danger">&times;</button>
                </div>
            </h5>
            <button v-if="product.isCustom == false" @click="editProduct" class="btn btn-primary">Edit</button>
        </div>
        <div class="product">
            <button @click="addProduct" class="btn btn-primary" style="margin: auto;">Add Product</button>
        </div>
    </div>
    <div id="shadow"></div>
    <div id="dialog">
        <a class="close">&times</a>
        <form:form id="editProductForm" modelAttribute="productDto" enctype="multipart/form-data">
            <div class="wrapper">
                <div>
                    <form:input path="name" type="text" placeholder="Rename"></form:input>
                </div>
                <div>
                    <form:input path="price" type="number" placeholder="Change price"></form:input>
                </div>
                <div>
                    <form:input cssStyle="width: 205px" path="spicy" type="number" min="0" max="3"
                                placeholder="Change spicy"></form:input>
                </div>
                <div>
                    <label for="filesEditProduct">Change image:</label>
                </div>
                <div>
                    <form:input id="filesEditProduct" path="files" type="file"></form:input>
                </div>
                <div>Add tags:</div>
                <div>
                    <button type="button" v-for="tag in categoryTags" @click="addTagToProduct"
                            class="btn btn-primary catBtn mt-1 ml-3">{{tag.name}}
                    </button>
                </div>
                <div>Add ingredients:</div>
                <div>
                    <button type="button" v-for="ing in categoryIngs" @click="addIngToProduct"
                            class="btn btn-primary catBtn mt-1 ml-3">{{ing.name}}
                    </button>
                </div>
                <div>
                    <button type="button" @click="submitProduct" class="btn btn-primary">Submit</button>
                    <button type="button" @click="deleteProduct" class="btn d-inline-block btn-primary btn-danger">
                        Delete product
                    </button>
                </div>
            </div>
        </form:form>
    </div>
</section>
<c:import url="_footer.jsp"></c:import>
<script src="<c:url value="/resources/js/admin.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/admin.css"/>">