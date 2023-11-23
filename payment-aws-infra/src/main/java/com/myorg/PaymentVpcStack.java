package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.ec2.Vpc;

public class PaymentVpcStack extends Stack {
	
	private Vpc vpc; 
	
    public PaymentVpcStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public PaymentVpcStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

         vpc = Vpc.Builder.create(this, "PaymentVpc")
                .maxAzs(3)  // Default is all AZs in region
                .build();
    }
    
    public Vpc getVpc() {
    	return vpc;
    }
}
