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
package cn.sycoder.sypay.payorder.service.impl;

import cn.sycoder.sypay.model.DBApplicationConfig;
import cn.sycoder.sypay.payorder.entity.SysConfig;
import cn.sycoder.sypay.payorder.mapper.SysConfigMapper;
import cn.sycoder.sypay.payorder.service.ISysConfigService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {
	/** 是否启用缓存
	 * true: 表示将使用内存缓存， 将部分系统配置项 或 商户应用/服务商信息进行缓存并读取
	 * false: 直接查询DB
	 * **/
	public static boolean IS_USE_CACHE = false;

	/** 数据库application配置参数 **/
	private static MutablePair<String, DBApplicationConfig>
		APPLICATION_CONFIG = new MutablePair<>("applicationConfig", null);

	public synchronized void initDBConfig(String groupKey) {

		// 若当前系统不缓存，则直接返回
		if(!IS_USE_CACHE){
			return;
		}
		if(APPLICATION_CONFIG.getLeft().equalsIgnoreCase(groupKey)){
			APPLICATION_CONFIG.right = this.selectByGroupKey(groupKey).
				toJavaObject(DBApplicationConfig.class);
		}
	}

	/** 获取实际的数据 **/
	public DBApplicationConfig getDBApplicationConfig() {

		// 查询DB
		if(!IS_USE_CACHE){
			return this.selectByGroupKey(APPLICATION_CONFIG.getLeft()).toJavaObject(DBApplicationConfig.class);
		}

		// 缓存数据
		if(APPLICATION_CONFIG.getRight() == null ){
			initDBConfig(APPLICATION_CONFIG.getLeft());
		}
		return APPLICATION_CONFIG.right;
	}


	/** 根据分组查询，并返回JSON对象格式的数据 **/
	public JSONObject selectByGroupKey(String groupKey){

		JSONObject result = new JSONObject();
		list(SysConfig.gw().select(SysConfig::getConfigKey, SysConfig::getConfigVal).eq(SysConfig::getGroupKey, groupKey))
			.stream().forEach(item -> result.put(item.getConfigKey(), item.getConfigVal()));
		return result;
	}


	public int updateByConfigKey(Map<String, String> updateMap) {
		int count = 0;
		Set<String> set = updateMap.keySet();
		for(String k : set) {
			SysConfig sysConfig = new SysConfig();
			sysConfig.setConfigKey(k);
			sysConfig.setConfigVal(updateMap.get(k));
			boolean update = this.saveOrUpdate(sysConfig);
			if (update) {
				count ++;
			}
		}
		return count;
	}
}
