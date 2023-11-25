package com.myorg;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awscdk.Fn;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
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
        		
        ApplicationLoadBalancedFargateService.Builder.create(this, "FoodService")
        .serviceName("food-service-ola")
        .cluster(cluster)           // Required
        .cpu(512)                   // Default is 256
        .desiredCount(1)            // Default is 1
        .listenerPort(8080)
        .assignPublicIp(true)
        .taskImageOptions(ApplicationLoadBalancedTaskImageOptions.builder()
                          .image(ContainerImage.fromRegistry("kmpx/order-ms"))
                          .containerPort(8080)
                          .containerName("order-ms")
                          .environment(authentication)
                          .build())
        .memoryLimitMiB(1024)       // Default is 512
        .publicLoadBalancer(true)   // Default is false
        .build();
    }
}
