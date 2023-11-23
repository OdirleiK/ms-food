package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.ec2.Vpc;

public class PaymentAwsVpcStack extends Stack {
    public PaymentAwsVpcStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public PaymentAwsVpcStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Vpc vpc = Vpc.Builder.create(this, "PaymentVpc")
                .maxAzs(3)  // Default is all AZs in region
                .build();
    }
}
