# governance management system
High-Level System Architecture
                               +----------------------+
                               |      Client          |
                               |  Postman / Browser   |
                               +----------+-----------+
                                          |
                                          | REST API
                                          |
                                          v
                    +-----------------------------------------+
                    |        Governance Service               |
                    |-----------------------------------------|
                    | Controller                              |
                    | Service (Business Logic)                |
                    | Repository                              |
                    | Mapper                                  |
                    | DTOs                                    |
                    | Entity                                  |
                    +-------------------+---------------------+
                                        |
                         Save Policy     |
                                        v
                             +----------------------+
                             | governance_db        |
                             | PostgreSQL           |
                             +----------------------+
                                        |
                                        |
                         Publish Event   |
                                        |
                                        v
                             +----------------------+
                             |     Apache Kafka     |
                             |      Topic           |
                             | governance-events    |
                             +----------------------+
                                        |
                                        |
                           Consume Event |
                                        |
                                        v
                    +-----------------------------------------+
                    |          Audit Service                  |
                    |-----------------------------------------|
                    | Kafka Consumer                          |
                    | Audit Service                           |
                    | Repository                              |
                    | Audit Entity                            |
                    +-------------------+---------------------+
                                        |
                                        |
                                        v
                             +----------------------+
                             | audit_db             |
                             | PostgreSQL           |
                             +----------------------+
