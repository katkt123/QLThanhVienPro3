package com.example.QLThanhVien.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.QLThanhVien.Repository.ThanhVienRepository;
import com.example.QLThanhVien.Repository.ThongTinSuDungRepository;

@Controller
public class XemViPhamController {
    @Autowired
    private ThanhVienRepository tvRepository;
    @Autowired
    private ThongTinSuDungRepository ttsdRepository;


    @RequestMapping("/XemViPham.html")
    public String action(Model model){
        System.out.println("Thong tin cho xem vi phạm");

        return "XemViPham.html";
    }
}
