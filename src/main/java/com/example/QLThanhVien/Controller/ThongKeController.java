package com.example.QLThanhVien.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.QLThanhVien.Enity.ThongTinSuDungEntity;
import com.example.QLThanhVien.Repository.ThongTinSuDungRepository;

@Controller
public class ThongKeController {
	@Autowired
    private ThongTinSuDungRepository ttsdRepository;
	
	@PutMapping("/ThongKe.html")
	public ResponseEntity<Object[][]> action(@RequestParam("chon") String chon, @RequestParam(name = "search", required = false) String search) {

	    List<ThongTinSuDungEntity> listTTSD = new ArrayList<>();

	    // Kiểm tra nếu mã thành viên được cung cấp
	    if (search != null) {
	    	if (chon == "khoa") {
	    		System.out.println("chon  khoa");
	    		listTTSD = ttsdRepository.findByMaTVKhoaContaining(search);
	    	}
	    	else {
	    		System.out.println("chon  nganh");
	    		listTTSD = ttsdRepository.findByMaTVNganhContaining(search);
	    	}
	        System.out.println(listTTSD.get(0).getMaTV().getMaTV());
	    } else {
	        // Nếu không có mã thành viên, hiển thị tất cả các bản ghi
	        listTTSD = (List<ThongTinSuDungEntity>) ttsdRepository.findAll();
	    }
	    Object[][] modObjects = new Object[listTTSD.size()][7]; // 6 cột: MaTT, MaTV.MaTV, MaTB.MaTB, TGVao, TGMuon, TGTra

	    int index = 0;
	    for (ThongTinSuDungEntity t : listTTSD) {
	        // Ánh xạ các trường của ThongTinSuDungEntity vào mảng Object
	        modObjects[index][0] = t.getMaTT();
	        modObjects[index][1] = t.getMaTV() != null? t.getMaTV().getMaTV() : "";
	        modObjects[index][2] = t.getMaTB() != null? t.getMaTB().getMaTB() : ""; 
	        modObjects[index][3] = t.getTGVao() != null? t.getTGVao().toString() : "";
	        modObjects[index][4] = t.getTGMuon() != null? t.getTGMuon().toString() : "";
	        modObjects[index][5] = t.getTGTra() != null? t.getTGTra().toString() : "";
	        modObjects[index][6] = t.getTGDatCho() != null? t.getTGDatCho().toString() : "";
	        index++;
	    }

	    return new ResponseEntity<>(modObjects, HttpStatus.OK);
	}
	
	@RequestMapping("/ThongKe.html")
    public String action(Model model){
        System.out.println("Thong tin cho bang thanh vien");
        
        model.addAttribute("message", "Thống kê");
        Iterable<ThongTinSuDungEntity> listTTSD = ttsdRepository.findAll();
        model.addAttribute("listTTSD",listTTSD);

        return "ThongKe.html";
    }
}


