package co.com.ies.smol.repository;

import co.com.ies.smol.domain.ControlInterfaceBoard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ControlInterfaceBoard entity.
 */
@Repository
public interface ControlInterfaceBoardRepository
    extends JpaRepository<ControlInterfaceBoard, Long>, JpaSpecificationExecutor<ControlInterfaceBoard> {
    default Optional<ControlInterfaceBoard> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ControlInterfaceBoard> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ControlInterfaceBoard> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct controlInterfaceBoard from ControlInterfaceBoard controlInterfaceBoard left join fetch controlInterfaceBoard.contract left join fetch controlInterfaceBoard.interfaceBoard",
        countQuery = "select count(distinct controlInterfaceBoard) from ControlInterfaceBoard controlInterfaceBoard"
    )
    Page<ControlInterfaceBoard> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct controlInterfaceBoard from ControlInterfaceBoard controlInterfaceBoard left join fetch controlInterfaceBoard.contract left join fetch controlInterfaceBoard.interfaceBoard"
    )
    List<ControlInterfaceBoard> findAllWithToOneRelationships();

    @Query(
        "select controlInterfaceBoard from ControlInterfaceBoard controlInterfaceBoard left join fetch controlInterfaceBoard.contract left join fetch controlInterfaceBoard.interfaceBoard where controlInterfaceBoard.id =:id"
    )
    Optional<ControlInterfaceBoard> findOneWithToOneRelationships(@Param("id") Long id);
}
