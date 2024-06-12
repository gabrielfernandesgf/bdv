package br.com.gabrielfernandes.bdv;

import java.util.HashSet;
import java.util.Set;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.sun.faces.config.ConfigureListener;
import com.sun.faces.config.FacesInitializer;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.gabrielfernandes.bdv")
public class BdvApplication implements ServletContextInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BdvApplication.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
        servletContext.setInitParameter("javax.faces.PARTIAL_STATE_SAVING_METHOD", "true");
        servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        servletContext.setInitParameter("facelets.DEVELOPMENT", "true");
        servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");

        Set<Class<?>> clazz = new HashSet<>();
        clazz.add(BdvApplication.class);

        FacesInitializer facesInitializer = new FacesInitializer();
        facesInitializer.onStartup(clazz, servletContext);
    }

    @Bean
    public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
        return new ServletListenerRegistrationBean<>(new ConfigureListener());
    }

     @Bean
    public ServletRegistrationBean<FacesServlet> facesServletRegistration() {
        ServletRegistrationBean<FacesServlet> registration = new ServletRegistrationBean<>(new FacesServlet(), "*.xhtml");
        registration.setLoadOnStartup(1);
        return registration;
    }
}
