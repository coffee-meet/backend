package coffeemeet.server.common.config;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.auth.implement.RefreshTokenQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith({RestDocumentationExtension.class})
public abstract class ControllerTestConfig {

  protected static final String TOKEN = "Bearer header.payload.signature";

  @Autowired
  protected ObjectMapper objectMapper;

  protected MockMvc mockMvc;

  @MockBean
  protected JwtTokenProvider jwtTokenProvider;

  @MockBean
  protected RefreshTokenQuery refreshTokenQuery;

  @BeforeEach
  void setUp(WebApplicationContext ctx, RestDocumentationContextProvider restDocumentation) {
    mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
        .apply(documentationConfiguration(restDocumentation))
        .addFilters(new CharacterEncodingFilter("UTF-8", true))
        .alwaysDo(print())
        .build();
  }

}
