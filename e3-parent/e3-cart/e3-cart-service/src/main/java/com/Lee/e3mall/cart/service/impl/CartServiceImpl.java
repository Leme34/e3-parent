package com.Lee.e3mall.cart.service.impl;

import com.Lee.dao.TbItemMapper;
import com.Lee.dto.E3Result;
import com.Lee.e3mall.cart.service.CartService;
import com.Lee.jedis.JedisClient;
import com.Lee.pojo.TbItem;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 购物车处理服务
 */
@Component
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper itemMapper;

	/**
	 * 添加购物车信息到redis缓存中
	 */
	@Override
	public E3Result addCart(long userId, long itemId, int num) {
		Gson gson = new Gson();
		//向redis中添加购物车。
		//数据类型是hash key：用户id field：商品id value：商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
		//如果存在数量相加
		if (hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
			//把json转换成TbItem
			TbItem item = gson.fromJson(json, TbItem.class);
			item.setNum(item.getNum() + num);
			//写回redis
			jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", gson.toJson(item));
			//返回成功
			return E3Result.ok();
		}
		//如果不存在，根据商品id取商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//设置购物车数据量
		item.setNum(num);
		//取一张图片
		String image = item.getImage();
		if (!StringUtils.isEmpty(image)) {
			item.setImage(image.split(",")[0]);
		}
		//添加到购物车列表
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", gson.toJson(item));
		return E3Result.ok();
	}

	/**
	 * 把购物车信息合并到redis缓存中
	 */
	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		//遍历商品列表
		for (TbItem tbItem : itemList) {
			//把列表添加到购物车。
			//判断购物车中是否有此商品
			//如果有，数量相加
			//如果没有添加新的商品
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		//返回成功
		return E3Result.ok();
	}


	@Override
	public List<TbItem> getCartList(long userId) {
		Gson gson = new Gson();
		//根据用户id查询购车列表
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String string : jsonList) {
			//创建一个TbItem对象
			TbItem item = gson.fromJson(string, TbItem.class);
			//添加到列表
			itemList.add(item);
		}
		return itemList;
	}

	/**
	 * 更新购物车缓存中此商品的数量
	 */
	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		Gson gson = new Gson();
		//从redis中取商品信息
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		//更新商品数量
		TbItem tbItem = gson.fromJson(json, TbItem.class);
		tbItem.setNum(num);
		//写入redis
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", gson.toJson(tbItem));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		// 删除购物车商品
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return E3Result.ok();
	}

	/**
	 * 清空此用户购物车缓存
	 */
	@Override
	public E3Result clearCartItem(long userId) {
		//这样删除会使用户下次登录后访问购物车报空指针异常,所以不能删除field
//		jedisClient.del(REDIS_CART_PRE + ":" + userId);

		//从redis中取商品信息
		List<TbItem> cartList = getCartList(userId);
		//遍历购物车删除商品
		cartList.stream().map(item->
				jedisClient.hdel(REDIS_CART_PRE + ":" + userId, item.getId() + ""));
		return E3Result.ok();
	}


}
