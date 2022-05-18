package io.mosip.print.listener.repository;

import io.mosip.print.listener.entity.PrintTracker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintTrackerRepository extends JpaRepository<PrintTracker, String> {
}
