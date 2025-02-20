package com.douyin.controller;


import com.douyin.dto.ProductDTO;
import com.douyin.dto.ProductPageQueryDTO;
import com.douyin.result.PageResult;
import com.douyin.result.Result;
import com.douyin.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 新增商品
     * @param productDTO
     * @return
     */
    @PostMapping()
    public Result save(@RequestBody ProductDTO productDTO){
        log.info("新增商品：{}",productDTO);
        productService.save(productDTO);
        return Result.success();
    }

    /**
     * 商品分页查询
     * @param productPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result page(ProductPageQueryDTO productPageQueryDTO){
        log.info("商品分页查询，参数为：{}",productPageQueryDTO);
        PageResult pageResult =productService.pageQuery(productPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除商品
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam("ids") List<Long> ids){
        log.info("删除商品：{}",ids);
        productService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改商品
     * @param productDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody ProductDTO productDTO){
        log.info("修改商品：{}",productDTO);
        productService.update(productDTO);
        return Result.success();
    }
}
