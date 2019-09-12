package com.t_systems.webstore.controller.restController;

import com.t_systems.webstore.model.dto.*;
import com.t_systems.webstore.model.entity.AbstractProduct;
import com.t_systems.webstore.model.entity.CatalogProduct;
import com.t_systems.webstore.service.FilesService;
import com.t_systems.webstore.service.ProductService;
import com.t_systems.webstore.validator.ImageValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final FilesService filesService;
    private final ImageValidator imageValidator;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }

        if (target.getClass() == CategoryDto.class) {
            dataBinder.setValidator(imageValidator);
            dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        }
        if (target.getClass() == ProductDto.class) {
            dataBinder.setValidator(imageValidator);
            dataBinder.setDisallowedFields("category", "tags", "ingredients");
        }
    }

    /**
     * get products
     * @param category category
     * @return list of products
     */
    @GetMapping("/getProducts/{category}")
    public List<ProductDto> getProductsByCategory(@PathVariable("category") String category){
        return productService.getProductDtosByCategory(category);
    }

    /**
     * get products with specified tags
     * @param category category
     * @param tags list of tags
     * @return list of products
     */
    @PostMapping("/getProductsWithTags/{category}")
    public List<ProductDto> getProductsWithTags(@PathVariable("category") String category,
                                                @RequestBody List<TagDto> tags){
        return productService.getProductDtosWithTags(category,tags);
    }

    /**
     * add catalog product to cart
     * @param product product name
     * @param session session
     * @return response entity
     */
    @PutMapping("/addToCart/{product}")
    public ResponseEntity<?> addToCart(@PathVariable("product") String product,
                                       HttpSession session){
        OrderDto orderDto = (OrderDto) session.getAttribute("order");
        ProductDto productDto = modelMapper.map(productService
                .getProduct(product, null),ProductDto.class);
        orderDto.getItems().add(productDto);
        session.setAttribute("order",orderDto);
        return new ResponseEntity<>("CatalogProduct added to cart!", HttpStatus.OK);
    }

    /**
     * add product to cart
     * @param product product name
     * @param session session
     * @param principal principal
     * @return response entity
     */
    @PostMapping("/addToCart/")
    public ResponseEntity<?> addToCart(@RequestBody ProductDto product,
                                       HttpSession session,
                                       Principal principal){
        OrderDto orderDto = (OrderDto) session.getAttribute("order");
        ProductDto productDto = modelMapper.map(productService.getProduct(product.getName(),
                product.getIsCustom()? principal.getName() : null),ProductDto.class);
        if(product.getIsCustom())
            productDto.setIsCustom(true);
        orderDto.getItems().add(productDto);
        session.setAttribute("order",orderDto);
        return new ResponseEntity<>("CatalogProduct added to cart!", HttpStatus.OK);
    }

    /**
     * remove product from cart
     * @param product product name
     * @param session session
     * @param principal principal
     * @return response entity
     */
    @PostMapping("/removeFromCart")
    public ResponseEntity<?> removeFromCart(@RequestBody ProductDto product,
                                            HttpSession session,
                                            Principal principal){
        OrderDto orderDto = (OrderDto) session.getAttribute("order");
        ProductDto productDto = modelMapper.map(productService.getProduct(product.getName(),
                product.getIsCustom()? principal.getName() : null),ProductDto.class);
        if(product.getIsCustom())
            productDto.setIsCustom(true);
        orderDto.getItems().remove(productDto);
        session.setAttribute("order",orderDto);
        return new ResponseEntity<>("CatalogProduct removed from cart!", HttpStatus.OK);
    }

    /**
     * delete all products of specified name
     * @param product product name
     * @param session session
     * @param principal principal
     * @return response entity
     */
    @PostMapping("/deleteAllFromCart")
    public ResponseEntity<?> deleteAllFromCart(@RequestBody ProductDto product,
                                               HttpSession session,
                                               Principal principal){
        OrderDto orderDto = (OrderDto) session.getAttribute("order");
        ProductDto productDto = modelMapper.map(productService.getProduct(product.getName(),
                product.getIsCustom()? principal.getName() : null),ProductDto.class);
        if(product.getIsCustom())
            productDto.setIsCustom(true);
        orderDto.getItems().removeIf(p->p.equals(productDto));
        session.setAttribute("order",orderDto);
        return new ResponseEntity<>("CatalogProduct deleted from cart!", HttpStatus.OK);
    }

    //Custom product page

    /**
     * get ingredients
     * @param category category
     * @return list of ingredients
     */
    @GetMapping("/customProduct/ingredients/{category}")
    public List<IngredientDto> getIngredients(@PathVariable("category") String category){
        return productService.getIngredientDtosByCategory(category);
    }

    /**
     * get custom products by user and category
     * @param category category
     * @param principal principal
     * @return list of products
     */
    @GetMapping("/customProduct/userProducts/{category}")
    public List<ProductDto> getUserProductsByCategory(@PathVariable("category") String category,
                                                      Principal principal){
        return productService.getProductDtosByCategoryAndUser(category, principal.getName());
    }

    /**
     * add custom product
     * @param productDto product
     * @param principal principal
     * @return response entity
     */
    @PostMapping("/customProduct/userProducts")
    public ResponseEntity<?> addUserProduct(@RequestBody ProductDto productDto, Principal principal){
        productService.setPrice(productDto);
        if (!productService.isCatalogProductOrCustomProductExists(productDto.getName(), principal.getName())) {
            productService.addCustomProduct(productDto, principal.getName());
            return new ResponseEntity<>("Product added!", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Product already exists in catalog!", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * add product to cart
     * @param product product name
     * @param session session
     * @param principal principal
     * @return response entity
     */
    @PutMapping("/customProduct/addToCart/{product}")
    public ResponseEntity<?> addToCart(@PathVariable("product") String product,
                          HttpSession session,
                          Principal principal){
        OrderDto order = (OrderDto)session.getAttribute("order");
        AbstractProduct clientProduct = productService.getProduct(product, principal.getName());
        ProductDto productDto = modelMapper.map(clientProduct, ProductDto.class);
        productDto.setIsCustom(true);
        order.getItems().add(productDto);
        session.setAttribute("order",order);
        return new ResponseEntity<>("Product added to cart!", HttpStatus.OK);
    }

    /**
     * remove custom product
     * @param product product name
     * @param principal principal
     * @param session session
     * @return response entity
     */
    @DeleteMapping("/customProduct/userProducts/{product}")
    public ResponseEntity<?> removeCustomProduct(@PathVariable("product") String product,
                                                 Principal principal,
                                                 HttpSession session){
        productService.detachOrRemoveProduct(product, principal.getName());
        productService.removeProductFromCart(session,product);
        return new ResponseEntity<>("Product deleted!",HttpStatus.OK);
    }

    //Admin page

    /**
     * get products
     * @param category category
     * @return list of products
     */
    @GetMapping("/admin/getProducts/{category}")
    public List<ProductDto> getProducts(@PathVariable("category") String category) {
        return productService.getProductDtosByCategory(category);
    }

    /**
     * add new category
     * @param category category name
     * @return response entity
     * @throws Exception ex
     */
    @PostMapping("/admin/addCategory")
    public ResponseEntity<?> addCategory(@ModelAttribute("categoryDto") @Valid CategoryDto category,
                                         BindingResult result) throws Exception{
            if(result.hasErrors()){
                return new ResponseEntity<>("Not an image!", HttpStatus.BAD_REQUEST);
            }
            String path = filesService.saveUploadedFiles(category.getFiles()).get(0);
            productService.addCategory(category, path);
            return new ResponseEntity<String>("Category added!", HttpStatus.OK);
    }

    /**
     * get categories
     * @return list of categories
     */
    @GetMapping("/admin/getCategories")
    public List<CategoryDto> getCategories() {
        return productService.getAllCategoryDtos();
    }

    /**
     * delete category
     * @param category category name
     * @return response entity
     */
    @DeleteMapping("/admin/deleteCategory/{category}")
    public ResponseEntity<?> deleteCategory(@PathVariable("category") String category) {
        if (!productService.tryToRemoveCategory(category))
            return new ResponseEntity<>("There are products in category", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Category deleted!", HttpStatus.OK);
    }

    /**
     * get ingredients
     * @return list of ingredients
     */
    @GetMapping("/admin/getIngredients")
    public List<IngredientDto> getAllIngredients() {
        return productService.getAllIngredientDtos();
    }

    /**
     * delete ingredient
     * @param name ingredient name
     * @return response entity
     */
    @DeleteMapping("/admin/deleteIngredient/{ingredient}")
    public ResponseEntity<?> deleteIngredient(@PathVariable("ingredient") String name) {
        productService.removeIngredient(name);
        return new ResponseEntity<>("Ingredient deleted!", HttpStatus.OK);
    }

    /**
     * add ingredient
     * @param ingredientDto ingredient
     * @return response entity
     */
    @PostMapping("/admin/addIngredient")
    public ResponseEntity<?> addIngredient(@RequestBody IngredientDto ingredientDto) {
        productService.addIngredient(ingredientDto);
        return new ResponseEntity<>("Ingredient added!", HttpStatus.OK);
    }

    /**
     * get tags
     * @return list of tags
     */
    @GetMapping("/admin/getTags")
    public List<TagDto> getTags() {
        return productService.getAllTagDtos();
    }

    /**
     * delete tag
     * @param name tag name
     * @return response entity
     */
    @DeleteMapping("/admin/deleteTag/{tag}")
    public ResponseEntity<?> deleteTag(@PathVariable("tag") String name) {
        productService.removeTag(name);
        return new ResponseEntity<>("Tag deleted!", HttpStatus.OK);
    }

    /**
     * add tag
     * @param tagDto tag
     * @return response entity
     */
    @PostMapping("/admin/addTag")
    public ResponseEntity<?> addTag(@RequestBody TagDto tagDto) {
        productService.addTag(tagDto);
        return new ResponseEntity<>("Tag added!", HttpStatus.OK);
    }

    /**
     * delete tag from product
     * @param category category
     * @param product product name
     * @param tag tag name
     * @return response entity
     */
    @DeleteMapping("/admin/deleteTag/{category}/{product}/{tag}")
    public ResponseEntity<?> deleteTagFromProduct(@PathVariable("category") String category,
                                                  @PathVariable("product") String product,
                                                  @PathVariable("tag") String tag) {
        productService.removeTagFromProduct(product, tag);
        return new ResponseEntity<>("Tag deleted!", HttpStatus.OK);
    }

    /**
     * get tyags by category
     * @param category category name
     * @return list of tags
     */
    @GetMapping("/admin/getCatTags/{category}")
    public List<TagDto> getTagsByCategory(@PathVariable("category") String category){
        return productService.getTagDtosByCategory(category);
    }

    /**
     * get ingredients by category
     * @param category category name
     * @return list of ingredients
     */
    @GetMapping("/admin/getCatIngs/{category}")
    public List<IngredientDto> getIngsByCategory(@PathVariable("category") String category){
        return productService.getIngredientDtosByCategory(category);
    }

    /**
     * delete ingredient from product
     * @param category category name
     * @param product product name
     * @param ingredient ingredient name
     * @return response entity
     */
    @DeleteMapping("/admin/deleteIngredient/{category}/{product}/{ingredient}")
    public ResponseEntity<?> deleteIngredientFromProduct(@PathVariable("category") String category,
                                                         @PathVariable("product") String product,
                                                         @PathVariable("ingredient") String ingredient) {
        productService.removeIngredientFromProduct(product, ingredient);
        return new ResponseEntity<>("Ingredient deleted!", HttpStatus.OK);
    }

    /**
     * add tag to product
     * @param category category name
     * @param product product name
     * @param tag tag name
     * @return response entity
     */
    @PutMapping("/admin/addTagToProduct/{category}/{product}/{tag}")
    public ResponseEntity<?> addTagToProduct(@PathVariable("category") String category,
                                             @PathVariable("product") String product,
                                             @PathVariable("tag") String tag) {
        if (((CatalogProduct) productService.getProduct(product, null)).getTags().contains(productService.getTag(tag)))
            return new ResponseEntity<>("Tag already exists!", HttpStatus.BAD_REQUEST);

        productService.addTagToProduct(product, tag);
        return new ResponseEntity<>("Tag added!", HttpStatus.OK);
    }

    /**
     * add ingredient to product
     * @param category category name
     * @param product product name
     * @param ingredient ingredient name
     * @return response entity
     */
    @PutMapping("/admin/addIngToProduct/{category}/{product}/{ingredient}")
    public ResponseEntity<?> addIngToProduct(@PathVariable("category") String category,
                                             @PathVariable("product") String product,
                                             @PathVariable("ingredient") String ingredient) {
        if (((CatalogProduct) productService.getProduct(product, null)).getIngredients().contains(productService.getIngredient(ingredient)))
            return new ResponseEntity<>("Ingredient already exists!", HttpStatus.BAD_REQUEST);

        productService.addIngToProduct(product, ingredient);
        return new ResponseEntity<>("Ingredient added!", HttpStatus.OK);
    }

    /**
     * update product
     * @param category category name
     * @param productName product name
     * @param productDto product dto
     * @return response entity
     * @throws Exception ex
     */
    @PostMapping("/admin/updateProduct/{category}/{product}")
    public ResponseEntity<?> updateProduct(@PathVariable("category") String category,
                                           @PathVariable("product") String productName,
                                           @ModelAttribute("productDto") @Valid ProductDto productDto,
                                           BindingResult result) throws Exception {
        if (result.hasErrors()){
            return new ResponseEntity<>("Not an image", HttpStatus.BAD_REQUEST);
        }
        productService.updateProduct(productName,productDto);
        return new ResponseEntity<>("CatalogProduct updated!", HttpStatus.OK);
    }

    /**
     * add product to category
     * @param category category name
     * @param productDto product dto
     * @return response entity
     * @throws Exception ex
     */
    @PostMapping("/admin/addProdcut/{category}")
    public ResponseEntity<?> addProduct(@PathVariable("category") String category,
                                        @ModelAttribute("productDto") @Valid ProductDto productDto) throws Exception{
        productService.addProduct(productDto,category);
        return new ResponseEntity<>("CatalogProduct added!", HttpStatus.OK);
    }

    /**
     * delete product
     * @param category category name
     * @param productName product name
     * @return response entity
     */
    @DeleteMapping ("/admin/deleteProduct/{category}/{product}")
    public ResponseEntity<?> deleteProduct(@PathVariable("category") String category,
                                           @PathVariable("product") String productName) {
        productService.detachOrRemoveProduct(productName,null);
        return new ResponseEntity<>("CatalogProduct removed!", HttpStatus.OK);
    }

    /**
     * get top product
     * @return list of products
     */
    @GetMapping("/admin/getTopProducts")
    public List<ProductDto> getTopProducts(){
        return productService.getTopProductsDtoForAdmin();
    }

    /**
     * get top products for external server
     * @return list of products
     */
    @GetMapping("/external/getTopProducts")
    public List<ProductDto> extGetTopProducts(){
        return productService.getTopProductDtos();
    }
}
