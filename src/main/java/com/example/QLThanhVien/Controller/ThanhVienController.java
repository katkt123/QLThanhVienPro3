/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.QLThanhVien.Controller;


import com.example.QLThanhVien.Enity.ThanhVienEntity;
import com.example.QLThanhVien.Enity.ThongTinSuDungEntity;
import com.example.QLThanhVien.Enity.XuLyViPhamEntity;
import com.example.QLThanhVien.Repository.ThanhVienRepository;
import com.example.QLThanhVien.Repository.ThietBiRepository;
import com.example.QLThanhVien.Repository.ThongTinSuDungRepository;
import com.example.QLThanhVien.Repository.XuLyViPhamRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author ASUS
 */
@Controller
public class ThanhVienController {

    @Autowired
    private ThanhVienRepository tvRepository;

    @Autowired
    private ThietBiRepository tbRepository;

    @Autowired
    private ThongTinSuDungRepository ttsdRepository;

    @Autowired
    private XuLyViPhamRepository  xlvpRepository;

//    @GetMapping("/ThanhVien") // Đổi URL thành /thanh-vien/all
//    public String getAll(Model m)
//    {
//        System.out.println("get aLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
//        Iterable<ThanhVienEnity> list= tvRepository.findAll();
//        m.addAttribute("data",list);
//        return "ThanhVien";
//    }
    @RequestMapping("/ThanhVien.html")
    public String sayHello(Model model){
        model.addAttribute("message", "Quản lý thành viên");
        Iterable<ThanhVienEntity> list= tvRepository.findAll();

        Iterable<ThongTinSuDungEntity> listtt=ttsdRepository.findAll();
        List<ThongTinSuDungEntity> filteredList = new ArrayList<>();

        for (ThongTinSuDungEntity entity : listtt) {
            if (entity.getTGMuon() != null && entity.getTGTra() != null) {
                filteredList.add(entity);
            }
        }
        model.addAttribute("data",list);
        model.addAttribute("data1",filteredList);
        model.addAttribute("ListMaTV",list);

        return "ThanhVien.html";
    }


    @PutMapping("/ThanhVien.html")
    public void editThanhVien(@RequestParam(name = "MaTV") Integer maTV, @RequestParam(name = "Ten") String ten, @RequestParam(name = "Khoa") String khoa, @RequestParam(name = "Nganh") String nganh, @RequestParam(name = "SDT") String sdt, @RequestParam(name = "Email") String email, @RequestParam(name = "Password") String password, Model model) {

        ThanhVienEntity tvEntity = new ThanhVienEntity(maTV,ten,khoa,nganh,sdt,password,email);

        tvRepository.save(tvEntity);

    }




    @PatchMapping("/ThanhVien.html")
    public ResponseEntity<String> addExcel(@RequestParam("file") MultipartFile file) {
        try {
            ArrayList<ThanhVienEntity> list_excel = new ArrayList<>();
            byte[] fileBytes = file.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên

            Row titleRow = sheet.getRow(1);

            Cell titleMaCell = titleRow.getCell(0);
            Cell titleTenCell = titleRow.getCell(1);
            Cell titleKhoaCell = titleRow.getCell(2);
            Cell titleNganhCell = titleRow.getCell(3);
            Cell titleSDTCell = titleRow.getCell(4);
            Cell titleEmailCell = titleRow.getCell(5);
            Cell titlePasswordCell = titleRow.getCell(6);

            // Kiểm tra cấu trúc của file Excel
            if (titleMaCell != null && titleTenCell != null && titleKhoaCell != null && titleNganhCell != null && titleSDTCell != null && titleEmailCell != null && titlePasswordCell != null) {
                // Lặp qua các hàng của sheet (bắt đầu từ hàng thứ 2)
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        Cell idCell = row.getCell(0);
                        Cell nameCell = row.getCell(1);
                        Cell khoaCell = row.getCell(2);
                        Cell nganhCell = row.getCell(3);
                        Cell sdtCell = row.getCell(4);
                        Cell passCell = row.getCell(5);
                        Cell emailCell = row.getCell(6);

                        // Kiểm tra và lưu dữ liệu vào Hibernate
                        if (idCell != null && nameCell != null && khoaCell != null && nganhCell != null && sdtCell != null) {
                            int idCode = 0;
                            if (idCell.getCellType() == CellType.NUMERIC) {
                                idCode = (int) idCell.getNumericCellValue();
                            } else if (idCell.getCellType() == CellType.STRING) {
                                String id = idCell.getStringCellValue();
                                idCode = Integer.parseInt(id);
                            }
                            String name = nameCell.getStringCellValue();
                            String khoa = khoaCell.getStringCellValue();
                            String nganh = nganhCell.getStringCellValue();
                            String sdt = sdtCell.getStringCellValue();
                            String pass = passCell == null ? "" : passCell.getStringCellValue(); // Kiểm tra cell passCell có null hay không
                            String email = emailCell == null ? "" : emailCell.getStringCellValue(); // Kiểm tra cell emailCell có null hay không

                            // Tạo đối tượng ThanhVienEntity từ dữ liệu
                            ThanhVienEntity thanhVien = new ThanhVienEntity(idCode, name, khoa, nganh, sdt, pass, email);

                            // Lưu đối tượng vào cơ sở dữ liệu bằng Hibernate
                            tvRepository.save(thanhVien);
                        }
                    }
                }
            }

