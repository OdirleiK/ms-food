package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;

public class PaymentServiceStack extends Stack {
    public PaymentServiceStack(final Construct scope, final String id, final Cluster cluster) {
        this(scope, id, null, cluster);
    }

    public PaymentServiceStack(final Construct scope, final String id, final StackProps props, final Cluster cluster) {
        super(scope, id, props);

        ApplicationLoadBalancedFargateService.Builder.create(this, "FoodService")
        .serviceName("food-service-ola")
        .cluster(cluster)           // Required
        .cpu(512)                   // Default is 256
        .desiredCount(1)            // Default is 1
        .listenerPort(8080)
        .assignPublicIp(true)
        .taskImageOptions(ApplicationLoadBalancedTaskImageOptions.builder()
                          .image(ContainerImage.fromRegistry("jacquelineoliveira/ola:1.0"))
                          .containerPort(8080)
                          .containerName("app_ola")
                          .build())
        .memoryLimitMiB(1024)       // Default is 512
        .publicLoadBalancer(true)   // Default is false
        .build();
    }
}
