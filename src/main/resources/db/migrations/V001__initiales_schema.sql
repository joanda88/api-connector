CREATE TABLE "user" (
    id character(36) NOT NULL,
    user_name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    country character varying(255) NOT NULL,
    gender character varying(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);
