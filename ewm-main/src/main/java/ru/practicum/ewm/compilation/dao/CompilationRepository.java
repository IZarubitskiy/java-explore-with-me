package ru.practicum.ewm.compilation.dao;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.compilation.model.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long>, JpaSpecificationExecutor<Compilation> {
    interface CompilationSpecification {
        static Specification<Compilation> byPinned(Boolean pinned) {
            if (pinned == null) {
                return null;
            }
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("pinned"), pinned);
        }
    }
}