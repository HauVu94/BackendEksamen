package no.kristiania;

import jakarta.persistence.EntityManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class EntityManagerFilter implements Filter {
    private final ChatConfig config;

    public EntityManagerFilter(ChatConfig config){
        this.config = config;
    }

    @Override
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        EntityManager entityManager = config.createEntityManagerForRequest();

        if(((HttpServletRequest)request).getMethod().equals("GET")){
            chain.doFilter(request,response);
        } else {
            entityManager.getTransaction().begin();
            chain.doFilter(request,response);
            entityManager.flush();
            entityManager.getTransaction().commit();
        }
        config.cleanRequestEntityManager();


    }

}
