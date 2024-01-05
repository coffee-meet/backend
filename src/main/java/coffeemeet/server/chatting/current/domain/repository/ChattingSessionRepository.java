package coffeemeet.server.chatting.current.domain.repository;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ChattingSessionRepository<T, ID> extends Repository<T, ID> {

  <S extends T> S save(S entity);

  Optional<T> findById(ID id);

  void deleteById(ID id);

}