            return ResponseEntity.ok("Thêm thành công"); // Trả về thông báo thành công
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Đã xảy ra lỗi khi thêm thành viên"); // Trả về thông báo lỗi nếu có lỗi xảy ra
        }
    }


    @DeleteMapping("/ThanhVien.html")
    public void deleteThanhVien(@RequestBody List<Integer> list_id){

        for (Integer id : list_id){

            // Danh sach thong tin su dung
            Iterable<ThongTinSuDungEntity> listTT = ttsdRepository.findAll();

            Iterable<XuLyViPhamEntity> listVP = xlvpRepository.findAll();

            // Danh sách thiet bi
            Optional<ThanhVienEntity> temp = tvRepository.findById(Long.valueOf(id));

            if (temp.isPresent()) {

                // Ep kieu thanh kieu thietbiEntity
                ThanhVienEntity tVien = temp.get();

                if(CheckMuonvaDatCho((List<ThongTinSuDungEntity>) listTT,tVien.getMaTV()) && CheckVP((List<XuLyViPhamEntity>) listVP,tVien.getMaTV())){
                    tvRepository.deleteById(Long.valueOf(tVien.getMaTV()));
                }


            }
        }

    }
    public boolean CheckMuonvaDatCho (List<ThongTinSuDungEntity> list,int idTV){
        for (ThongTinSuDungEntity temp : list){
            if (temp.getMaTV() != null) {
                if (temp.getMaTV().getMaTV() == idTV) {
                    if (temp.getTGVao() == null) {
                        return false;
                    }
                }
            }
        }
        for (ThongTinSuDungEntity temp : list){
            if (temp.getMaTV() != null) {
                if (temp.getMaTV().getMaTV() == idTV) {
                    if (temp.getTGVao() != null) {
                        List<Integer> arr= tvRepository.getIDByMaTV(idTV);
                        for(Integer a:arr){
                            ttsdRepository.deleteById(a);
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean CheckVP(List<XuLyViPhamEntity> list,int idTV){
        for(XuLyViPhamEntity arr: list){
            if(arr.getMaTV().getMaTV()==idTV && arr.getTrang_thaixl()==0){
                return false;
            }
        }
        return true;
    }

    @PutMapping(path = "/ThanhVien.html", consumes = "application/json")
    public void traThietBi(@RequestBody Map<String, Object> requestBody){
        String maTT = (String) requestBody.get("personIdtt");
        Integer idTT = Integer.parseInt(maTT);
        Optional<ThongTinSuDungEntity> ttOptional = ttsdRepository.findById(idTT);

        if (ttOptional.isPresent()) {
            ThongTinSuDungEntity tt = ttOptional.get();
            tt.setTGTra(null);
            ttsdRepository.save(tt);
        } else {
            System.out.println("Thất bại!!!");
        }
    }


    @PostMapping(path = "/ThanhVien.html", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> enterKhuVuc(@RequestBody Map<String, Object> requestBody) {
        String maTVString = (String) requestBody.get("maTV");
        Integer maTV = Integer.parseInt(maTVString);

        ThanhVienEntity thanhVien = tvRepository.findById(Long.valueOf(maTV)).orElse(null);

        List<XuLyViPhamEntity> listxlvp = xlvpRepository.findByMaTV(maTV);
        boolean isViolating = !listxlvp.isEmpty();



        Map<String, Object> response = new HashMap<>();
        response.put("thanhVien",thanhVien);
        response.put("isViolating", isViolating);

        if (thanhVien != null) {
            // Nếu Thành viên được tìm thấy, trả về thông tin Thành viên dưới dạng JSON
            Date date=new Date();
            ThongTinSuDungEntity ttsd = new ThongTinSuDungEntity(thanhVien,date);
            ttsdRepository.save(ttsd);
            return ResponseEntity.ok(response);
        } else {
            // Nếu không tìm thấy Thành viên, trả về mã lỗi 404 (Not Found)
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/ThanhVien.html")
    public ResponseEntity<String> addMember(
                          @RequestParam(name = "Ten") String tenTV,
                          @RequestParam(name = "Khoa") String khoa,
                          @RequestParam(name = "Nganh") String nganh,
                          @RequestParam(name = "SDT") String sdt,
                          @RequestParam(name = "Email") String email,
                          @RequestParam(name = "Password") String password,
                          Model model) {

        Iterable<ThanhVienEntity> listtv= tvRepository.findAll();

        for (ThanhVienEntity tv : listtv) {
            if (tv.getSDT().equals(sdt)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại đã tồn tại");
            }
            if (tv.getEmail().equals(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email đã tồn tại");
            }
        }

            Long count = tvRepository.countAll();
            LocalDate currentDate = LocalDate.now();
            String year = String.valueOf(currentDate.getYear()).substring(2);
            String khoaCode = "00";
            String idTV;
            switch (khoa.toUpperCase(Locale.ROOT)) {
                case "SP KHXH":
                    switch (nganh.toUpperCase(Locale.ROOT)) {
                        case "ĐỊA":
                            khoaCode = "11";
                            break;
                        case "SỬ":
                            khoaCode = "10";
                            break;
                        case "VĂN":
                            khoaCode = "09";
                            break;
                        default:
                            khoaCode = "00";
                            break;
                    }
                    break;
                case "SP KHTN":
                    switch (nganh.toUpperCase(Locale.ROOT)) {
                        case "LÍ":
                            khoaCode = "02";
                            break;
                        case "HÓA":
                            khoaCode = "03";
                            break;
                        case "SINH":
                            khoaCode = "04";
                            break;
                        default:
                            khoaCode = "00";
                            break;
                    }
                    break;
                case "NGOẠI NGỮ":
                    switch (nganh.toUpperCase(Locale.ROOT)) {
                        case "ANH":
                            khoaCode = "13";
                            break;
                        case "NNA":
                            khoaCode = "38";
                            break;
                        default:
                            khoaCode = "00";
                            break;
                    }
                    break;
                case "QTKD":
                    khoaCode = "55";
                    break;
                case "QLGD":
                    switch (nganh.toUpperCase(Locale.ROOT)) {
                        case "TLH":
                            khoaCode = "53";
                            break;
                        default:
                            khoaCode = "00";
                            break;
                    }
                    break;
                case "TOÁN UD":
                    switch (nganh.toUpperCase(Locale.ROOT)) {
                        case "TOÁN":
                            khoaCode = "48";
                            break;
                        default:
                            khoaCode = "00";
                            break;
                    }
                    break;
                case "CNTT":
                    switch (nganh.toUpperCase(Locale.ROOT)) {
                        case "CNTT":
                            khoaCode = "41";
                            break;
                        case "KTPM":
                            khoaCode = "42";
                            break;
                        case "HTTT":
                            khoaCode = "43";
                            break;
                        default:
                            khoaCode = "00";
                            break;
                    }
                    break;
                default:
                    khoaCode = "00";
                    break;
            }

            idTV= "11"+ year + khoaCode + String.format("%04d", count + 1);
            int maTV= Integer.parseInt(idTV);



            ThanhVienEntity thanhVienEntity = new ThanhVienEntity(maTV, tenTV, khoa, nganh, sdt, password, email);

            // Lưu thông tin thành viên vào cơ sở dữ liệu
            tvRepository.save(thanhVienEntity);
            return ResponseEntity.ok("Thành viên đã được thêm thành công");


    }


//    @RequestMapping("/edit/{MaTV}")
//    public String showEdit(@PathVariable("MaTV") int maTV, Model model){
//        ThanhVienEnity thanhVien = tvRepository.findById(Long.valueOf(maTV)).orElse(null);
//        System.out.println(thanhVien.getTen());
//        if (thanhVien != null) {
//            // Truyền thông tin thành viên vào model để hiển thị trên form
//            model.addAttribute("thanhVien", thanhVien);
//            return "editForm.html"; // Trả về view chứa form chỉnh sửa
//        }
//        return "redirect:/ThanhVien.html";
//    }
//    @PostMapping("/edit/{maTV}")
//    public String processEditForm(@PathVariable("maTV") int maTV, @ModelAttribute ThanhVienEnity thanhVien) {
//        // Lưu thông tin thành viên đã chỉnh sửa vào cơ sở dữ liệu
//        tvRepository.save(thanhVien);
//        // Chuyển hướng đến trang hiển thị danh sách thành viên sau khi chỉnh sửa thành công
//        return "redirect:/ThanhVien.html"; // Thay "/ThanhVien.html" bằng URL của trang hiển thị danh sách thành viên của bạn
//    }
}
