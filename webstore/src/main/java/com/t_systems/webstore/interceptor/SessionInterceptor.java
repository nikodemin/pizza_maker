package com.t_systems.webstore.interceptor;

import com.t_systems.webstore.model.dto.OrderDto;
import com.t_systems.webstore.model.enums.OrderStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession();

        if (session.getAttribute("order") == null) {
            OrderDto order = new OrderDto();
            order.setStatus(OrderStatus.UNPAID.toString());
            order.setItems(new ArrayList<>());
            session.setAttribute("order", order);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!username.equals("anonymousUser"))
            session.setAttribute("username", username);

        return true;
    }
}
