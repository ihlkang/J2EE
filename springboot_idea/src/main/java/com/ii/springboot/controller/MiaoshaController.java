package com.ii.springboot.controller;

import com.ii.springboot.service.MiaoShaUserService;
import com.ii.springboot.domain.MiaoShaUser;
import com.ii.springboot.result.Result;
import com.ii.springboot.service.MiaoshaService;
import com.ii.springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ii.springboot.domain.MiaoshaOrder;
import com.ii.springboot.domain.OrderInfo;
import com.ii.springboot.redis.RedisService;
import com.ii.springboot.result.CodeMsg;
import com.ii.springboot.service.GoodsService;
import com.ii.springboot.vo.GoodsVo;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

	@Autowired
    MiaoShaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	@Autowired
	MiaoshaService miaoshaService;

	/**
	 *  GET POST有什么区别？
	 * */
	@RequestMapping(value="/do_miaosha", method=RequestMethod.POST)
	@ResponseBody
	public Result<OrderInfo> miaosha(Model model, MiaoShaUser user,
									 @RequestParam("goodsId")long goodsId) {
		model.addAttribute("user", user);
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		//判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);//10个商品，req1 req2
		int stock = goods.getStockCount();
		if(stock <= 0) {
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		//判断是否已经秒杀到了
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}
		//减库存 下订单 写入秒杀订单
		OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
		return Result.success(orderInfo);
	}
}
