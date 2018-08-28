package com.visionvera.rmcp.job.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.visionvera.rmcp.common.util.ResultInfo;
import com.visionvera.rmcp.job.entity.JobAndTrigger;
import com.visionvera.rmcp.job.service.JobAndTriggerService;

/**
 * quartz定时器controller
 * Description: 
 * @version  2018年8月27日 下午5:28:51 韩旭 (QQ:85668064) created
 */
@RestController
@RequestMapping(value="/job")
public class JobController{
	@Autowired
	private JobAndTriggerService jobAndTriggerService;
	
	final Logger log = LoggerFactory.getLogger(JobController.class);  
	
	/**
	 * 创建定时器
	 * Description: 
	 * @Version 2018年8月27日 下午5:29:30 韩旭(QQ:85668064) 创建
	 * @param map
	 * @throws Exception
	 */
	@RequestMapping(value="/addjob")
	public ResultInfo addjob(@RequestBody Map<String,String> map){	
		ResultInfo rt=new ResultInfo(ResultInfo.SUCESS, "创建成功！", null);
		try {
			jobAndTriggerService.addJob(map.get("jobClassName"), map.get("jobGroupName"), map.get("cronExpression"));
		} catch (Exception e) {
			e.printStackTrace();
			rt=new ResultInfo(ResultInfo.SUCESS, "创建失败！", null);
		}
		return rt;
	}
	
	/**
	 * 暂停定时器
	 * Description: 
	 * @Version 2018年8月27日 下午5:29:47 韩旭(QQ:85668064) 创建
	 * @param map
	 * @throws Exception
	 */
	@RequestMapping(value="/pausejob")
	public ResultInfo pausejob(@RequestBody Map<String,String> map){
		ResultInfo rt=new ResultInfo(ResultInfo.SUCESS, "暂停成功！", null);
		try {
			jobAndTriggerService.jobPause(map.get("jobClassName"), map.get("jobGroupName"));
		} catch (Exception e) {
			e.printStackTrace();
			rt=new ResultInfo(ResultInfo.SUCESS, "暂停失败！", null);
		}
		return rt;
	}
	
	/**
	 * 恢复定时器
	 * Description: 
	 * @Version 2018年8月27日 下午5:30:04 韩旭(QQ:85668064) 创建
	 * @param map
	 * @throws Exception
	 */
	@RequestMapping(value="/resumejob")
	public ResultInfo resumejob(@RequestBody Map<String,String> map){	
		ResultInfo rt=new ResultInfo(ResultInfo.SUCESS, "恢复成功！", null);
		try {
			jobAndTriggerService.jobresume(map.get("jobClassName"), map.get("jobGroupName"));
		} catch (Exception e) {
			e.printStackTrace();
			rt=new ResultInfo(ResultInfo.SUCESS, "恢复失败！", null);
		}
		return rt;
	}
	
	/**
	 * 更新定时器
	 * Description: 
	 * @Version 2018年8月27日 下午5:30:17 韩旭(QQ:85668064) 创建
	 * @param map
	 * @throws Exception
	 */
	@RequestMapping(value="/reschedulejob")
	public ResultInfo rescheduleJob(@RequestBody Map<String,String> map){	
		ResultInfo rt=new ResultInfo(ResultInfo.SUCESS, "更新成功！", null);
		try {
			jobAndTriggerService.jobreschedule(map.get("jobClassName"), map.get("jobGroupName"), map.get("cronExpression"));
		} catch (Exception e) {
			e.printStackTrace();
			rt=new ResultInfo(ResultInfo.SUCESS, "更新失败！", null);
		}
		return rt;
	}
	
	/**
	 * 删除定时器
	 * Description: 
	 * @Version 2018年8月27日 下午5:30:26 韩旭(QQ:85668064) 创建
	 * @param map
	 * @throws Exception
	 */
	@RequestMapping(value="/deletejob")
	public ResultInfo deletejob(@RequestBody Map<String,String> map){
		ResultInfo rt=new ResultInfo(ResultInfo.SUCESS, "删除成功！", null);
		try {
			jobAndTriggerService.jobdelete(map.get("jobClassName"), map.get("jobGroupName"));
		} catch (Exception e) {
			e.printStackTrace();
			rt=new ResultInfo(ResultInfo.SUCESS, "删除失败！", null);
		}
		return rt;
	}
	
	/**
	 * 分页查询定时器列表
	 * Description: 
	 * @Version 2018年8月27日 下午5:30:39 韩旭(QQ:85668064) 创建
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/queryjob")
	public Map<String, Object> queryjob(@RequestParam(value="page")Integer page, @RequestParam(value="pageSize")Integer pageSize) {			
		PageInfo<JobAndTrigger> jobAndTrigger = jobAndTriggerService.getJobAndTriggerDetails(page, pageSize);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("JobAndTrigger", jobAndTrigger);
		map.put("number", jobAndTrigger.getTotal());
		return map;
	}
	
}
