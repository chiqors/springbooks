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

 Date: 31/05/2023 16:17:37
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
-- Sequence structure for logs_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."logs_id_seq";
CREATE SEQUENCE "public"."logs_id_seq" 
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
  "registered_at" timestamp(0),
  "deleted" bool,
  "book_code" text COLLATE "pg_catalog"."default",
  "updated_at" timestamp(0),
  "deleted_at" timestamp(0)
)
;

-- ----------------------------
-- Records of books
-- ----------------------------
INSERT INTO "public"."books" VALUES (3, 'Pride and Prejudice', 'Jane Austen', 8, '1813-01-28', '2020-05-20 03:00:00', 'f', 'B200520203', NULL, NULL);
INSERT INTO "public"."books" VALUES (5, 'The Catcher in the Rye', 'J.D. Salinger', 3, '1951-07-16', '2021-01-03 02:56:04', 'f', 'B030120215', NULL, NULL);
INSERT INTO "public"."books" VALUES (7, 'Great Gatsby', 'F. Scott Fitzgerald', 4, '2015-09-10', '2023-05-25 04:07:55', 't', 'B250520237', NULL, '2023-05-30 04:00:00');
INSERT INTO "public"."books" VALUES (18, 'Maze Runner', 'Brian Scott', 4, '2016-03-02', '2023-05-30 07:15:20', 'f', 'B300523145251', NULL, NULL);
INSERT INTO "public"."books" VALUES (1, 'The Great Gatsby', 'F. Scott Fitzgerald', 12, '2020-01-01', '2020-02-15 21:13:50', 'f', 'B150220201', NULL, NULL);
INSERT INTO "public"."books" VALUES (19, 'Appschef Guide Book', 'Welly Kurniawan', 4, '2016-03-02', '2023-05-30 08:10:11', 'f', 'B300523151757', NULL, NULL);
INSERT INTO "public"."books" VALUES (2, 'Great Gatsby', 'F. Scott Fitzgerald', 4, '2015-09-10', '2020-03-10 07:10:50', 'f', 'B100320202', '2023-05-31 09:23:36', NULL);
INSERT INTO "public"."books" VALUES (4, '1984', 'George Orwell', 10, '1949-06-08', '2020-09-05 03:02:04', 'f', 'B050920204', NULL, NULL);

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
INSERT INTO "public"."detail_transactions" VALUES (6, 6, 1, 2);
INSERT INTO "public"."detail_transactions" VALUES (7, 6, 2, 1);
INSERT INTO "public"."detail_transactions" VALUES (8, 7, 1, 2);
INSERT INTO "public"."detail_transactions" VALUES (9, 7, 2, 1);
INSERT INTO "public"."detail_transactions" VALUES (14, 12, 1, 2);

-- ----------------------------
-- Table structure for logs
-- ----------------------------
DROP TABLE IF EXISTS "public"."logs";
CREATE TABLE "public"."logs" (
  "timestamp" timestamp(6),
  "url_path" text COLLATE "pg_catalog"."default",
  "host_name" varchar(100) COLLATE "pg_catalog"."default",
  "http_method" varchar(8) COLLATE "pg_catalog"."default",
  "http_code" int2,
  "message" text COLLATE "pg_catalog"."default",
  "id" int8 NOT NULL DEFAULT nextval('logs_id_seq'::regclass)
)
;

