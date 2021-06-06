package com.store.controller.commands.products;

import com.store.controller.commands.Command;
import com.store.controller.commands.CommandUtils;
import com.store.model.dao.Utils;
import com.store.model.entity.Product;
import com.store.model.service.ProductService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ProductListCommand implements Command {

    private ProductService productService;

    private static final Logger log = Logger.getLogger(ProductListCommand.class);

    public ProductListCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Commands starts");
        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        String sort = request.getParameter("parameter");
        String direction = null;
        String orderDirection = request.getParameter("sortDirection");
        if (sort != null && orderDirection != null) {
            sort = sort.toLowerCase();
            direction = productService.getSortDirection(orderDirection);
        }
        if (sort == null || sort.equals("")) {
            sort = "id";
        }
        if (direction == null || direction.equals("")) {
            direction = "ASC";
        }

        request.setAttribute("sortWay", request.getParameter("parameter"));
        request.setAttribute("sortDirection", request.getParameter("sortDirection"));
        request.setAttribute("currentPage", page);
        int totalCount = productService.countAllProducts();
        request.setAttribute("pageCount", CommandUtils.getPageCount(totalCount, Utils.PRODUCTS_PER_PAGE));
        String locale = CommandUtils.checkForLocale(request);
        List<Product> products = productService.listProductsPerPage(page, Utils.PRODUCTS_PER_PAGE, sort, direction,
                locale);

        request.setAttribute("products", products);

        CommandUtils.setAttributes(request, productService);

        log.debug("Commands finished");
        return "/WEB-INF/product-list.jsp";
    }


}
