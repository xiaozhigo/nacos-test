package com.hehe.nacosapply.loadbalance;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.utils.StringUtils;
import com.alibaba.nacos.client.naming.core.Balancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @title:
 * @author: luo heng
 * @date: 2023/1/16 13:51
 * @description:   自定义负载均衡实现需要实现 ReactorServiceInstanceLoadBalancer 接口 以及重写choose方法 实现同一集群优先调用
 * @version:
 * @param
 * @return:
 */
@Slf4j
public class NacosSameClusterWeightedRule implements ReactorServiceInstanceLoadBalancer {

    // 注入当前服务的nacos的配置信息
    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    // loadbalancer 提供的访问当前服务的名称
    final String serviceId;

    // loadbalancer 提供的访问的服务列表
    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public NacosSameClusterWeightedRule(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    /**
     * 服务器调用负载均衡时调的方法
     * 此处代码内容与 RandomLoadBalancer 一致
     */
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map((serviceInstances) -> {
            return this.processInstanceResponse(supplier, serviceInstances);
        });
    }

    /**
     * 对负载均衡的服务进行筛选的方法
     * 此处代码内容与 RandomLoadBalancer 一致
     */
    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier, List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback)supplier).selectedServiceInstance((ServiceInstance)serviceInstanceResponse.getServer());
        }

        return serviceInstanceResponse;
    }

    /**
     * 对负载均衡的服务进行筛选的方法
     * 自定义
     * 此处的 instances 实例列表  只会提供健康的实例  所以不需要担心如果实例无法访问的情况
     */
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            return new EmptyResponse();
        }
        //这里可以利用metadata做版本控制调用
        List<ServiceInstance> metadataMatchInstances = instances;
        /*String targetVersion = this.nacosDiscoveryProperties.getMetadata().get("target-version");

        // 如果配置了版本映射，那么只调用元数据匹配的实例
        if (com.alibaba.cloud.commons.lang.StringUtils.isNotBlank(targetVersion)) {
            metadataMatchInstances = instances.stream()
                    .filter(instance -> Objects.equals(targetVersion, instance.getMetadata().get("version")))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(metadataMatchInstances)) {
                log.warn("未找到元数据匹配的目标实例！请检查配置。targetVersion = {}, instance = {}", targetVersion, instances);
                return null;
            }
        }*/

        // 获取当前服务所在的集群名称
        String currentClusterName = nacosDiscoveryProperties.getClusterName();
        // 过滤在同一集群下注册的服务 根据集群名称筛选的集合
        List<ServiceInstance> sameClusterNameInstList  = metadataMatchInstances.stream().filter(i-> StringUtils.equals(i.getMetadata().get("nacos.cluster"),currentClusterName)).collect(Collectors.toList());
        ServiceInstance sameClusterNameInst;
        if (sameClusterNameInstList.isEmpty()) {
            // 如果为空，则根据权重直接过滤所有服务列表
            sameClusterNameInst = getHostByRandomWeight(metadataMatchInstances);
        } else {
            // 如果不为空，则根据权重直接过滤所在集群下的服务列表
            sameClusterNameInst = getHostByRandomWeight(sameClusterNameInstList);
        }
        return new DefaultResponse(sameClusterNameInst);
    }

    private ServiceInstance getHostByRandomWeight(List<ServiceInstance> sameClusterNameInstList){

        List<Instance> list = new ArrayList<>();
        Map<String, ServiceInstance> dataMap = new HashMap<>();
        // 此处将 ServiceInstance 转化为 Instance 是为了接下来调用nacos中的权重算法，由于入参不同，所以需要转换，此处建议打断电进行参数调试，以下是我目前为止所用到的参数，转化为map是为了最终方便获取取值到的服务对象
        sameClusterNameInstList.forEach(i->{
            Instance ins = new Instance();
            Map<String, String> metadata = i.getMetadata();

            ins.setInstanceId(metadata.get("nacos.instanceId"));
            ins.setWeight(new BigDecimal(metadata.get("nacos.weight")).doubleValue());
            ins.setClusterName(metadata.get("nacos.cluster"));
            ins.setEphemeral(Boolean.parseBoolean(metadata.get("nacos.ephemeral")));
            ins.setHealthy(Boolean.parseBoolean(metadata.get("nacos.healthy")));
            ins.setPort(i.getPort());
            ins.setIp(i.getHost());
            ins.setServiceName(i.getServiceId());

            ins.setMetadata(metadata);

            list.add(ins);
            // key为服务ID，值为服务对象
            dataMap.put(metadata.get("nacos.instanceId"),i);
        });
        // 调用nacos官方提供的负载均衡权重算法
        Instance hostByRandomWeightCopy = ExtendBalancer.getHostByRandomWeightCopy(list);

        // 根据最终ID获取需要返回的实例对象
        return dataMap.get(hostByRandomWeightCopy.getInstanceId());
    }

}

class ExtendBalancer extends Balancer {
    /**
     * 根据权重选择随机选择一个
     */
    public static Instance getHostByRandomWeightCopy(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}
