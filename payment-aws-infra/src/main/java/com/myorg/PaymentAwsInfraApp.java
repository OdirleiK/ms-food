package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class PaymentAwsInfraApp {
    public static void main(final String[] args) {
        App app = new App();
        
        PaymentVpcStack vpcStack = new PaymentVpcStack(app, "Vpc");
        PaymentClusterStack clusterStack = new PaymentClusterStack(app, "Cluster", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);
        
        app.synth();
        
    }
}

