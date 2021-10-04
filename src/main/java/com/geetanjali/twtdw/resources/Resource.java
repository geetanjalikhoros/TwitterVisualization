package com.geetanjali.twtdw.resources;

import com.codahale.metrics.annotation.Metered;
import com.geetanjali.twtdw.api.Representation;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.jersey.params.*;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import java.util.logging.Logger;
import org.slf4j.Marker;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Resource {
    private final String message;
    private Twitter twitter ;
    private Status status;
    private List<Status> statuses;


    public Resource(String message) {
        this.message = message;
        this.twitter = TwitterFactory.getSingleton();
        this.statuses = new ArrayList<Status>();
    }

    @Path("timeline")
    @GET
    public Response fetchTweet() throws TwitterException, IOException
    {
        try
        {
            Paging page = new Paging(1,200);
            statuses.addAll(twitter.getHomeTimeline(page));
            int count = statuses.size();
            Representation r[] = new Representation[count];
            for(int i=0; i<count; i++)
            {
                System.out.println("Tweet "+ i +"="+ statuses.get(i).getText());
                r[i] = new Representation(statuses.get(i).getText(), statuses.get(i).getUser().getName(),
                        statuses.get(i).getUser().getScreenName(), statuses.get(i).getUser().getProfileImageURL(),
                        statuses.get(i).getCreatedAt());
            }
            return Response.ok().entity(r).build();}
        catch (TwitterException e)
        {
            return Response.serverError().entity("Error in retrieving tweets").build();
        }
    }

    @Path("tweet")
    @POST
    public Response postTweet() throws TwitterException, IOException
    {
        try {
            status = twitter.updateStatus(message);
            System.out.println("Status update successful to " + status.getText() + "\n");
            return Response.ok().entity("Status updated successfully").build();
        } catch (TwitterException e) {
            return Response.serverError().entity("Error in updating status").build();
        }
    }
}