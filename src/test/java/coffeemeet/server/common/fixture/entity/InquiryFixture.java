package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.presentation.dto.InquiryHTTP;
import coffeemeet.server.inquiry.service.dto.InquirySearchResponse;
import java.util.List;
import org.instancio.Instancio;

public class InquiryFixture {

  public static Inquiry inquiry() {
    return Instancio.of(Inquiry.class)
        .set(field(Inquiry::isChecked), false)
        .generate(field(Inquiry::getTitle), gen -> gen.string().maxLength(20))
        .generate(field(Inquiry::getContent), gen -> gen.string().maxLength(200)).create();
  }

  public static InquiryHTTP.Request request() {
    return Instancio.of(InquiryHTTP.Request.class)
        .generate(field(InquiryHTTP.Request::title), gen -> gen.string().maxLength(20))
        .generate(field(InquiryHTTP.Request::content), gen -> gen.string().maxLength(200)).create();
  }

  public static List<Inquiry> inquiries(int size) {
    return Instancio.ofList(Inquiry.class).size(size)
        .set(field(Inquiry::isChecked), false)
        .generate(field(Inquiry::getId), gen -> gen.longSeq().start(1L))
        .generate(field(Inquiry::getTitle), gen -> gen.string().maxLength(20))
        .generate(field(Inquiry::getContent), gen -> gen.string().maxLength(200)).create();
  }

  public static List<Inquiry> inquiriesWithFixedId(int size, Long id) {
    return Instancio.ofList(Inquiry.class).size(size)
        .set(field(Inquiry::isChecked), false)
        .set(field(Inquiry::getInquirerId), id)
        .generate(field(Inquiry::getId), gen -> gen.longSeq().start(1L))
        .generate(field(Inquiry::getTitle), gen -> gen.string().maxLength(20))
        .generate(field(Inquiry::getContent), gen -> gen.string().maxLength(200)).create();
  }

  public static InquirySearchResponse inquirySearchResponse() {
    return Instancio.of(InquirySearchResponse.class)
        .create();
  }

}
