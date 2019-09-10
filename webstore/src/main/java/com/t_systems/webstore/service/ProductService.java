package com.t_systems.webstore.service;

import com.t_systems.webstore.dao.*;
import com.t_systems.webstore.model.dto.*;
import com.t_systems.webstore.model.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j
public class ProductService {
    private static final int TOP_PRODUCTS_QUANTITY = 6;
    private static final long ADMIN_TOP_PRODUCTS_QUANTITY = 10;
    private final ProductDao productDao;
    private final IngredientDao ingredientDao;
    private final OrderDao orderDao;
    private final TagDao tagDao;
    private final CategoryDao categoryDao;
    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final FilesService filesService;
    private JavaMessageService javaMessageService;

    /**
     * resolver circular dependency by setting jms in JavaMessageService.init() method
     * @param javaMessageService jms instance
     */
    public void setJavaMessageService(JavaMessageService javaMessageService){
        this.javaMessageService = javaMessageService;
    }

    @PostConstruct
    private void init(){
        Converter<ProductDto,CustomProduct> toCustomProductConverter = new AbstractConverter<ProductDto, CustomProduct>() {
            @Override
            protected CustomProduct convert(ProductDto productDto) {

                CustomProduct product = new CustomProduct();
                product.setName(productDto.getName());
                product.setPrice(productDto.getPrice());

                product.setCategory(getCategory(productDto.getCategory().getName()));
                product.setIngredients(productDto.getIngredients().stream()
                        .map(i->getIngredient(i.getName())).collect(Collectors.toList()));
                return product;
            }
        };

        Converter<ProductDto,CatalogProduct> toCatalogProductConverter = new AbstractConverter<ProductDto, CatalogProduct>() {
            @Override
            protected CatalogProduct convert(ProductDto productDto) {
                CatalogProduct product = new CatalogProduct();
                product.setName(String.valueOf((new Date()).getTime()));

                if (productDto.getName() != null && productDto.getName().trim().length() != 0)
                            product.setName(productDto.getName());
                if (productDto.getPrice() != null)
                        product.setPrice(productDto.getPrice());

                List<String> image = null;
                try {
                    image = filesService.saveUploadedFiles(productDto.getFiles());
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }

                if (image.size() > 0) {
                        product.setImage(image.get(0));
                    }
                if (productDto.getSpicy() != null)
                        product.setSpicy(productDto.getSpicy());
                return product;
            }
        };

        modelMapper.addConverter(toCustomProductConverter);
        modelMapper.addConverter(toCatalogProductConverter);
    }

    /**
     * add custom product
     * @param product product
     */
    public void addCustomProduct(AbstractProduct product) {
        productDao.addProduct(product);
    }

    /**
     * get products by category
     * @param category category name
     * @return list of CatalogProduct
     */
    public List<CatalogProduct> getProductsByCategory(String category) {
        return productDao.getProductsByCat(category);
    }

    /**
     * get top products dto
     * @return list of ProductDto
     */
    public List<ProductDto> getTopProductsDto() {
        //get sorted products from last orders
        List<CatalogProduct> sortedProducts = orderDao.getRecentOrders().stream()
                .flatMap(o -> o.getItems().stream())
                .filter(p->p.getCategory() != null && p instanceof CatalogProduct)
                .sorted((p1, p2) -> p1.getId().compareTo(p2.getId()))
                .map(p->(CatalogProduct)p)
                .collect(Collectors.toList());

        //map of product and it quantity
        Map<CatalogProduct, Integer> map = new HashMap<>();
        sortedProducts.forEach(p -> {
            if (map.containsKey(p))
                map.put(p, map.get(p) + 1);
            else
                map.put(p, 1);
        });

        //get top products related to it quantity
        return map.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(TOP_PRODUCTS_QUANTITY).map(e -> modelMapper.map(e.getKey(),ProductDto.class)).collect(Collectors.toList());
    }

    /**
     * add ingredient
     * @param ingredient Ingredient
     */
    public void addIngredient(Ingredient ingredient) {
        ingredientDao.addIngredient(ingredient);
    }

    /**
     * get all ingredients dto
     * @return list of IngredientDto
     */
    public List<IngredientDto> getAllIngredientDtos() {
        return ingredientDao.getAllIngredients().stream().map(e -> modelMapper.map(e,IngredientDto.class))
                .collect(Collectors.toList());
    }

    /**
     * remove ingredient
     * @param name ingredient name
     */
    public void removeIngredient(String name) {
        ingredientDao.removeIngredient(name);
    }

    /**
     * get ingredient
     * @param name ingredient name
     * @return Ingredient
     */
    public Ingredient getIngredient(String name) {
        return ingredientDao.getIngredient(name);
    }

    /**
     * add tag
     * @param tag Tag
     */
    public void addTag(Tag tag) {
        tagDao.addTag(tag);
    }

    /**
     * get all tags
     * @return list of Tag
     */
    public List<Tag> getAllTags() {
        return tagDao.getAllTags();
    }

