package com.geetanjali.twtdw;

import com.geetanjali.twtdw.resources.Resource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TwitterDropWizardApplication extends Application<TwitterDropWizardConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TwitterDropWizardApplication().run(args);
    }

    @Override
    public String getName() {
        return "TwitterDropWizard";
    }

    @Override
    public void initialize(final Bootstrap<TwitterDropWizardConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/"));
    }

    @Override
    public void run(final TwitterDropWizardConfiguration configuration,
                    final Environment environment) {
        final Resource resource = new Resource(configuration.getMessage());
        environment.jersey().register(resource);
    }

}
