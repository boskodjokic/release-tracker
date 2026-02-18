CREATE TABLE releases (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          status VARCHAR(50) NOT NULL,
                          release_date DATE,
                          created_at DATETIME NOT NULL,
                          last_update_at DATETIME NOT NULL,
                          PRIMARY KEY (id)
);

CREATE INDEX idx_releases_status ON releases(status);
CREATE INDEX idx_releases_release_date ON releases(release_date);