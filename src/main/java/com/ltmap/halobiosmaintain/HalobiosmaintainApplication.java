package com.ltmap.halobiosmaintain;

import com.ltmap.halobiosmaintain.applistener.ApplicationClosedListener;
import com.ltmap.halobiosmaintain.applistener.ApplicationReadyListener;
import com.ltmap.halobiosmaintain.applistener.ApplicationStartedListener;
import com.ltmap.halobiosmaintain.applistener.ApplicationStartingListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@ServletComponentScan
public class HalobiosmaintainApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(HalobiosmaintainApplication.class);
        application.addListeners(new ApplicationStartingListener());
        application.addListeners(new ApplicationStartedListener());
        application.addListeners(new ApplicationReadyListener());
        application.addListeners(new ApplicationClosedListener());
        application.run(args);
    }

}
