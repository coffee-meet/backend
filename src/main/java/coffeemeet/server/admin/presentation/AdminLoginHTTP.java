package coffeemeet.server.admin.presentation;

public sealed interface AdminLoginHTTP permits AdminLoginHTTP.Request {

  record Request(
      String id,
      String password
  ) implements AdminLoginHTTP {

  }

}
