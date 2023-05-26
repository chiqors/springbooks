/*
 Navicat Premium Data Transfer

 Source Server         : Postgres
 Source Server Type    : PostgreSQL
 Source Server Version : 150001 (150001)
 Source Host           : localhost:5432
 Source Catalog        : springbooks
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 150001 (150001)
 File Encoding         : 65001

 Date: 26/05/2023 16:28:14
*/


-- ----------------------------
-- Sequence structure for books_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."books_id_seq";
CREATE SEQUENCE "public"."books_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for detail_transactions_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."detail_transactions_id_seq";
CREATE SEQUENCE "public"."detail_transactions_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for members_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."members_id_seq";
CREATE SEQUENCE "public"."members_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for transactions_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."transactions_id_seq";
CREATE SEQUENCE "public"."transactions_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for books
-- ----------------------------
DROP TABLE IF EXISTS "public"."books";
CREATE TABLE "public"."books" (
  "id" int8 NOT NULL DEFAULT nextval('books_id_seq'::regclass),
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "author" varchar(75) COLLATE "pg_catalog"."default",
  "stock" int2,
  "published_at" date,
  "registered_at" date,
  "deleted" bool
)
;

-- ----------------------------
-- Records of books
-- ----------------------------
INSERT INTO "public"."books" VALUES (1, 'The Great Gatsby', 'F. Scott Fitzgerald', 10, '2020-01-01', '2020-02-15', 'f');
INSERT INTO "public"."books" VALUES (2, 'To Kill a Mockingbird', 'Harper Lee', 5, '1960-07-11', '2020-03-10', 'f');
INSERT INTO "public"."books" VALUES (3, 'Pride and Prejudice', 'Jane Austen', 8, '1813-01-28', '2020-05-20', 'f');
INSERT INTO "public"."books" VALUES (4, '1984', 'George Orwell', 12, '1949-06-08', '2020-09-05', 'f');
INSERT INTO "public"."books" VALUES (5, 'The Catcher in the Rye', 'J.D. Salinger', 3, '1951-07-16', '2021-01-03', 'f');
INSERT INTO "public"."books" VALUES (7, 'Great Gatsby', 'F. Scott Fitzgerald', 4, '2015-09-10', '2023-05-25', 't');

-- ----------------------------
-- Table structure for detail_transactions
-- ----------------------------
DROP TABLE IF EXISTS "public"."detail_transactions";
CREATE TABLE "public"."detail_transactions" (
  "id" int8 NOT NULL DEFAULT nextval('detail_transactions_id_seq'::regclass),
  "transaction_id" int8 NOT NULL,
  "book_id" int8 NOT NULL,
  "total" int2
)
;

-- ----------------------------
-- Records of detail_transactions
-- ----------------------------
INSERT INTO "public"."detail_transactions" VALUES (1, 1, 1, 2);
INSERT INTO "public"."detail_transactions" VALUES (2, 1, 3, 1);
INSERT INTO "public"."detail_transactions" VALUES (3, 2, 2, 1);
INSERT INTO "public"."detail_transactions" VALUES (4, 2, 4, 1);
INSERT INTO "public"."detail_transactions" VALUES (5, 3, 5, 1);

-- ----------------------------
-- Table structure for members
-- ----------------------------
DROP TABLE IF EXISTS "public"."members";
CREATE TABLE "public"."members" (
  "id" int8 NOT NULL DEFAULT nextval('members_id_seq'::regclass),
  "name" varchar(75) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "phone" varchar(20) COLLATE "pg_catalog"."default",
  "registered_at" date,
  "deleted" bool
)
;

-- ----------------------------
-- Records of members
-- ----------------------------
INSERT INTO "public"."members" VALUES (1, 'John Doe', 'johndoe@example.com', '123-456-7890', '2022-01-01', 'f');
INSERT INTO "public"."members" VALUES (2, 'Jane Smith', 'janesmith@example.com', '987-654-3210', '2022-02-15', 'f');
INSERT INTO "public"."members" VALUES (3, 'Michael Johnson', 'michaeljohnson@example.com', '555-123-4567', '2022-03-10', 'f');
INSERT INTO "public"."members" VALUES (4, 'Emily Davis', 'emilydavis@example.com', '111-222-3333', '2022-05-20', 'f');
INSERT INTO "public"."members" VALUES (5, 'David Wilson', 'davidwilson@example.com', '999-888-7777', '2022-09-05', 'f');
INSERT INTO "public"."members" VALUES (6, 'chiqors', 'chiqo@mail.com', '0813234234', '2023-05-24', 't');

-- ----------------------------
-- Table structure for transactions
-- ----------------------------
DROP TABLE IF EXISTS "public"."transactions";
CREATE TABLE "public"."transactions" (
  "id" int8 NOT NULL DEFAULT nextval('transactions_id_seq'::regclass),
  "transaction_code" varchar(255) COLLATE "pg_catalog"."default",
  "borrowed_at" date,
  "act_returned_at" date,
  "returned_at" date,
  "member_id" int8,
  "status" varchar(20) COLLATE "pg_catalog"."default",
  "total_books" int2,
  "operator_name" varchar(75) COLLATE "pg_catalog"."default",
  "total_fines" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of transactions
-- ----------------------------
INSERT INTO "public"."transactions" VALUES (1, 'T260520231230001', '2023-05-26', NULL, NULL, 3, 'borrowed', 3, 'John Smith', NULL);
INSERT INTO "public"."transactions" VALUES (2, 'T260520231235002', '2023-05-21', '2023-05-26', '2023-05-26', 2, 'returned', 2, 'Sarah Thompson', NULL);
INSERT INTO "public"."transactions" VALUES (3, 'T260520231245003', '2023-05-23', '2023-05-24', '2023-05-26', 4, 'returned', 1, 'Robert Johnson', '5000');

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."books_id_seq"
OWNED BY "public"."books"."id";
SELECT setval('"public"."books_id_seq"', 7, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."detail_transactions_id_seq"
OWNED BY "public"."detail_transactions"."id";
SELECT setval('"public"."detail_transactions_id_seq"', 5, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."members_id_seq"
OWNED BY "public"."members"."id";
SELECT setval('"public"."members_id_seq"', 6, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."transactions_id_seq"
OWNED BY "public"."transactions"."id";
SELECT setval('"public"."transactions_id_seq"', 3, true);

-- ----------------------------
-- Primary Key structure for table books
-- ----------------------------
ALTER TABLE "public"."books" ADD CONSTRAINT "books_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table detail_transactions
-- ----------------------------
ALTER TABLE "public"."detail_transactions" ADD CONSTRAINT "detail_transactions_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table members
-- ----------------------------
ALTER TABLE "public"."members" ADD CONSTRAINT "members_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table transactions
-- ----------------------------
ALTER TABLE "public"."transactions" ADD CONSTRAINT "transactions_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table detail_transactions
-- ----------------------------
ALTER TABLE "public"."detail_transactions" ADD CONSTRAINT "book_id_fk" FOREIGN KEY ("book_id") REFERENCES "public"."books" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."detail_transactions" ADD CONSTRAINT "transaction_id_fk" FOREIGN KEY ("transaction_id") REFERENCES "public"."transactions" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table transactions
-- ----------------------------
ALTER TABLE "public"."transactions" ADD CONSTRAINT "member_id_fk" FOREIGN KEY ("member_id") REFERENCES "public"."members" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
