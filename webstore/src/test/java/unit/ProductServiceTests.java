package unit;

import com.t_systems.webstore.dao.*;
import com.t_systems.webstore.model.dto.IngredientDto;
import com.t_systems.webstore.model.dto.ProductDto;
import com.t_systems.webstore.model.dto.TagDto;
import com.t_systems.webstore.model.entity.*;
import com.t_systems.webstore.service.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ProductServiceTests {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;
    @Mock
    private IngredientDao ingredientDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private TagDao tagDao;
    @Mock
    private CategoryDao categoryDao;
    @Mock
    private UserDao userDao;
    @Mock
    private ModelMapper modelMapper;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
/*
    todo

    @Test
    public void getTopProductsDtoTest(){
        productService.setMappingService(mappingService);
        List<_Order> orders = new ArrayList<>();

        AbstractProduct product = new CatalogProduct();
        product.setCategory(new Category());
        product.setId(1L);
        ((CatalogProduct) product).setName("name");
        AbstractProduct product2 = new CatalogProduct();
        product2.setCategory(new Category());
        product2.setId(2L);
        ((CatalogProduct) product2).setName("name2");
        AbstractProduct uncountedProduct = new CustomProduct();
        uncountedProduct.setCategory(new Category());
        uncountedProduct.setId(3L);

        for (int i = 0; i < 10; i++) {
            _Order order = new _Order();
            order.setItems(Arrays.asList(product,product2, uncountedProduct));
            orders.add(order);
        }

        _Order uncountedOrder = new _Order();
        AbstractProduct uncountedProduct2 = new CatalogProduct();
        uncountedProduct2.setId(4L);
        uncountedOrder.setItems(Arrays.asList(uncountedProduct2,uncountedProduct2,uncountedProduct2));
        orders.add(uncountedOrder);

        when(orderDao.getRecentOrders()).thenReturn(orders);

        productService.getTopProductsDto();
        ArgumentCaptor<AbstractProduct> abstractProductArgumentCaptor =
                ArgumentCaptor.forClass(AbstractProduct.class);
        verify(mappingService,times(2))
                .toProductDto(abstractProductArgumentCaptor.capture());
        List<AbstractProduct> products = abstractProductArgumentCaptor.getAllValues();
        Assert.assertEquals(true, products.contains(product));
        Assert.assertEquals(true, products.contains(product2));
        Assert.assertEquals(false, products.contains(uncountedProduct));
        Assert.assertEquals(false, products.contains(uncountedProduct2));
    }

    @Test
    public void getProductDtosWithTagsTest(){
        productService.setMappingService(mappingService);
        String category = "category";
        Tag tag = new Tag();
        tag.setName("tag");
        Tag tag2 = new Tag();
        tag2.setName("tag2");
        Tag tag3 = new Tag();
        tag3.setName("tag3");

        CatalogProduct product = new CatalogProduct();
        product.setTags(Arrays.asList(tag,tag2));
        CatalogProduct product2 = new CatalogProduct();
        product2.setTags(Arrays.asList(tag));
        CatalogProduct product3 = new CatalogProduct();
        product3.setTags(Arrays.asList(tag,tag3));

        when(productService.getProductsByCategory(category))
                .thenReturn(Arrays.asList(product,product2,product3));
        TagDto tagDto = new TagDto();
        tagDto.setName("tag");
        TagDto tagDto2 = new TagDto();
        tagDto2.setName("tag2");

        productService.getProductDtosWithTags(category,Arrays.asList(tagDto,tagDto2));
        ArgumentCaptor<CatalogProduct> catalogProductCaptor =
                ArgumentCaptor.forClass(CatalogProduct.class);
        verify(mappingService, times(1))
                .toProductDto(catalogProductCaptor.capture());
        List<CatalogProduct> products = catalogProductCaptor.getAllValues();
        Assert.assertEquals(true,products.contains(product));
        Assert.assertEquals(false,products.contains(product2));
        Assert.assertEquals(false,products.contains(product3));
    }

    @Test
    public void setPriceTest(){
        ProductDto productDto = new ProductDto();
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("1");
        IngredientDto ingredientDto2 = new IngredientDto();
        ingredientDto2.setName("2");
        IngredientDto ingredientDto3 = new IngredientDto();
        ingredientDto3.setName("3");
        IngredientDto ingredientDto4 = new IngredientDto();
        ingredientDto4.setName("4");
        productDto.setIngredients(Arrays.asList(ingredientDto,ingredientDto2,
                ingredientDto3,ingredientDto4));

        Ingredient ingredient = new Ingredient();
        ingredient.setPrice(1);
        when(ingredientDao.getIngredient("1")).thenReturn(ingredient);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setPrice(3);
        when(ingredientDao.getIngredient("2")).thenReturn(ingredient2);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setPrice(5);
        when(ingredientDao.getIngredient("3")).thenReturn(ingredient3);
        Ingredient ingredient4 = new Ingredient();
        ingredient4.setPrice(7);
        when(ingredientDao.getIngredient("4")).thenReturn(ingredient4);

        productService.setPrice(productDto);
        Assert.assertEquals(Integer.valueOf(16),productDto.getPrice());
    }

    @Test
    public void getTopProductsDtoForAdminTest(){
        productService.setMappingService(mappingService);
        List<_Order> orders = new ArrayList<>();
        when(mappingService.toProductDto(any())).thenReturn(new ProductDto());

        AbstractProduct product = new CatalogProduct();
        product.setCategory(new Category());
        product.setId(1L);
        ((CatalogProduct) product).setName("name");
        AbstractProduct product2 = new CatalogProduct();
        product2.setCategory(new Category());
        product2.setId(2L);
        ((CatalogProduct) product2).setName("name2");
        AbstractProduct product3 = new CustomProduct();
        product3.setCategory(new Category());
        product3.setId(3L);

        for (int i = 0; i < 10; i++) {
            _Order order = new _Order();
            order.setItems(Arrays.asList(product,product2, product3));
            orders.add(order);
        }

        _Order uncountedOrder = new _Order();
        AbstractProduct uncountedProduct = new CatalogProduct();
        uncountedProduct.setId(4L);
        uncountedOrder.setItems(Arrays.asList(uncountedProduct,uncountedProduct,uncountedProduct));
        orders.add(uncountedOrder);

        when(orderDao.getRecentOrders()).thenReturn(orders);

        productService.getTopProductsDtoForAdmin();
        ArgumentCaptor<AbstractProduct> abstractProductArgumentCaptor =
                ArgumentCaptor.forClass(AbstractProduct.class);
        verify(mappingService,times(3))
                .toProductDto(abstractProductArgumentCaptor.capture());
        List<AbstractProduct> products = abstractProductArgumentCaptor.getAllValues();
        Assert.assertEquals(true,products.contains(product));
        Assert.assertEquals(true,products.contains(product2));
        Assert.assertEquals(true,products.contains(product3));
        Assert.assertEquals(false,products.contains(uncountedProduct));
    }

 */
}
