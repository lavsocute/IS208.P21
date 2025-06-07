package helper;

import BUS.DonViTinhBUS;
import BUS.ThoiGianBHBUS;
import DAO.ChiTietPhieuNhapDAO;
import DAO.ChiTietPhieuXuatDAO;
import DAO.KhachHangDAO;
import DAO.NhaCungCapDAO;
import DAO.NhanVienDAO;
import DAO.PhieuNhapDAO;
import DAO.PhieuXuatDAO;
import DAO.SanPhamDAO;
import DTO.ChiTietPhieuDTO;
import DTO.PhieuNhapDTO;
import DTO.PhieuXuatDTO;
import DTO.SanPhamDTO;
import com.itextpdf.text.Chunk;
import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.Date;

public class writePDF {

    DecimalFormat formatter = new DecimalFormat("###,###,###");
    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY HH:mm");
    Document document = new Document();
    FileOutputStream file;
    JFrame jf = new JFrame();
    FileDialog fd = new FileDialog(jf, "Xuất pdf", FileDialog.SAVE);
    Font fontNormal10;
    Font fontBold15;
    Font fontBold25;
    Font fontBoldItalic15;

    DonViTinhBUS dvtBus = new DonViTinhBUS();
    ThoiGianBHBUS tgbhBus = new ThoiGianBHBUS();

