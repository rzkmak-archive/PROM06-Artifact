CREATE TABLE IF NOT EXISTS session (
    id INT NOT NULL AUTO_INCREMENT,
    session_id VARCHAR(255) NOT NULL,
    notification_target_type VARCHAR(255) NOT NULL DEFAULT 'EMAIL',
    notification_target_value VARCHAR(25) NOT NULL,
    threshold_duration_in_seconds BIGINT NOT NULL,
    threshold_count BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_session_id session(session_id);

CREATE TABLE IF NOT EXISTS alert_history (
    id INT NOT NULL AUTO_INCREMENT,
    session_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_session_id alert_history(session_id);

CREATE TABLE IF NOT EXISTS screening_history (
    id INT NOT NULL AUTO_INCREMENT,
    session_id VARCHAR(255) NOT NULL,
    screening_type VARCHAR(255) NOT NULL,
    screening_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_session_id screening_history(session_id);
