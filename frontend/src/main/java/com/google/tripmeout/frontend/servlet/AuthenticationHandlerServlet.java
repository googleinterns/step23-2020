package com.google.tripmeout.frontend.servlet;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.tripmeout.frontend.AuthenticationInfoModel;
import java.io.IOException;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class AuthenticationHandlerServlet extends HttpServlet {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final long serialVersionUID = 1L;
  private static final String DEFAULT_LOGOUT_URL_REDIRECT = "/";
  private static final String DEFAULT_LOGIN_URL_REDIRECT = "/";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.atInfo().log("%s serving GET %s", AuthenticationHandlerServlet.class.getSimpleName(),
        request.getRequestURI());
    response.setContentType("application/json");

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String logoutUrl = userService.createLogoutURL(DEFAULT_LOGOUT_URL_REDIRECT);
      AuthenticationInfoModel returnInfo =
          AuthenticationInfoModel.builder().setLoggedIn(true).setReturnLink(logoutUrl).build();

      Gson gson = new Gson();
      String json = gson.toJson(returnInfo);
      response.getWriter().println(json);
    } else {
      String loginUrl = userService.createLoginURL(DEFAULT_LOGIN_URL_REDIRECT);
      AuthenticationInfoModel returnInfo =
          AuthenticationInfoModel.builder().setLoggedIn(false).setReturnLink(loginUrl).build();
      Gson gson = new Gson();
      String json = gson.toJson(returnInfo);
      response.getWriter().println(json);
    }
  }
}