    public writePDF() {
        try {
            String fontPath = "lib/TimesNewRoman/";
            fontNormal10 = new Font(BaseFont.createFont(fontPath + "SVN-Times New Roman.ttf", 
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 12, Font.NORMAL);
            fontBold25 = new Font(BaseFont.createFont(fontPath + "SVN-Times New Roman Bold.ttf", 
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 25, Font.NORMAL);
            fontBold15 = new Font(BaseFont.createFont(fontPath + "SVN-Times New Roman Bold.ttf", 
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 15, Font.NORMAL);
            fontBoldItalic15 = new Font(BaseFont.createFont(fontPath + "SVN-Times New Roman Bold Italic.ttf", 
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 15, Font.NORMAL);
        } catch (Exception e) {
            try {
                fontNormal10 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
                fontBold15 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
                fontBold25 = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
                fontBoldItalic15 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLDITALIC);
                JOptionPane.showMessageDialog(null, "Using standard fonts as custom fonts couldn't be loaded");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Critical font error: " + ex.getMessage());
            }
        }
    }

    // Các phương thức khác giữ nguyên...

    public void writePN(int maphieu) {
        String url = "";
        try {
            fd.setTitle("In phiếu nhập");
            fd.setLocationRelativeTo(null);
            url = getFile(maphieu + "");
            if (url == null || url.equals("nullnull")) {
                return;
            }
            url = url + ".pdf";
            file = new FileOutputStream(url);
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            // Phần header giữ nguyên...
            Paragraph company = new Paragraph("Hệ thống quản lý kho hàng", fontBold15);
            company.add(new Chunk(createWhiteSpace(20)));
            Date today = new Date(System.currentTimeMillis());
            company.add(new Chunk("Thời gian in phiếu: " + formatDate.format(today), fontNormal10));
            company.setAlignment(Element.ALIGN_LEFT);
            document.add(company);
            
            document.add(Chunk.NEWLINE);
            Paragraph header = new Paragraph("THÔNG TIN PHIẾU NHẬP", fontBold25);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            
            PhieuNhapDTO pn = PhieuNhapDAO.getInstance().selectById(maphieu + "");
            
            // Thông tin phiếu nhập
            Paragraph paragraph1 = new Paragraph("Mã phiếu: PN-" + pn.getMaphieu(), fontNormal10);
            String ncc = NhaCungCapDAO.getInstance().selectById(pn.getManhacungcap() + "").getTenncc();
            Paragraph paragraph2 = new Paragraph("Nhà cung cấp: " + ncc, fontNormal10);
            paragraph2.add(new Chunk(createWhiteSpace(5)));
            paragraph2.add(new Chunk("-"));
            paragraph2.add(new Chunk(createWhiteSpace(5)));
            String diachincc = NhaCungCapDAO.getInstance().selectById(pn.getManhacungcap() + "").getDiachi();
            paragraph2.add(new Chunk(diachincc, fontNormal10));

            String ngtao = NhanVienDAO.getInstance().selectById(pn.getManguoitao() + "").getHoten();
            Paragraph paragraph3 = new Paragraph("Người thực hiện: " + ngtao, fontNormal10);
            paragraph3.add(new Chunk(createWhiteSpace(5)));
            paragraph3.add(new Chunk("-"));
            paragraph3.add(new Chunk(createWhiteSpace(5)));
            paragraph3.add(new Chunk("Mã nhân viên: " + pn.getManguoitao(), fontNormal10));
            Paragraph paragraph4 = new Paragraph("Thời gian nhập: " + formatDate.format(pn.getThoigiantao()), fontNormal10);
            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(paragraph4);
            document.add(Chunk.NEWLINE);
            
            // Tạo bảng sản phẩm
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{30f, 35f, 20f, 15f, 20f});
            
            // Tiêu đề bảng
            table.addCell(new PdfPCell(new Phrase("Tên sản phẩm", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Đơn vị tính - Thời gian BH", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Giá", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Số lượng", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Tổng tiền", fontBold15)));

            // Thêm dữ liệu sản phẩm
            for (ChiTietPhieuDTO ctp : ChiTietPhieuNhapDAO.getInstance().selectAll(maphieu + "")) {
                SanPhamDTO sp = SanPhamDAO.getInstance().selectById(String.valueOf(ctp.getMaSanPham()));
                
                if (sp != null) {
                    // Tên sản phẩm
                    table.addCell(new PdfPCell(new Phrase(sp.getTensp(), fontNormal10)));
                    
                    // Đơn vị tính + Thời gian bảo hành
                    String dvtInfo = dvtBus.getTenDonViTinh(sp.getDonvitinh()) + " - " 
                                  + tgbhBus.getTenThoiGianBH(sp.getThoigianbaohanh());
                    table.addCell(new PdfPCell(new Phrase(dvtInfo, fontNormal10)));
                    
                    // Giá
                    table.addCell(new PdfPCell(new Phrase(formatter.format(ctp.getDonGia()) + "đ", fontNormal10)));
                    
                    // Số lượng
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(ctp.getSoLuong()), fontNormal10)));
                    
                    // Tổng tiền
                    table.addCell(new PdfPCell(new Phrase(formatter.format(ctp.getSoLuong() * ctp.getDonGia()) + "đ", fontNormal10)));
                } else {
                    // Trường hợp không tìm thấy sản phẩm
                    table.addCell(new PdfPCell(new Phrase("Không tìm thấy sản phẩm", fontNormal10)));
                    table.addCell(new PdfPCell(new Phrase("N/A", fontNormal10)));
                    table.addCell(new PdfPCell(new Phrase("N/A", fontNormal10)));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(ctp.getSoLuong()), fontNormal10)));
                    table.addCell(new PdfPCell(new Phrase(formatter.format(ctp.getSoLuong() * ctp.getDonGia()) + "đ", fontNormal10)));
                }
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            // Tổng thành tiền
            Paragraph paraTongThanhToan = new Paragraph(new Phrase("Tổng thành tiền: " + formatter.format(pn.getTongTien()) + "đ", fontBold15));
            paraTongThanhToan.setIndentationLeft(300);
            document.add(paraTongThanhToan);
            
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // Phần chữ ký
            Paragraph paragraph = new Paragraph();
            paragraph.setIndentationLeft(22);
            paragraph.add(new Chunk("Người lập phiếu", fontBoldItalic15));
            paragraph.add(new Chunk(createWhiteSpace(30)));
            paragraph.add(new Chunk("Nhân viên nhận", fontBoldItalic15));
            paragraph.add(new Chunk(createWhiteSpace(30)));
            paragraph.add(new Chunk("Nhà cung cấp", fontBoldItalic15));

            Paragraph sign = new Paragraph();
            sign.setIndentationLeft(23);
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
            sign.add(new Chunk(createWhiteSpace(30)));
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
            sign.add(new Chunk(createWhiteSpace(28)));
            sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
            document.add(paragraph);
            document.add(sign);

            document.close();
            writer.close();
            openFile(url);

        } catch (DocumentException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi ghi file " + url);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
public void writePX(int maphieu) {
    String url = "";
    try {
        fd.setTitle("In phiếu xuất");
        fd.setLocationRelativeTo(null);
        url = getFile(maphieu + "");
        if (url == null || url.equals("nullnull")) {
            return;
        }
        url = url + ".pdf";
        file = new FileOutputStream(url);
        document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, file);
        document.open();

        // Header
        Paragraph company = new Paragraph("Hệ thống quản lý kho hàng", fontBold15);
        company.add(new Chunk(createWhiteSpace(20)));
        Date today = new Date(System.currentTimeMillis());
        company.add(new Chunk("Thời gian in phiếu: " + formatDate.format(today), fontNormal10));
        company.setAlignment(Element.ALIGN_LEFT);
        document.add(company);
        
        document.add(Chunk.NEWLINE);
        Paragraph header = new Paragraph("THÔNG TIN PHIẾU XUẤT", fontBold25);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        
        PhieuXuatDTO px = PhieuXuatDAO.getInstance().selectById(maphieu + "");
        
        // Thông tin phiếu xuất
        Paragraph paragraph1 = new Paragraph("Mã phiếu: PX-" + px.getMaphieu(), fontNormal10);
        String khachHang = KhachHangDAO.getInstance().selectById(px.getMakh() + "").getHoten();
        Paragraph paragraph2 = new Paragraph("Khách hàng: " + khachHang, fontNormal10);
        paragraph2.add(new Chunk(createWhiteSpace(5)));
        paragraph2.add(new Chunk("-"));
        paragraph2.add(new Chunk(createWhiteSpace(5)));
        String diachiKH = KhachHangDAO.getInstance().selectById(px.getMakh() + "").getDiachi();
        paragraph2.add(new Chunk(diachiKH, fontNormal10));

        String ngtao = NhanVienDAO.getInstance().selectById(px.getManguoitao() + "").getHoten();
        Paragraph paragraph3 = new Paragraph("Người thực hiện: " + ngtao, fontNormal10);
        paragraph3.add(new Chunk(createWhiteSpace(5)));
        paragraph3.add(new Chunk("-"));
        paragraph3.add(new Chunk(createWhiteSpace(5)));
        paragraph3.add(new Chunk("Mã nhân viên: " + px.getManguoitao(), fontNormal10));
        Paragraph paragraph4 = new Paragraph("Thời gian xuất: " + formatDate.format(px.getThoigiantao()), fontNormal10);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(Chunk.NEWLINE);
        
        // Tạo bảng sản phẩm
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{30f, 35f, 20f, 15f, 20f});
        
        // Tiêu đề bảng
        table.addCell(new PdfPCell(new Phrase("Tên sản phẩm", fontBold15)));
        table.addCell(new PdfPCell(new Phrase("Đơn vị tính - Thời gian BH", fontBold15)));
        table.addCell(new PdfPCell(new Phrase("Giá", fontBold15)));
        table.addCell(new PdfPCell(new Phrase("Số lượng", fontBold15)));
        table.addCell(new PdfPCell(new Phrase("Tổng tiền", fontBold15)));

        // Thêm dữ liệu sản phẩm
        for (ChiTietPhieuDTO ctp : ChiTietPhieuXuatDAO.getInstance().selectAll(maphieu + "")) {
            SanPhamDTO sp = SanPhamDAO.getInstance().selectById(String.valueOf(ctp.getMaSanPham()));
            
            if (sp != null) {
                // Tên sản phẩm
                table.addCell(new PdfPCell(new Phrase(sp.getTensp(), fontNormal10)));
                
                // Đơn vị tính + Thời gian bảo hành
                String dvtInfo = dvtBus.getTenDonViTinh(sp.getDonvitinh()) + " - " 
                              + tgbhBus.getTenThoiGianBH(sp.getThoigianbaohanh());
                table.addCell(new PdfPCell(new Phrase(dvtInfo, fontNormal10)));
                
                // Giá
                table.addCell(new PdfPCell(new Phrase(formatter.format(ctp.getDonGia()) + "đ", fontNormal10)));
                
                // Số lượng
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ctp.getSoLuong()), fontNormal10)));
                
                // Tổng tiền
                table.addCell(new PdfPCell(new Phrase(formatter.format(ctp.getSoLuong() * ctp.getDonGia()) + "đ", fontNormal10)));
            } else {
                // Trường hợp không tìm thấy sản phẩm
                table.addCell(new PdfPCell(new Phrase("Không tìm thấy sản phẩm", fontNormal10)));
                table.addCell(new PdfPCell(new Phrase("N/A", fontNormal10)));
                table.addCell(new PdfPCell(new Phrase("N/A", fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ctp.getSoLuong()), fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(formatter.format(ctp.getSoLuong() * ctp.getDonGia()) + "đ", fontNormal10)));
            }
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        // Tổng thành tiền
        Paragraph paraTongThanhToan = new Paragraph(new Phrase("Tổng thành tiền: " + formatter.format(px.getTongTien()) + "đ", fontBold15));
        paraTongThanhToan.setIndentationLeft(300);
        document.add(paraTongThanhToan);
        
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        // Phần chữ ký
        Paragraph paragraph = new Paragraph();
        paragraph.setIndentationLeft(22);
        paragraph.add(new Chunk("Người lập phiếu", fontBoldItalic15));
        paragraph.add(new Chunk(createWhiteSpace(30)));
        paragraph.add(new Chunk("Người giao", fontBoldItalic15));
        paragraph.add(new Chunk(createWhiteSpace(30)));
        paragraph.add(new Chunk("Khách hàng", fontBoldItalic15));

        Paragraph sign = new Paragraph();
        sign.setIndentationLeft(20);
        sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
        sign.add(new Chunk(createWhiteSpace(25)));
        sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
        sign.add(new Chunk(createWhiteSpace(23)));
        sign.add(new Chunk("(Ký và ghi rõ họ tên)", fontNormal10));
        document.add(paragraph);
        document.add(sign);

        document.close();
        writer.close();
        openFile(url);

    } catch (DocumentException | FileNotFoundException ex) {
        JOptionPane.showMessageDialog(null, "Lỗi khi ghi file " + url);
        ex.printStackTrace();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
        ex.printStackTrace();
    }
}
    // Các phương thức khác giữ nguyên...
    private String getFile(String name) {
        fd.pack();
        fd.setSize(800, 600);
        fd.validate();
        Rectangle rect = jf.getContentPane().getBounds();
        double width = fd.getBounds().getWidth();
        double height = fd.getBounds().getHeight();
        double x = rect.getCenterX() - (width / 2);
        double y = rect.getCenterY() - (height / 2);
        Point leftCorner = new Point();
        leftCorner.setLocation(x, y);
        fd.setLocation(leftCorner);
        fd.setFile(name + ".pdf");
        fd.setVisible(true);
        
        String directory = fd.getDirectory();
        String filename = fd.getFile();
        
        if (directory == null || filename == null) {
            return null;
        }
        return directory + filename;
    }

    private void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static Chunk createWhiteSpace(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(" ");
        }
        return new Chunk(builder.toString());
    }
}