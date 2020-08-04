package com.google.tripmeout.frontend.servlet;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.maps.errors.ApiException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.User;


public class RequestAuthenticatorFilterTest {
  @Mock UserService userService;
  @Mock HttpServletRequest request;
  @Mock FilterChain chain;

  FakeHttpServletResponse response;
  RequestAuthenticatorFilter filter;

  @Before
  public void setUp() {
    initMocks(this);
    response = new FakeHttpServletResponse();
    filter = new RequestAuthenticatorFilter(userService);
  }

  @Test
  public void doFilter_userNotLoggedIn_returnsUnathorized() throws Exception {
    when(userService.isUserLoggedIn()).thenReturn(false);
    filter.doFilter(request, response, chain);
    assertThat(response.getStatus()).isEqualTo(401);
  }

  @Test
  public void doFilter_userIsNotGoogleDomain_returnsForbidden() throws Exception {
    when(userService.isUserLoggedIn()).thenReturn(true);
    when(userService.getCurrentUser()).thenReturn(new User("afeenster@gmail.com", "gmail", "userId"));
    filter.doFilter(request, response, chain);
    assertThat(response.getStatus()).isEqualTo(403);
  }

  @Test
  public void doFilter_userIsLoggedInFromGoogleEmail_continuesChain() throws Exception {
    when(userService.isUserLoggedIn()).thenReturn(true);
    when(userService.getCurrentUser()).thenReturn(new User("afeenster@google.com", "gmail", "userId"));
    filter.doFilter(request, response, chain);
    verify(chain).doFilter(request, response);
  }
}
