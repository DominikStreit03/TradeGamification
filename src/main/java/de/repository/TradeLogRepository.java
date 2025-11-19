package de.repository;

import de.model.TradeLog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TradeLogRepository extends JpaRepository<TradeLog, Long> {}