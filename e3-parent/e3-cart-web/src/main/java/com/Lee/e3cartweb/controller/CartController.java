package com.Lee.e3cartweb.controller;

import com.Lee.dto.E3Result;
import com.Lee.e3mall.cart.service.CartService;
import com.Lee.pojo.TbItem;
import com.Lee.pojo.TbUser;
import com.Lee.service.ItemService;
import com.Lee.utils.CookieUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车处理Controller
 */
@Controller
public class CartController {
	
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	
	@Reference
	private ItemService itemService;
	@Reference
	private CartService cartService;

	/**
	 *	若用户未登录存入cookie
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue="1")Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//如果是登录状态，把购物车写入redis
		if (user != null) {
			//保存到服务端
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑视图
			return "cartSuccess";
		}
		//如果未登录使用cookie
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断此商品在商品列表中是否存在,存在则直接累加数量
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			//如果存在数量相加
			if (tbItem.getId() == itemId.longValue()) {  //包装类型若直接==判断的是地址是否相等
				flag = true;
				//找到商品，数量相加
				tbItem.setNum(tbItem.getNum() + num);
				//跳出循环
				break;
			}
		}
		//如果不存在
		if (!flag) {
			//根据商品id查询商品信息。得到一个TbItem对象
			TbItem tbItem = itemService.getItemById(itemId);
			//设置商品数量
			tbItem.setNum(num);
			//取一张图片
			String image = tbItem.getImage();
			if (!StringUtils.isEmpty(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			//把商品添加到商品列表，再在后边set到cookie中
			cartList.add(tbItem);
		}
		//写入cookie
		Gson gson = new Gson();
		//因为有中文所以需要编码在保存到cookie
		CookieUtils.setCookie(request, response, "cart", gson.toJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回添加成功页面
		return "cartSuccess";
	}
	
	/**
	 * 从cookie中取购物车列表的处理
	 */
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		//解码取出已编码的cookie
		String json = CookieUtils.getCookieValue(request, "cart", true);
		//判断json是否为空
		if (StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}
		//把json转换成商品列表
		Gson gson = new Gson();
		List<TbItem> list = gson.fromJson(json, new TypeToken<List<TbItem>>(){}.getType());
		return list;
	}
	
	/**
	 * 展示购物车列表
	 */
	@RequestMapping("/cart/cart")
	public String showCatList(HttpServletRequest request, HttpServletResponse response) {
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//如果是登录状态
		if (user != null) {
			//从cookie中取购物车列表
			//如果不为空，把cookie中的购物车商品和服务端的购物车商品合并。
			cartService.mergeCart(user.getId(), cartList);
			//把cookie中的购物车删除
			CookieUtils.deleteCookie(request, response, "cart");
			//从服务端取购物车列表
			cartList = cartService.getCartList(user.getId());
			
		}
		//把列表传递给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "cart";
	}
	
	/**
	 * 点击购物车页面中的+号或-号，更新购物车商品数量
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num
			, HttpServletRequest request ,HttpServletResponse response) {
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//已登录则直接在更新商品数量到redis
		if (user != null) {
			cartService.updateCartNum(user.getId(), itemId, num);
			return E3Result.ok();
		}
		//否则未登录,从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历商品列表找到对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		//把购物车列表写回cookie
		Gson gson = new Gson();
		CookieUtils.setCookie(request, response, "cart", gson.toJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回成功
		return E3Result.ok();
	}
	
	/**
	 * 删除购物车商品
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历列表，找到要删除的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				//删除商品
				cartList.remove(tbItem);
				//跳出循环
				break;
			}
		}
		//把购物车列表写入cookie
		Gson gson = new Gson();
		CookieUtils.setCookie(request, response, "cart", gson.toJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回逻辑视图
		return "redirect:/cart/cart.html";
	}
}