    /**
     * get tag dtos by category
     * @param category category name
     * @return list of TagDto
     */
    public List<TagDto> getTagDtosByCategory(String category) {
        return tagDao.getTagsByCategory(category).stream()
                .map(t->modelMapper.map(t, TagDto.class)).collect(Collectors.toList());
    }

    /**
     * remove tag
     * @param name tag name
     */
    public void removeTag(String name) {
        tagDao.removeTag(name);
    }

    /**
     * get tag
     * @param name tag name
     * @return Tag
     */
    public Tag getTag(String name) {
        return tagDao.getTag(name);
    }

    /**
     * add category
     * @param category category name
     */
    public void addCategory(Category category) {
        categoryDao.addCategory(category);
    }

    /**
     * get all category dtos
     * @return list of CategoryDto
     */
    public List<CategoryDto> getAllCategoryDtos() {
        return categoryDao.getAllCategories().stream()
                .map(c->modelMapper.map(c,CategoryDto.class)).collect(Collectors.toList());
    }

    /**
     * get category
     * @param name category name
     * @return Category
     */
    public Category getCategory(String name) {
        return categoryDao.getCategory(name);
    }

    /**
     * add ingredient to product
     * @param productName product name
     * @param ingredient ingredient name
     */
    public void addIngToProduct(String productName, String ingredient) {
        CatalogProduct product = ((CatalogProduct) productDao.getProduct(productName, null));
        productDao.addIngToProduct(product, ingredientDao.getIngredient(ingredient));
    }

    /**
     * detach and try to remove product
     * @param productName product name
     * @param username username
     */
    public void detachOrRemoveProduct(String productName, String username) {
        AbstractProduct product = productDao.getProduct(productName,
                username==null? null : userDao.getUser(username));
        productDao.detachProduct(product);
        if (!orderDao.isProductInOrder(product)){
            productDao.removeProduct(product);
        }
    }

    /**
     * remove category
     * @param name category name
     */
    public void removeCategory(String name) {
        Category category = categoryDao.getCategory(name);
        tagDao.removeCategory(category);
        ingredientDao.removeCategory(category);
        categoryDao.removeCategory(name);
    }

    /**
     * remove ingredient from product
     * @param productName product name
     * @param ingredient ingredient name
     */
    public void removeIngredientFromProduct(String productName, String ingredient) {
        CatalogProduct product = ((CatalogProduct) productDao.getProduct(productName, null));
        productDao.removeIngredientFromProduct(product, ingredientDao.getIngredient(ingredient));
    }

    /**
     * remove tag drom product
     * @param productName product name
     * @param tag tag name
     */
    public void removeTagFromProduct(String productName, String tag) {
        CatalogProduct product = ((CatalogProduct) productDao.getProduct(productName, null));
        productDao.removeTagFromProduct(product, tagDao.getTag(tag));
    }

    /**
     * add tag to product
     * @param productName product name
     * @param tag tag name
     */
    public void addTagToProduct(String productName, String tag) {
        CatalogProduct product = ((CatalogProduct) productDao.getProduct(productName, null));
        productDao.addTagToProduct(product, tagDao.getTag(tag));
    }

    /**
     * get product
     * @param name product name
     * @param username username
     * @return product
     */
    public AbstractProduct getProduct(String name, String username) {
        return productDao.getProduct(name, userDao.getUser(username));
    }

    /**
     * get ingredient dtos by category
     * @param category category name
     * @return list of ingredients
     */
    public List<IngredientDto> getIngredientDtosByCategory(String category) {
        return ingredientDao.getIngredientsByCategory(categoryDao.getCategory(category)).stream()
                .map(i->modelMapper.map(i,IngredientDto.class)).collect(Collectors.toList());
    }

    /**
     * get custom product dtos by category
     * @param category category name
     * @param username username
     * @return list of products
     */
    public List<ProductDto> getProductDtosByCategoryAndUser(String category, String username) {
        return productDao.getProductsByCatAndUser(categoryDao.getCategory(category), userDao.getUser(username))
                .stream().map(p->{
                    ProductDto productDto = modelMapper.map(p,ProductDto.class);
                    productDto.setIsCustom(true);
                    return productDto;
                }).collect(Collectors.toList());
    }

    /**
     * get product dtos with specified tags
     * @param category category name
     * @param tags list of tag dtos
     * @return list of products
     */
    public List<ProductDto> getProductDtosWithTags(String category, List<TagDto> tags) {
        return getProductsByCategory(category).stream()
                .filter(product->{
                    boolean res = true;
                    for (TagDto tag:tags) {
                        res &= product.getTags().stream()
                                .filter(catTag->catTag.getName().equals(tag.getName()))
                                .count() > 0;
                    }
                    return res;
                })
                .map(p->modelMapper.map(p,ProductDto.class)).collect(Collectors.toList());
    }

    /**
     * get product dtos by category
     * @param category categoory name
     * @return list of products
     */
    public List<ProductDto> getProductDtosByCategory(String category) {
        return getProductsByCategory(category).stream()
                .map(p->modelMapper.map(p,ProductDto.class)).collect(Collectors.toList());
    }

