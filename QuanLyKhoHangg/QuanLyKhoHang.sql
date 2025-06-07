
-- CREATE TABLES (in dependency order)

-- 1. Basic reference tables
CREATE TABLE THOIGIANBH (
    mathoigianbh NUMBER PRIMARY KEY,
    tenthgianbh NVARCHAR2(100) NOT NULL,
    trangthai NUMBER(1) DEFAULT 1 NOT NULL,  -- 0: Không hoạt động, 1: Hoạt động
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

CREATE TABLE THUONGHIEU (
    mathuonghieu NUMBER PRIMARY KEY,
    tenthuonghieu NVARCHAR2(100) NOT NULL,
    trangthai NUMBER(1) DEFAULT 1 NOT NULL,  -- 0: Không hoạt động, 1: Hoạt động
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

CREATE TABLE XUATXU (
    maxuatxu NUMBER PRIMARY KEY,
    tenxuatxu NVARCHAR2(100) NOT NULL,
    trangthai NUMBER(1) DEFAULT 1 NOT NULL,  -- 0: Không hoạt động, 1: Hoạt động
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

CREATE TABLE DONVITINH (
    madonvitinh NUMBER PRIMARY KEY,
    tendonvitinh NVARCHAR2(50) NOT NULL,
    trangthai NUMBER(1) DEFAULT 1 NOT NULL,  -- 0: Không hoạt động, 1: Hoạt động
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

CREATE TABLE KHUVUCKHO (
    makhuvuc NUMBER PRIMARY KEY,
    tenkhuvuc NVARCHAR2(100) NOT NULL,
    ghichu NVARCHAR2(500),
    trangthai NUMBER(1) DEFAULT 1 NOT NULL,  -- 0: Không hoạt động, 1: Hoạt động
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

CREATE TABLE LOAISANPHAM (
    maloai NUMBER PRIMARY KEY,
    tenloai NVARCHAR2(100) NOT NULL,
    mota NVARCHAR2(1000),
    trangthai NUMBER(1) DEFAULT 1 NOT NULL,  -- 0: Không hoạt động, 1: Hoạt động
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

-- 2. Main tables
CREATE TABLE NHANVIEN (
    manv NUMBER PRIMARY KEY,
    hoten NVARCHAR2(100) NOT NULL,
    gioitinh NUMBER(1) NOT NULL,                   -- 0: Nữ, 1: Nam
    sdt VARCHAR2(15),
    ngaysinh DATE,
    trangthai NUMBER(1) NOT NULL,                  -- 0: Không hoạt động, 1: Hoạt động
    email VARCHAR2(100),
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

CREATE TABLE KhachHang (
    maKH NUMBER PRIMARY KEY,
    hoten NVARCHAR2(100) NOT NULL,
    sdt VARCHAR2(15),
    diachi NVARCHAR2(200),
    ngaythamgia DATE,
    trangthai NUMBER DEFAULT 1 NOT NULL
);

CREATE TABLE NHACUNGCAP (
    MANHACUNGCAP NUMBER PRIMARY KEY,
    TENNHACUNGCAP NVARCHAR2(100) NOT NULL,
    DIACHI NVARCHAR2(200),
    EMAIL VARCHAR2(100),
    SDT VARCHAR2(20),
    TRANGTHAI NUMBER(1) DEFAULT 1
);

-- Modified SANPHAM table with gianhap
CREATE TABLE SANPHAM (
    masp NUMBER PRIMARY KEY,
    tensp NVARCHAR2(100) NOT NULL,
    hinhanh VARCHAR2(255),
    maloai NUMBER NOT NULL,
    mota NVARCHAR2(1000),
    soluongton NUMBER DEFAULT 0,
    gianhap NUMBER NOT NULL,                      -- Added purchase price
    giaban NUMBER NOT NULL,
    thoigianbaohanh NUMBER,
    thuonghieu NUMBER,
    xuatxu NUMBER,
    donvitinh NUMBER,
    khuvuckho NUMBER,
    trangthai NUMBER(1) DEFAULT 1 NOT NULL,  -- 0: Không hoạt động, 1: Hoạt động
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_sanpham_loaisanpham FOREIGN KEY (maloai) 
        REFERENCES LOAISANPHAM(maloai) ON DELETE CASCADE,
    CONSTRAINT fk_sanpham_thoigianbh FOREIGN KEY (thoigianbaohanh) 
        REFERENCES THOIGIANBH(mathoigianbh),
    CONSTRAINT fk_sanpham_thuonghieu FOREIGN KEY (thuonghieu) 
        REFERENCES THUONGHIEU(mathuonghieu),
    CONSTRAINT fk_sanpham_xuatxu FOREIGN KEY (xuatxu) 
        REFERENCES XUATXU(maxuatxu),
    CONSTRAINT fk_sanpham_donvitinh FOREIGN KEY (donvitinh) 
        REFERENCES DONVITINH(madonvitinh),
    CONSTRAINT fk_sanpham_khuvuckho FOREIGN KEY (khuvuckho) 
        REFERENCES KHUVUCKHO(makhuvuc),
    
    -- Check constraints
    CONSTRAINT chk_sanpham_soluongton CHECK (soluongton >= 0),
    CONSTRAINT chk_sanpham_gianhap CHECK (gianhap >= 0),
    CONSTRAINT chk_sanpham_giaban CHECK (giaban >= 0),
    CONSTRAINT chk_sanpham_trangthai CHECK (trangthai IN (0, 1))
);

CREATE TABLE TAIKHOAN (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    manv NUMBER NOT NULL,
    username VARCHAR2(50) NOT NULL,
    matkhau VARCHAR2(255) NOT NULL,               -- Lưu mật khẩu đã băm (hash)
    manhomquyen number(1) NOT NULL,                  -- ADMIN, MANAGER, STAFF...
    trangthai NUMBER(1) NOT NULL,                 -- 0: Khóa, 1: Hoạt động
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_taikhoan_nhanvien FOREIGN KEY (manv) 
        REFERENCES NHANVIEN(manv) ON DELETE CASCADE,
    CONSTRAINT uk_taikhoan_username UNIQUE (username),
    CONSTRAINT uk_taikhoan_manv UNIQUE (manv)     -- Đảm bảo 1 nhân viên chỉ có 1 tài khoản
);

-- 3. Transaction tables
CREATE TABLE PHIEUNHAP (
    maphieunhap NUMBER PRIMARY KEY,
    thoigian TIMESTAMP NOT NULL,
    manhacungcap NUMBER NOT NULL,
    nguoitao NUMBER NOT NULL,
    tongtien NUMBER NOT NULL,
    trangthai NUMBER(1) DEFAULT 1 NOT NULL,  -- 0: Hủy, 1: Hoạt động
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_phieunhap_nhacungcap FOREIGN KEY (manhacungcap) 
        REFERENCES NHACUNGCAP(MANHACUNGCAP),
    CONSTRAINT fk_phieunhap_nhanvien FOREIGN KEY (nguoitao) 
        REFERENCES NHANVIEN(manv)
);

CREATE TABLE CTPHIEUNHAP (
    maphieunhap NUMBER NOT NULL,
    masanpham NUMBER NOT NULL,
    soluong NUMBER NOT NULL,
    dongia NUMBER NOT NULL,
    hinhthucnhap NUMBER(1) NOT NULL,  -- 0: Nhập hàng, 1: Nhập trả hàng
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT pk_ctphieunhap PRIMARY KEY (maphieunhap, masanpham),
    CONSTRAINT fk_ctphieunhap_phieunhap FOREIGN KEY (maphieunhap) 
        REFERENCES PHIEUNHAP(maphieunhap) ON DELETE CASCADE,
    CONSTRAINT fk_ctphieunhap_sanpham FOREIGN KEY (masanpham) 
        REFERENCES SANPHAM(masp),
    CONSTRAINT chk_ctphieunhap_soluong CHECK (soluong > 0),
    CONSTRAINT chk_ctphieunhap_dongia CHECK (dongia > 0)
);

CREATE TABLE PHIEUXUAT (
    maphieuxuat NUMBER PRIMARY KEY,
    thoigian TIMESTAMP NOT NULL,
    makhachhang NUMBER,
    nguoitao NUMBER NOT NULL,
    tongtien NUMBER NOT NULL,
    trangthai NUMBER(1) DEFAULT 1 NOT NULL,  -- 0: Hủy, 1: Hoạt động
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_phieuxuat_khachhang FOREIGN KEY (makhachhang) 
        REFERENCES KHACHHANG(maKH),
    CONSTRAINT fk_phieuxuat_nhanvien FOREIGN KEY (nguoitao) 
        REFERENCES NHANVIEN(manv)
);

CREATE TABLE CTPHIEUXUAT (
    maphieuxuat NUMBER NOT NULL,
    masanpham NUMBER NOT NULL,
    soluong NUMBER NOT NULL,
    dongia NUMBER NOT NULL,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT pk_ctphieuxuat PRIMARY KEY (maphieuxuat, masanpham),
    CONSTRAINT fk_ctphieuxuat_phieuxuat FOREIGN KEY (maphieuxuat) 
        REFERENCES PHIEUXUAT(maphieuxuat) ON DELETE CASCADE,
    CONSTRAINT fk_ctphieuxuat_sanpham FOREIGN KEY (masanpham) 
        REFERENCES SANPHAM(masp),
    CONSTRAINT chk_ctphieuxuat_soluong CHECK (soluong > 0),
    CONSTRAINT chk_ctphieuxuat_dongia CHECK (dongia > 0)
);

CREATE TABLE CTSANPHAM (
    machitiet VARCHAR2(50) PRIMARY KEY,
    masanpham NUMBER NOT NULL,
    maphieunhap NUMBER,
    maphieuxuat NUMBER,
    tinhtrang NUMBER(1) NOT NULL,  -- 0: Hỏng, 1: Trong kho, 2: Đã bán
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_ctsanpham_sanpham FOREIGN KEY (masanpham) 
        REFERENCES SANPHAM(masp),
    CONSTRAINT fk_ctsanpham_phieunhap FOREIGN KEY (maphieunhap) 
        REFERENCES PHIEUNHAP(maphieunhap),
    CONSTRAINT fk_ctsanpham_phieuxuat FOREIGN KEY (maphieuxuat) 
        REFERENCES PHIEUXUAT(maphieuxuat),
    CONSTRAINT chk_ctsanpham_tinhtrang CHECK (tinhtrang IN (0, 1, 2))
);

-- CREATE SEQUENCES
CREATE SEQUENCE nhanvien_seq
    START WITH 1000
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE seq_thoigianbh START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_thuonghieu START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_xuatxu START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_donvitinh START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_khuvuckho START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_loaisanpham START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_sanpham START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE khachhang_seq START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE NHACUNGCAP_SEQ START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE phieunhap_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE phieuxuat_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_ctsanpham START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- CREATE TRIGGERS

-- Auto-increment ID triggers (đã sửa)
CREATE OR REPLACE TRIGGER trg_nhanvien_id
BEFORE INSERT ON NHANVIEN
FOR EACH ROW
BEGIN
    IF :NEW.manv IS NULL OR :NEW.manv = 0 THEN
        SELECT nhanvien_seq.NEXTVAL INTO :NEW.manv FROM DUAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_thoigianbh_id
BEFORE INSERT ON THOIGIANBH
FOR EACH ROW
BEGIN
    IF :NEW.mathoigianbh IS NULL OR :NEW.mathoigianbh = 0 THEN
        SELECT seq_thoigianbh.NEXTVAL INTO :NEW.mathoigianbh FROM DUAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_thuonghieu_id
BEFORE INSERT ON THUONGHIEU
FOR EACH ROW
BEGIN
    IF :NEW.mathuonghieu IS NULL OR :NEW.mathuonghieu = 0 THEN
        SELECT seq_thuonghieu.NEXTVAL INTO :NEW.mathuonghieu FROM DUAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_xuatxu_id
BEFORE INSERT ON XUATXU
FOR EACH ROW
BEGIN
    IF :NEW.maxuatxu IS NULL OR :NEW.maxuatxu = 0 THEN
        SELECT seq_xuatxu.NEXTVAL INTO :NEW.maxuatxu FROM DUAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_donvitinh_id
BEFORE INSERT ON DONVITINH
FOR EACH ROW
BEGIN
    IF :NEW.madonvitinh IS NULL OR :NEW.madonvitinh = 0 THEN
        SELECT seq_donvitinh.NEXTVAL INTO :NEW.madonvitinh FROM DUAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_khuvuckho_id
BEFORE INSERT ON KHUVUCKHO
FOR EACH ROW
BEGIN
    IF :NEW.makhuvuc IS NULL OR :NEW.makhuvuc = 0 THEN
        SELECT seq_khuvuckho.NEXTVAL INTO :NEW.makhuvuc FROM DUAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_loaisanpham_id
BEFORE INSERT ON LOAISANPHAM
FOR EACH ROW
BEGIN
    IF :NEW.maloai IS NULL OR :NEW.maloai = 0 THEN
        SELECT seq_loaisanpham.NEXTVAL INTO :NEW.maloai FROM DUAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_sanpham_id
BEFORE INSERT ON SANPHAM
FOR EACH ROW
BEGIN
    IF :NEW.masp IS NULL OR :NEW.masp = 0 THEN
        SELECT seq_sanpham.NEXTVAL INTO :NEW.masp FROM DUAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER khachhang_trg
BEFORE INSERT ON KhachHang
FOR EACH ROW
BEGIN
    IF :NEW.maKH IS NULL OR :NEW.maKH = 0 THEN
        SELECT khachhang_seq.NEXTVAL INTO :NEW.maKH FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER NHACUNGCAP_TRG
BEFORE INSERT ON NHACUNGCAP
FOR EACH ROW
BEGIN
    IF :NEW.MANHACUNGCAP IS NULL OR :NEW.MANHACUNGCAP = 0 THEN
        :NEW.MANHACUNGCAP := NHACUNGCAP_SEQ.NEXTVAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_phieunhap_id
BEFORE INSERT ON PHIEUNHAP
FOR EACH ROW
BEGIN
    IF :NEW.maphieunhap IS NULL OR :NEW.maphieunhap = 0 THEN
        SELECT phieunhap_seq.NEXTVAL INTO :NEW.maphieunhap FROM DUAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_phieuxuat_id
BEFORE INSERT ON PHIEUXUAT
FOR EACH ROW
BEGIN
    IF :NEW.maphieuxuat IS NULL OR :NEW.maphieuxuat = 0 THEN
        SELECT phieuxuat_seq.NEXTVAL INTO :NEW.maphieuxuat FROM DUAL;
    END IF;
END;
/

-- Trigger mới cho CTSANPHAM
CREATE OR REPLACE TRIGGER trg_ctsanpham_id
BEFORE INSERT ON CTSANPHAM
FOR EACH ROW
BEGIN
    IF :NEW.machitiet IS NULL THEN
        SELECT 'CTSP-' || TO_CHAR(seq_ctsanpham.NEXTVAL, 'FM00000') 
        INTO :NEW.machitiet FROM DUAL;
    END IF;
END;
/

-- Các trigger cập nhật thời gian (giữ nguyên như cũ)
-- ... (giữ nguyên các trigger updated_at)

-- CREATE INDEXES (giữ nguyên như cũ)
-- ... (giữ nguyên các index)

-- Add unique constraint for KhachHang phone number
ALTER TABLE KhachHang ADD CONSTRAINT uk_khachhang_sdt UNIQUE (sdt);

-- Chèn dữ liệu mẫu (đã sửa để không chỉ định ID)
INSERT INTO THOIGIANBH (tenthgianbh, trangthai) VALUES ('6 tháng', 1);
INSERT INTO THOIGIANBH (tenthgianbh, trangthai) VALUES ('12 tháng', 1);
INSERT INTO THOIGIANBH (tenthgianbh, trangthai) VALUES ('24 tháng', 1);

-- Thương hiệu
INSERT INTO THUONGHIEU (tenthuonghieu, trangthai) VALUES ('Samsung', 1);
INSERT INTO THUONGHIEU (tenthuonghieu, trangthai) VALUES ('Apple', 1);
INSERT INTO THUONGHIEU (tenthuonghieu, trangthai) VALUES ('Sony', 1);

-- Xuất xứ
INSERT INTO XUATXU (tenxuatxu, trangthai) VALUES ('Việt Nam', 1);
INSERT INTO XUATXU (tenxuatxu, trangthai) VALUES ('Trung Quốc', 1);
INSERT INTO XUATXU (tenxuatxu, trangthai) VALUES ('Hàn Quốc', 1);

-- Đơn vị tính
INSERT INTO DONVITINH (tendonvitinh, trangthai) VALUES ('Cái', 1);
INSERT INTO DONVITINH (tendonvitinh, trangthai) VALUES ('Chiếc', 1);
INSERT INTO DONVITINH (tendonvitinh, trangthai) VALUES ('Bộ', 1);

-- Khu vực kho
INSERT INTO KHUVUCKHO (tenkhuvuc, ghichu, trangthai) VALUES ('Kho A - Tầng 1', 'Khu vực hàng điện tử', 1);
INSERT INTO KHUVUCKHO (tenkhuvuc, ghichu, trangthai) VALUES ('Kho B - Tầng 2', 'Khu vực hàng gia dụng', 1);

-- Loại sản phẩm
INSERT INTO LOAISANPHAM (tenloai, mota, trangthai) VALUES ('Điện thoại', 'Các loại smartphone và điện thoại di động', 1);
INSERT INTO LOAISANPHAM (tenloai, mota, trangthai) VALUES ('Laptop', 'Máy tính xách tay các loại', 1);
INSERT INTO LOAISANPHAM (tenloai, mota, trangthai) VALUES ('Tai nghe', 'Tai nghe không dây và có dây', 1);

-- Nhân viên (không chỉ định manv)
INSERT INTO NHANVIEN (hoten, gioitinh, sdt, trangthai, email) 
VALUES ('Nguyễn Văn A', 1, '0912345678', 1, 'nva@company.com');

INSERT INTO NHANVIEN (hoten, gioitinh, sdt, trangthai, email) 
VALUES ('Trần Thị B', 0, '0987654321', 1, 'ttb@company.com');

-- Tài khoản
INSERT INTO TAIKHOAN (manv, username, matkhau, manhomquyen, trangthai)
VALUES (1000, 'admin', '$2a$12$w6xkC/NefCAFenWzQDbII.VyyCkFzYAf9Tj6Qe82fJnZgnkJTELRO', 0, 1);

INSERT INTO TAIKHOAN (manv, username, matkhau, manhomquyen, trangthai)
VALUES (1001, 'user', '$2a$12$w6xkC/NefCAFenWzQDbII.VyyCkFzYAf9Tj6Qe82fJnZgnkJTELRO', 1, 1);

-- Khách hàng (không chỉ định maKH)
INSERT INTO KhachHang (hoten, sdt, diachi, ngaythamgia, trangthai) VALUES 
('Lê Văn C', '0909123456', '123 Đường Lê Lợi, Q1, TP.HCM', TO_DATE('15/01/2023', 'DD/MM/YYYY'), 1);

INSERT INTO KhachHang (hoten, sdt, diachi, ngaythamgia, trangthai) VALUES 
('Phạm Thị D', '0911122334', '456 Đường Nguyễn Huệ, Q1, TP.HCM', TO_DATE('20/02/2023', 'DD/MM/YYYY'), 1);

-- Nhà cung cấp (không chỉ định MANHACUNGCAP)
INSERT INTO NHACUNGCAP (TENNHACUNGCAP, DIACHI, EMAIL, SDT, TRANGTHAI) 
VALUES ('Công ty TNHH Thương mại ABC', '123 Đường Lê Lợi, Q1, TP.HCM', 'contact@abc.com', '02838223344', 1);

INSERT INTO NHACUNGCAP (TENNHACUNGCAP, DIACHI, EMAIL, SDT, TRANGTHAI) 
VALUES ('Công ty CP Xuất nhập khẩu XYZ', '456 Đường Nguyễn Văn Linh, Q7, TP.HCM', 'info@xyz.com.vn', '02839998877', 1);

-- Sản phẩm (không chỉ định masp)
INSERT INTO SANPHAM (tensp, hinhanh, maloai, mota, soluongton, gianhap, giaban, thoigianbaohanh, thuonghieu, xuatxu, donvitinh, khuvuckho, trangthai) 
VALUES ('iPhone 14 Pro', 'iphone14.jpg', 1, 'iPhone 14 Pro 128GB', 10, 22000000, 25000000, 2, 2, 2, 1, 1, 1);

INSERT INTO SANPHAM (tensp, hinhanh, maloai, mota, soluongton, gianhap, giaban, thoigianbaohanh, thuonghieu, xuatxu, donvitinh, khuvuckho, trangthai) 
VALUES ('Galaxy S22', 's22.jpg', 1, 'Samsung Galaxy S22 256GB', 15, 18000000, 21000000, 2, 1, 3, 1, 1, 1);

INSERT INTO SANPHAM (tensp, hinhanh, maloai, mota, soluongton, gianhap, giaban, thoigianbaohanh, thuonghieu, xuatxu, donvitinh, khuvuckho, trangthai) 
VALUES ('MacBook Air M1', 'mba_m1.jpg', 2, 'MacBook Air M1 2020 8GB/256GB', 5, 22000000, 25000000, 3, 2, 2, 2, 2, 1);



-- Disable foreign key constraints temporarily to avoid issues with deletion order

COMMIT;