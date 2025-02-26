package com.douyin.controller.user;

import com.douyin.entity.Product;
import com.douyin.result.Result;
import com.douyin.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userProductController")
@RequestMapping("/user/product")
@Slf4j
public class ProductController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProductService productService;

    /**
     * 根据分类id查询
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result list(Long categoryId) {

        //构造redis中的key
        String key = "Product:" + categoryId;

        //查询redis中是否有商品数据
        List<Product> list = (List<Product>)redisTemplate.opsForValue().get(key);

        //如果有，直接返回
        if(list != null){
            return Result.success(list);
        }

        //如果没有，查询数据库，将查询到的数据缓存到redis中
        Product product = new Product();
        product.setCategoryId(categoryId);
        product.setStatus(1);//查询起售中的


        list = productService.list(product);
        redisTemplate.opsForValue().set(key,list);

        return Result.success(list);
    }

}