-- ----------------------------
-- Records of logs
-- ----------------------------
INSERT INTO "public"."logs" VALUES ('2023-05-29 00:00:00', '/api/members', 'http://localhost:8080', 'POST', 201, 'Created member with code: M2905230', 1);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 201, 'Book created', 2);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 201, 'Book created', 3);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 201, 'Book created', 4);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 500, 'Failed to create book', 5);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 500, 'Failed to create book', 6);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 500, 'Failed to create book', 7);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 500, 'Failed to create book', 8);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 201, 'Book created', 9);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 500, 'Failed to create book', 10);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 201, 'Book created', 11);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 12);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 13);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 14);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 15);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 16);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 17);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 18);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 19);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 20);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 21);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 22);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 201, 'Book created', 23);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 400, 'Failed to create book', 24);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 25);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 400, 'Failed to update book', 26);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 400, 'Failed to update book', 27);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 28);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B290520230df', 'http://localhost:8080', 'DELETE', 404, 'Book not found', 29);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B100320202dfdf', 'http://localhost:8080', 'PUT', 400, 'Failed to update book', 30);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B290520230df', 'http://localhost:8080', 'DELETE', 404, 'Book not found', 31);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B290520230df', 'http://localhost:8080', 'DELETE', 404, 'Book not found', 32);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books', 'http://localhost:8080', 'POST', 201, 'Book created', 33);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/books/B250520237', 'http://localhost:8080', 'DELETE', 404, 'Book not found', 34);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/members', 'http://localhost:8080', 'POST', 201, 'Created member with code: M3005230', 35);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/members/M010120221', 'http://localhost:8080', 'PUT', 200, 'Updated member with code: M010120221', 36);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/members/M240520236', 'http://localhost:8080', 'DELETE', 200, 'Member with code: M240520236 deleted', 37);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/transactions', 'http://localhost:8080', 'POST', 500, 'Failed to create transaction', 38);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/transactions', 'http://localhost:8080', 'POST', 500, 'Failed to create transaction', 39);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/transactions', 'http://localhost:8080', 'POST', 500, 'Failed to create transaction', 40);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/transactions', 'http://localhost:8080', 'POST', 500, 'Failed to create transaction', 41);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/transactions', 'http://localhost:8080', 'POST', 500, 'Failed to create transaction', 42);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/transactions', 'http://localhost:8080', 'POST', 500, 'Failed to create transaction', 43);
INSERT INTO "public"."logs" VALUES ('2023-05-30 00:00:00', '/api/transactions/7', 'http://localhost:8080', 'PUT', 500, 'Failed to update transaction', 44);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/books/B100320202', 'http://localhost:8080', 'PUT', 200, 'Book updated', 45);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/members', 'http://localhost:8080', 'PUT', 400, 'Failed to update member', 46);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/members', 'http://localhost:8080', 'PUT', 400, 'Failed to update member', 47);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/members', 'http://localhost:8080', 'PUT', 400, 'Failed to update member', 48);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/members', 'http://localhost:8080', 'PUT', 200, 'Updated member with code: M3005230', 49);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/members/M2905230', 'http://localhost:8080', 'DELETE', 200, 'Member with code: M2905230 deleted', 50);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/transactions', 'http://localhost:8080', 'PUT', 200, 'Transaction updated', 51);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/transactions', 'http://localhost:8080', 'PUT', 200, 'Transaction updated', 52);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/transactions', 'http://localhost:8080', 'PUT', 400, 'Invalid form', 53);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/books/B290520230asd', 'http://localhost:8080', 'DELETE', 500, 'Failed to delete book', 54);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/books/B290520230asd', 'http://localhost:8080', 'DELETE', 400, 'Failed to delete book', 55);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/books/B290520230asd', 'http://localhost:8080', 'DELETE', 400, 'Failed to delete book', 56);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/books/B290520230asd', 'http://localhost:8080', 'DELETE', 400, 'Failed to delete book', 57);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/members/M2905230sdsd', 'http://localhost:8080', 'DELETE', 400, 'Failed to delete member', 58);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/members', 'http://localhost:8080', 'POST', 400, 'Failed to create member', 59);
INSERT INTO "public"."logs" VALUES ('2023-05-31 00:00:00', '/api/members', 'http://localhost:8080', 'PUT', 400, 'Failed to update member', 60);

-- ----------------------------
-- Table structure for members
-- ----------------------------
DROP TABLE IF EXISTS "public"."members";
CREATE TABLE "public"."members" (
  "id" int8 NOT NULL DEFAULT nextval('members_id_seq'::regclass),
  "name" varchar(75) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "phone" varchar(20) COLLATE "pg_catalog"."default",
  "registered_at" timestamp(0),
  "deleted" bool,
  "member_code" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "updated_at" timestamp(6),
  "deleted_at" timestamp(6)
)
;

