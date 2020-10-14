package org.springblade.system.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.launch.constant.CommonConstant;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.system.entity.AuthTable;
import org.springblade.system.service.IAuthTableService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class TableNameScanHandler {
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IAuthTableService authTableService;

	@RabbitListener(queues = CommonConstant.API_RESOURCE)
	public void scanTableNameQueue(JSONObject resource){
		String serverId=resource.getString("serverId");
		//Object obj=redisUtil.hget(CommonConstant.API_RESOURCE,serverId);
		JSONArray tables=resource.getJSONArray("tables");
		try {
			List<Map> list = tables.toJavaList(Map.class);
			authTableService.customSaveBatch(list);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
