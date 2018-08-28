package com.visionvera.rmcp.job.dao;

import java.util.List;

import com.visionvera.rmcp.job.entity.JobAndTrigger;

public interface JobAndTriggerMapper {
	public List<JobAndTrigger> getJobAndTriggerDetails();
}
