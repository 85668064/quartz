package com.visionvera.rmcp.job.service;


import com.github.pagehelper.PageInfo;
import com.visionvera.rmcp.common.exception.BusinessException;
import com.visionvera.rmcp.common.exception.SystemException;
import com.visionvera.rmcp.job.entity.JobAndTrigger;

public interface JobAndTriggerService {
	
	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);

	void addJob(String jobClassName, String jobGroupName, String cronExpression) throws BusinessException, SystemException;
	
	void createJob(String jobName, String jobGroup, String cronExpression, Class jobClass) throws BusinessException, SystemException;

	void jobPause(String jobClassName, String jobGroupName) throws BusinessException, SystemException;

	void jobresume(String jobClassName, String jobGroupName) throws BusinessException, SystemException;

	void jobreschedule(String jobClassName, String jobGroupName, String cronExpression) throws BusinessException, SystemException;

	void jobdelete(String jobClassName, String jobGroupName) throws BusinessException, SystemException;

	boolean isExist(String jobName, String jobGroupName) throws BusinessException, SystemException;
}
