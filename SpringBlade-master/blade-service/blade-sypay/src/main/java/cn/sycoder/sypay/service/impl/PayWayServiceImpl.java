/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sycoder.sypay.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.sycoder.sypay.entity.Merchant;
import cn.sycoder.sypay.entity.MerchantApp;
import cn.sycoder.sypay.entity.PayWay;
import cn.sycoder.sypay.mapper.MerchantMapper;
import cn.sycoder.sypay.mapper.PayWayMapper;
import cn.sycoder.sypay.service.IMerchantAppService;
import cn.sycoder.sypay.service.IMerchantService;
import cn.sycoder.sypay.service.IPayWayService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.SyPayConstant;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class PayWayServiceImpl extends BaseServiceImpl<PayWayMapper, PayWay> implements IPayWayService {


}
