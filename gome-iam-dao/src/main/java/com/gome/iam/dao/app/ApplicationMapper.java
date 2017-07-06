package com.gome.iam.dao.app;

import com.gome.iam.domain.app.Application;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author yintongjiang
 * @params
 * @since 2016/10/24
 */
@Repository
public interface ApplicationMapper {
    int count();


    int countByAppName(String appName);


    void save(Application application);


    void update(Application application);


    Application find(Integer appId);


    Application findByAppkey(String appKey);


    List<Application> findAll();


    List<Application> findByAppName(@Param(value = "appName") String appName, @Param(value = "offset") int offset, @Param(value = "limit") int limit);


    List<Application> findByPage(@Param(value = "offset") int offet, @Param(value = "limit") int limit);

    List<Application> findByStatus(Integer status);

    int countByDomain(@Param(value = "domain") String domain,@Param(value = "status") Integer status);
}
