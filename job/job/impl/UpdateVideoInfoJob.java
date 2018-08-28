package com.visionvera.rmcp.job.job.impl;

import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.visionvera.rmcp.common.exception.SystemException;
import com.visionvera.rmcp.educourse.dao.MpyiliaoVideoInfoMapper;
import com.visionvera.rmcp.job.SpringContextUtil;
import com.visionvera.rmcp.job.job.BaseJob;

public class UpdateVideoInfoJob implements BaseJob {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			String id = arg0.getTrigger().getJobKey().getName().split("_")[1];
			MpyiliaoVideoInfoMapper mpyiliaoVideoInfoMapper = (MpyiliaoVideoInfoMapper) SpringContextUtil.getBean("mpyiliaoVideoInfoMapper");
			Map map = mpyiliaoVideoInfoMapper.getById(Integer.parseInt(id));
			map.put("videostatus", 1);
			mpyiliaoVideoInfoMapper.updateVideoInfo(map);
		} catch (NumberFormatException | SystemException e) {
			e.printStackTrace();
		}
	}
	
}