package com.visionvera.rmcp.job.service.impl;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.rmcp.common.exception.BusinessException;
import com.visionvera.rmcp.common.exception.SystemException;
import com.visionvera.rmcp.job.dao.JobAndTriggerMapper;
import com.visionvera.rmcp.job.entity.JobAndTrigger;
import com.visionvera.rmcp.job.job.BaseJob;
import com.visionvera.rmcp.job.service.JobAndTriggerService;

/**
 * quartz定时器操作类 Description:
 * 
 * @version 2018年8月27日 下午5:18:50 韩旭 (QQ:85668064) created
 */
@Service
public class JobAndTriggerImpl implements JobAndTriggerService {

	@Autowired
	private JobAndTriggerMapper jobAndTriggerMapper;

	// 加入Qulifier注解，通过名称注入bean
	@Autowired
	@Qualifier("Scheduler")
	private Scheduler scheduler;

	/**
	 * 查询定时器列表 Description:
	 * 
	 * @Version 2018年8月27日 下午5:18:26 韩旭(QQ:85668064) 创建
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails();
		PageInfo<JobAndTrigger> pageInfo = new PageInfo<JobAndTrigger>(list);
		return pageInfo;
	}

	/**
	 * 添加一个quartz定时器 Description:
	 * 
	 * @Version 2018年8月27日 下午5:11:39 韩旭(QQ:85668064) 创建
	 * @param jobClassName
	 * @param jobGroupName
	 * @param cronExpression
	 * @throws Exception
	 */
	@Override
	public void addJob(String jobClassName, String jobGroupName, String cronExpression)throws BusinessException, SystemException {
		try {
			// 启动调度器
			scheduler.start();

			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();

			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);

		} catch (Exception e) {
			throw new SystemException("创建定时任务失败:" + e.getMessage());
		}
	}

	/**
	 * 添加一个quartz定时器 Description:
	 * 
	 * @Version 2018年8月27日 下午5:11:39 韩旭(QQ:85668064) 创建
	 * @param jobClassName
	 * @param jobGroupName
	 * @param cronExpression
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void createJob(String jobName, String jobGroup, String cronExpression, Class jobClass)
			throws BusinessException, SystemException {
		try {
			// 启动调度器
			scheduler.start();

			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();

			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
					.withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);

		} catch (SchedulerException e) {
			throw new SystemException("创建定时任务失败:" + e.getMessage());
		}
	}

	/**
	 * 暂停一个定时器 Description:
	 * 
	 * @Version 2018年8月27日 下午5:13:38 韩旭(QQ:85668064) 创建
	 * @param jobClassName
	 * @param jobGroupName
	 * @throws Exception
	 */
	@Override
	public void jobPause(String jobClassName, String jobGroupName) throws BusinessException, SystemException {
		try {
			scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
		} catch (SchedulerException e) {
			throw new SystemException(e.getMessage());
		}
	}

	/**
	 * 恢复一个定时器 Description:
	 * 
	 * @Version 2018年8月27日 下午5:13:38 韩旭(QQ:85668064) 创建
	 * @param jobClassName
	 * @param jobGroupName
	 * @throws Exception
	 */
	@Override
	public void jobresume(String jobClassName, String jobGroupName) throws BusinessException, SystemException {
		try {
			scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
		} catch (Exception e) {
			throw new SystemException(e.getMessage());
		}
	}

	/**
	 * 更新一个定时器 Description:
	 * 
	 * @Version 2018年8月27日 下午5:13:38 韩旭(QQ:85668064) 创建
	 * @param jobClassName
	 * @param jobGroupName
	 * @throws Exception
	 */
	@Override
	public void jobreschedule(String jobClassName, String jobGroupName, String cronExpression)
			throws BusinessException, SystemException {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (Exception e) {
			throw new SystemException("更新定时任务失败:" + e.getMessage());
		}
	}

	/**
	 * 删除一个定时器 Description:
	 * 
	 * @Version 2018年8月27日 下午5:13:38 韩旭(QQ:85668064) 创建
	 * @param jobClassName
	 * @param jobGroupName
	 * @throws Exception
	 */
	@Override
	public void jobdelete(String jobClassName, String jobGroupName) throws BusinessException, SystemException {
		try {
			scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
			scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
		} catch (Exception e) {
			throw new SystemException(e.getMessage());
		}
	}
	
	/**
	 * 验证定时器是否存在
	 * Description: 
	 * @Version 2018年8月27日 下午6:31:50 韩旭(QQ:85668064) 创建
	 * @param jobName
	 * @param jobGroupName
	 * @return
	 */
	@Override
	public boolean isExist(String jobName,String jobGroupName) throws BusinessException, SystemException {
		try {
			TriggerState state = scheduler.getTriggerState(new TriggerKey(jobName, jobGroupName));
			if(TriggerState.NONE.compareTo(state) != 0){
				return true;
			}
		} catch (SchedulerException e) {
			throw new SystemException(e.getMessage());
		}
		return false;
	}
	
	/**
	 * 反射job执行类 Description:
	 * 
	 * @Version 2018年8月27日 下午6:05:09 韩旭(QQ:85668064) 创建
	 * @param classname
	 * @return
	 * @throws Exception
	 */
	public static BaseJob getClass(String classname) throws Exception {
		Class<?> class1 = Class.forName(classname);
		return (BaseJob) class1.newInstance();
	}

}