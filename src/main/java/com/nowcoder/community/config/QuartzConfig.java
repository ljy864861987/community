package com.nowcoder.community.config;

import com.nowcoder.community.quartz.A_Job;
import com.nowcoder.community.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

// 配置 -> 数据库 -> 调用
@Configuration
public class QuartzConfig {

	// FactoryBean可简化Bean的实例化过程：
	// 1.通过FactoryBean封装了Bean的实例化过程
	// 2.将FactoryBean装配到Spring容器里
	// 3.将FactoryBean注入给其他的Bean
	// 4.该Bean得到的是FactoryBean所管理的对象实例

	// 配置JobDetail
//	@Bean
	public JobDetailFactoryBean a_JobDetail() {
		JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
		factoryBean.setJobClass(A_Job.class);
		factoryBean.setName("a_Job");
		factoryBean.setGroup("a_JobGroup");
		factoryBean.setDurability(true);
		factoryBean.setRequestsRecovery(true);
		return factoryBean;
	}

	// 配置Trigger（SimpleTriggerFactoryBean，CronTriggerFactoryBean）
//	@Bean
	public SimpleTriggerFactoryBean a_Trigger(JobDetail a_JobDetail) {
		SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
		factoryBean.setJobDetail(a_JobDetail);
		factoryBean.setName("a_Trigger");
		factoryBean.setGroup("a_TriggerGroup");
		factoryBean.setRepeatInterval(1000);
		factoryBean.setJobDataMap(new JobDataMap());
		return factoryBean;
	}

	@Bean
	public JobDetailFactoryBean postScoreRefreshJobDetail() {
		JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
		factoryBean.setJobClass(PostScoreRefreshJob.class);
		factoryBean.setName("postScoreRefreshJob");
		factoryBean.setGroup("communityJobGroup");
		factoryBean.setDurability(true);
		factoryBean.setRequestsRecovery(true);
		return factoryBean;
	}

	@Bean
	public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail) {
		SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
		factoryBean.setJobDetail(postScoreRefreshJobDetail);
		factoryBean.setName("postScoreRefreshJobDetailTrigger");
		factoryBean.setGroup("communityTriggerGroup");
		factoryBean.setRepeatInterval(1000 * 60);
		factoryBean.setJobDataMap(new JobDataMap());
		return factoryBean;
	}

}
