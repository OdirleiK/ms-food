package com.myorg;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.applicationautoscaling.EnableScalingProps;
import software.amazon.awscdk.services.ecr.IRepository;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.logs.LogGroup;
import software.constructs.Construct;


public class PaymentServiceStack extends Stack {
    public PaymentServiceStack(final Construct scope, final String id, final Cluster cluster) {
        this(scope, id, null, cluster);
    }

    public PaymentServiceStack(final Construct scope, final String id, final StackProps props, final Cluster cluster) {
        super(scope, id, props);

        Map<String, String> authentication = new HashMap<>();
        
        authentication.put("SPRING_DATASOURCE_URL", "jdbc:mysql://" + Fn.importValue("order-db-endpoint") 
        				  + ":3306/food-order-ms?createDatabaseIfNotExist=true");
        authentication.put("SPRING_DATASOURCE_USERNAME", "admin");  
        authentication.put("SPRING_DATASOURCE_PASSWORD", Fn.importValue("order-db-senha"));
        			
        
        IRepository repository = Repository.fromRepositoryName(this, "repository", "img-order-ms");

        ApplicationLoadBalancedFargateService foodService = ApplicationLoadBalancedFargateService.Builder.create(this, "FoodService")
        .serviceName("food-service-ola")
        .cluster(cluster)           // Required
        .cpu(512)                   // Default is 256
        .desiredCount(1)            // Default is 1
        .listenerPort(8080)
        .assignPublicIp(true)
        .taskImageOptions(ApplicationLoadBalancedTaskImageOptions.builder()
                          .image(ContainerImage.fromEcrRepository(repository))
                          .containerPort(8080)
                          .containerName("order-ms")
                          .environment(authentication)
                          .logDriver(LogDriver.awsLogs(AwsLogDriverProps.builder()
                                  .logGroup(LogGroup.Builder.create(this, "OrderMsLogGroup")
                                      .logGroupName("OrderMsLog")
                                      .removalPolicy(RemovalPolicy.DESTROY)
                                      .build())
                                  .streamPrefix("PedidosMS")
                                  .build()))
                          .build())
        .memoryLimitMiB(1024)       // Default is 512
        .publicLoadBalancer(true)   // Default is false
        .build();
        
        ScalableTaskCount scalableTarget = foodService.getService().autoScaleTaskCount(EnableScalingProps.builder()
                .minCapacity(1)
                .maxCapacity(3)
                .build());
        scalableTarget.scaleOnCpuUtilization("CpuScaling", CpuUtilizationScalingProps.builder()
                .targetUtilizationPercent(75)
                	.scaleInCooldown(Duration.minutes(3))
                	.scaleOutCooldown(Duration.minutes(2))
                .build());
        scalableTarget.scaleOnMemoryUtilization("MemoryScaling", MemoryUtilizationScalingProps.builder()
                .targetUtilizationPercent(70)
	                .scaleInCooldown(Duration.minutes(3))
	            	.scaleOutCooldown(Duration.minutes(2))
                .build());
    }
}
