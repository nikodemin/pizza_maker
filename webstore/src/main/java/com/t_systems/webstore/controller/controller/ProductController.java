package com.t_systems.webstore.controller.controller;

import com.t_systems.webstore.model.dto.CategoryDto;
import com.t_systems.webstore.model.dto.IngredientDto;
import com.t_systems.webstore.model.dto.ProductDto;
import com.t_systems.webstore.model.dto.TagDto;
import com.t_systems.webstore.model.enums.OrderStatus;
import com.t_systems.webstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@Log4j
public class ProductController {
    private final ProductService productService;

    /**
     * get admin page
     * @param model model
     * @return page name
     */
    @GetMapping("/admin")
    public String getAdminPage(Model model) {

        model.addAttribute("categories", productService.getAllCategoryDtos());
        model.addAttribute("categoryDto", new CategoryDto());
        model.addAttribute("ingredientDto", new IngredientDto());
        model.addAttribute("tagDto", new TagDto());
        model.addAttribute("productDto", new ProductDto());
        return "admin";
    }

    /**
     * get stats admin page
     * @param model model
     * @return page name
     */
    @GetMapping("/admin/stats")
    public String getStatsAdminPage(Model model){
        List<String> statusList = Stream.of(OrderStatus.values())
                .map(OrderStatus::toString).collect(Collectors.toList());
        model.addAttribute("statusList",statusList);
        return "statsAdmin";
    }

    /**
     * get catalog page
     * @param category specified category
     * @param model model
     * @return page name
     */
    @GetMapping("/catalog/{category}")
    public String getCatalogPage(@PathVariable("category") String category, Model model){
        model.addAttribute("tags", productService.getTagDtosByCategory(category));
        return "catalog";
    }

    /**
     * get index page
     * @param model model
     * @return page name
     */
    @GetMapping("/")
    public String getIndexPage(Model model){
        log.info("Entered index page");
        model.addAttribute("leaders", productService.getTopProductsDto());
        model.addAttribute("categories",productService.getAllCategoryDtos());
        return "index";
    }
}