-- ----------------------------
-- Records of members
-- ----------------------------
INSERT INTO "public"."members" VALUES (1, 'bahul', 'bahul@mail.com', '08323545655', '2022-01-01 09:01:50', 'f', 'M010120221', NULL, NULL);
INSERT INTO "public"."members" VALUES (6, 'chiqors', 'chiqo@mail.com', '0813234234', '2023-05-24 04:05:05', 't', 'M240520236', NULL, NULL);
INSERT INTO "public"."members" VALUES (3, 'Michael Johnson', 'michaeljohnson@example.com', '555-123-4567', '2022-03-10 09:06:10', 'f', 'M100320223', NULL, NULL);
INSERT INTO "public"."members" VALUES (2, 'Jane Smith', 'janesmith@example.com', '987-654-3210', '2022-02-15 07:10:10', 'f', 'M150220222', NULL, NULL);
INSERT INTO "public"."members" VALUES (4, 'Emily Davis', 'emilydavis@example.com', '111-222-3333', '2022-05-20 09:10:10', 'f', 'M200520224', NULL, NULL);
INSERT INTO "public"."members" VALUES (5, 'David Wilson', 'davidwilson@example.com', '999-888-7777', '2022-09-05 08:10:10', 'f', 'M050920225', NULL, NULL);
INSERT INTO "public"."members" VALUES (64, 'yahoo', 'yahoo@mail.com', '08323545655', '2023-05-30 08:00:22', 'f', 'M3005230', '2023-05-31 09:29:57.594', NULL);
INSERT INTO "public"."members" VALUES (63, 'rizki', 'rizki@mail.com', '0813234236', '2023-05-29 09:07:06', 'f', 'M2905230', NULL, '2023-05-31 09:30:42.881');

-- ----------------------------
-- Table structure for transactions
-- ----------------------------
DROP TABLE IF EXISTS "public"."transactions";
CREATE TABLE "public"."transactions" (
  "id" int8 NOT NULL DEFAULT nextval('transactions_id_seq'::regclass),
  "transaction_code" varchar(255) COLLATE "pg_catalog"."default",
  "borrowed_at" timestamp(0),
  "est_returned_at" date,
  "returned_at" timestamp(0),
  "member_id" int8 NOT NULL,
  "status" varchar(20) COLLATE "pg_catalog"."default",
  "total_books" int2,
  "operator_name" varchar(75) COLLATE "pg_catalog"."default",
  "total_fines" numeric NOT NULL DEFAULT 0,
  "updated_at" timestamp(6)
)
;

-- ----------------------------
-- Records of transactions
-- ----------------------------
INSERT INTO "public"."transactions" VALUES (1, 'T260520231230003', '2023-05-26 07:03:00', '2023-05-29', NULL, 3, 'borrowed', 3, 'John Smith', 0, NULL);
INSERT INTO "public"."transactions" VALUES (7, 'T29052023S0915081', '2023-05-29 02:03:01', '2023-05-29', NULL, 1, 'borrowed', 3, 'Kevin', 0, NULL);
INSERT INTO "public"."transactions" VALUES (2, 'T260520231235002', '2023-05-21 21:02:00', '2023-05-26', '2023-05-31 22:02:03', 2, 'returned', 2, 'Sarah Thompson', 0, NULL);
INSERT INTO "public"."transactions" VALUES (3, 'T260520231245004', '2023-05-23 03:00:57', '2023-05-24', '2023-05-26 02:03:58', 4, 'returned', 1, 'Robert Johnson', 5000, NULL);
INSERT INTO "public"."transactions" VALUES (6, 'T280520232221021', '2023-05-28 23:01:50', '2023-05-29', '2023-05-28 02:01:01', 1, 'returned', 3, 'Kevin', 0, NULL);
INSERT INTO "public"."transactions" VALUES (12, 'T31052023112323M3005230', '2023-05-31 11:23:23', '2023-05-29', '2023-05-31 13:47:45', 64, 'returned', 2, 'Kevin', 2000, '2023-05-31 13:47:44.689');

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."books_id_seq"
OWNED BY "public"."books"."id";
SELECT setval('"public"."books_id_seq"', 19, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."detail_transactions_id_seq"
OWNED BY "public"."detail_transactions"."id";
SELECT setval('"public"."detail_transactions_id_seq"', 15, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."logs_id_seq"
OWNED BY "public"."logs"."id";
SELECT setval('"public"."logs_id_seq"', 60, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."members_id_seq"
OWNED BY "public"."members"."id";
SELECT setval('"public"."members_id_seq"', 64, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."transactions_id_seq"
OWNED BY "public"."transactions"."id";
SELECT setval('"public"."transactions_id_seq"', 13, true);

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