    /**
     * set price to product dto
     * @param productDto product
     */
    public void setPrice(ProductDto productDto) {
        Integer total = 0;
        for (IngredientDto i:productDto.getIngredients()) {
            total += ingredientDao.getIngredient(i.getName()).getPrice();
        }
        productDto.setPrice(total);
    }

    /**
     * get all tag dtos
     * @return list of tags
     */
    public List<TagDto> getAllTagDtos() {
        return getAllTags().stream().map(t -> modelMapper.map(t, TagDto.class))
                .collect(Collectors.toList());
    }

    /**
     * get top product dtos with custom products
     * @return list of products
     */
    public List<ProductDto> getTopProductsDtoForAdmin() {
        //get sorted products from last orders
        List<AbstractProduct> sortedProducts = orderDao.getRecentOrders().stream()
                .flatMap(o -> o.getItems().stream())
                .filter(p->p.getCategory() != null)
                .sorted((p1, p2) -> p1.getId().compareTo(p2.getId()))
                .collect(Collectors.toList());

        //map of product and it quantity
        Map<AbstractProduct, Integer> map = new HashMap<>();
        sortedProducts.forEach(p -> {
            if (map.containsKey(p))
                map.put(p, map.get(p) + 1);
            else
                map.put(p, 1);
        });

        //get top products related to it quantity
        return map.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(ADMIN_TOP_PRODUCTS_QUANTITY).map(e -> {
                    ProductDto productDto = modelMapper.map(e.getKey(),ProductDto.class);
                    if (e.getKey() instanceof CustomProduct) {
                        productDto.setIsCustom(true);
                    }
                    return productDto;
                }).collect(Collectors.toList());
    }

    /**
     * is custom or catalog product exists
     * @param name product name
     * @param username username
     * @return boolean: exists or not
     */
    public boolean isCatalogProductOrCustomProductExists(String name, String username) {
        return productDao.isCatalogProductExists(name) ||
                productDao.isCustomProductExists(name,userDao.getUser(username));
    }

    /**
     * remove product from cart
     * @param session session
     * @param product product name
     */
    public void removeProductFromCart(HttpSession session, String product) {
        OrderDto order = (OrderDto)session.getAttribute("order");
        List<ProductDto> products = order.getItems().stream()
                .filter(p -> !p.getName().equals(product)).collect(Collectors.toList());
        order.setItems(products);
        session.setAttribute("order",order);
    }

    /**
     * add custom product
     * @param productDto product dto
     * @param username username
     */
    public void addCustomProduct(ProductDto productDto, String username) {
        CustomProduct product = modelMapper.map(productDto,CustomProduct.class);
        product.setAuthor(userDao.getUser(username));
        productDao.addProduct(product);
    }

    /**
     * add category
     * @param categoryDto category dto
     * @param path path to image
     */
    public void addCategory(CategoryDto categoryDto, String path) {
        Category category = modelMapper.map(categoryDto,Category.class);
        category.setImage(path);
        categoryDao.addCategory(category);
    }

    /**
     * add ingredient
     * @param ingredientDto ingredient dto
     */
    public void addIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = modelMapper.map(ingredientDto, Ingredient.class);
        ingredient.setCategories(ingredientDto.getCategories().stream()
                .map(this::getCategory).collect(Collectors.toList()));
        ingredientDao.addIngredient(ingredient);
    }

    /**
     * add tag
     * @param tagDto tag dto
     */
    public void addTag(TagDto tagDto) {
        List<Category> categories = tagDto.getCategories().stream()
                .map(this::getCategory).collect(Collectors.toList());
        Tag tag = new Tag();
        tag.setCategories(categories);
        tag.setName(tagDto.getName());
        tagDao.addTag(tag);
    }

    /**
     * add catalog product
     * @param productDto product dto
     * @param category category name
     */
    public void addProduct(ProductDto productDto, String category) {
        CatalogProduct product = modelMapper.map(productDto,CatalogProduct.class);
        product.setCategory(getCategory(category));
        productDao.addProduct(product);
    }

    /**
     * update existing product
     * @param productName product name
     * @param productDto product dto
     * @throws IOException ex
     */
    public void updateProduct(String productName, ProductDto productDto) throws IOException {
        CatalogProduct product = ((CatalogProduct) getProduct(productName, null));

        if (productDto.getName() != null && productDto.getName().trim().length() != 0) {
            product.setName(productDto.getName());
        }
        if (productDto.getPrice() != null) {
            product.setPrice(productDto.getPrice());
        }

        List<String> image = filesService.saveUploadedFiles(productDto.getFiles());

        if (image.size() > 0) {
            product.setImage(image.get(0));
        }
        if (productDto.getSpicy() != null) {
            product.setSpicy(productDto.getSpicy());
        }

        productDao.updateProduct(product);
        javaMessageService.sendTopProducts();
    }
}
