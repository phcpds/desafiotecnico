--------------------------
-- Criação da Tabela de Tarefas
--------------------------

CREATE TABLE IF NOT EXISTS public.tarefas
(
    id numeric NOT NULL DEFAULT nextval('"Tarefa_id_seq"'::regclass),
    nome character varying(200) COLLATE pg_catalog."default" NOT NULL,
    descricao character varying(200) COLLATE pg_catalog."default" NOT NULL,
    dt_criacao timestamp without time zone NOT NULL,
    status character varying(1) COLLATE pg_catalog."default" NOT NULL DEFAULT 'N'::character varying,
    categoria character varying(1) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tarefas_pkey PRIMARY KEY (id)
)


ALTER TABLE IF EXISTS public.tarefas
    OWNER to postgres;
	
--------------------------
-- criação da tabela de criterios
--------------------------

CREATE TABLE IF NOT EXISTS public.criterios
(
    id numeric NOT NULL DEFAULT nextval('"Criterio_id_seq"'::regclass),
    descricao character varying(100) COLLATE pg_catalog."default" NOT NULL,
    idtarefa numeric NOT NULL,
    concluido boolean NOT NULL DEFAULT false,
    CONSTRAINT "Criterios_pkey" PRIMARY KEY (id),
    CONSTRAINT fk_tarefa FOREIGN KEY (idtarefa)
        REFERENCES public.tarefas (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

ALTER TABLE IF EXISTS public.criterios
    OWNER to postgres;
	
--------------------------
-- Criação das sequences
--------------------------


CREATE SEQUENCE IF NOT EXISTS public."Criterio_id_seq"
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1
    OWNED BY criterios.id;

ALTER SEQUENCE public."Criterio_id_seq"
    OWNER TO postgres;


CREATE SEQUENCE IF NOT EXISTS public."Tarefa_id_seq"
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public."Tarefa_id_seq"
    OWNER TO postgres;