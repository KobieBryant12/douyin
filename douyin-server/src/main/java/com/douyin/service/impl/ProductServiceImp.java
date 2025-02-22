package com.douyin.service.impl;

import com.douyin.constant.MessageConstant;
import com.douyin.dto.ProductDTO;
import com.douyin.dto.ProductPageQueryDTO;
import com.douyin.entity.Product;
import com.douyin.exception.DeletionNotAllowedException;
import com.douyin.mapper.ProductMapper;
import com.douyin.result.PageResult;
import com.douyin.service.ProductService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 新增商品
     * @param productDTO
     */
    @Override
    public void save(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO,product);

        //创建时间 修改时间
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());

        //向菜品表插入数据（一条）
        productMapper.insert(product);
        Long productId = product.getId();//获取insert语句生成的主键Id

    }

    /**
     * 商品分页查询
     * @param productPageQueryDTO
     * @return
     */
    public PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(productPageQueryDTO.getPage(),productPageQueryDTO.getPageSize());
        Page page = productMapper.pageQuery(productPageQueryDTO);
        List result = page.getResult();
        long total = page.getTotal();
        return new PageResult(total,result);

    }

    /**
     * 商品批量删除
     * @param ids
     */
    //TODO
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断能否删除（是否起售中）
        for(Long id:ids){
            Product product = productMapper.getById(id);
            if(product.getStatus() == 1){//起售中
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //TODO判断是否能删除（是否被订单关联）
//        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
//        if(setmealIds != null && setmealIds.size() > 0){
//            //不能删除
//            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
//        }

        //可以删除就删除菜品数据
//        for (Long id : ids) {
//            dishMapper.delectById(id);
//            //删除对应的口味数据
//            dishFlavorMapper.deleteByDishId(id);
//        }
        //根据菜品id集合直接批量删除菜品集合（不需要for一条一条发送sql语句）
        productMapper.deleteByIds(ids);

    }

    /**
     * 修改商品
     * @param productDTO
     */
    @Override
    public void update(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO,product);

        //修改菜品表
        productMapper.update(product);
    }
}
