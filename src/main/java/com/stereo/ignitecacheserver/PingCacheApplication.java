package com.stereo.ignitecacheserver;

import com.stereo.ignitecacheserver.config.IgniteConfig;
import com.stereo.ignitecacheserver.modal.Employee;
import com.stereo.ignitecacheserver.modal.Ping;
//import com.stereo.ignitecacheserver.repository.PingRepository;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PingCacheApplication {

    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context
//                = new AnnotationConfigApplicationContext();
//        context.register(IgniteConfig.class);
//        context.refresh();

//        PingRepository repository = context.getBean(PingRepository.class);
        IgniteConfig config = new IgniteConfig();
        IgniteCache<Integer, Ping> pingCache = config.igniteInstance().getOrCreateCache("pingCache");
        pingCache.loadCache(null);
//        IgniteCache<Integer, Ping> pingCache = config.igniteInstance().cache("pingCache");


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(
            new Runnable() {
                public void run() {
                    Ping ping = new Ping();
                    ping.setId(1);
                    ping.setTimestamp(System.currentTimeMillis());
//                    String url = "http://127.0.0.1:port/ignite?cmd=put&key=" + ping.getId() + "&val=" + ping.getTimestamp() + "&cacheName=pingCache&destId=node1";
//                    repository.save(ping);
                    pingCache.put(1, ping);
                    System.out.println("Sending data...");
                }
        }, 0, 1, TimeUnit.SECONDS);

    }

}
