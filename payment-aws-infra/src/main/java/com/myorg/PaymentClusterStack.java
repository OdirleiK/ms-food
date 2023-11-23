package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.ecs.Cluster;

public class PaymentClusterStack extends Stack {
    public PaymentClusterStack(final Construct scope, final String id, final Vpc vpc) {
        this(scope, id, null, vpc);
    }

    public PaymentClusterStack(final Construct scope, final String id, final StackProps props, final Vpc vpc) {
        super(scope, id, props);

        Cluster cluster = Cluster.Builder.create(this, "FoodCluster")
        		.clusterName("cluster-food-ms")
        		.vpc(vpc).build();   
    }
}
