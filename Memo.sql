-- 1. 테이블 생성
CREATE TABLE memo (
    id NUMBER PRIMARY KEY,
    title VARCHAR2(100),
    content CLOB
);

-- 2. 시퀀스 생성
CREATE SEQUENCE memo_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- 3. 트리거 생성
CREATE TRIGGER memo_trigger
BEFORE INSERT ON memo
FOR EACH ROW
BEGIN
    SELECT memo_seq.NEXTVAL INTO :new.id FROM dual;
END;

