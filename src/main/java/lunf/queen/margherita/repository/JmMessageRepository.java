package lunf.queen.margherita.repository;

import lunf.queen.margherita.entity.JmMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JmMessageRepository extends JpaRepository<JmMessage, Long> {
}
