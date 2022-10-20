package com.shop.buysell.services;

import com.shop.buysell.models.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<Product> productList = new ArrayList<>();
    private Long Id = 0L;

    {
        productList.add(new Product(++Id,"PlayStation 5", "Simple description", 67000, "Krasnoyarsk", "tomas"));
        productList.add(new Product(++Id,"Iphone 8", "Simple description", 24000, "Moscow", "artem"));

    }

    public List<Product> list(){
        return productList;
    }

    public void saveProduct(Product product){
        product.setId(++Id);
        productList.add(product);
    }

    public void deleteProduct(Long id){
        productList.removeIf(product -> product.getId().equals(id));
    }

    public Product getProductById(Long id) {

        for (Product product :
                productList) {
            if (product.getId().equals(id)) return product;
        }
        return null;
    }
}
