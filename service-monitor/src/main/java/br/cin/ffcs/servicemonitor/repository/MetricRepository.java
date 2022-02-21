package br.cin.ffcs.servicemonitor.repository;

import br.cin.ffcs.servicemonitor.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MetricRepository extends JpaRepository<Metric, Long> {
}
