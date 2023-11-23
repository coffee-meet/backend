package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.presentation.dto.InquiryHTTP;
import org.instancio.Instancio;

public class InquiryFixture {

  public static Inquiry inquiry() {
    return Instancio.of(Inquiry.class)
        .generate(field(Inquiry::getTitle), gen -> gen.string().maxLength(20))
        .generate(field(Inquiry::getContent), gen -> gen.string().maxLength(200)).create();
  }

  public static InquiryHTTP.Request request() {
    return Instancio.of(InquiryHTTP.Request.class)
        .generate(field(InquiryHTTP.Request::title), gen -> gen.string().maxLength(20))
        .generate(field(InquiryHTTP.Request::content), gen -> gen.string().maxLength(200)).create();
  }

}
