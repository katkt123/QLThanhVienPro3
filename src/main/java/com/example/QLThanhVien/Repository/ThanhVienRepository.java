/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.QLThanhVien.Repository;

import com.example.QLThanhVien.Enity.ThanhVienEnity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository; 
/**
 *
 * @author ASUS
 */

public interface ThanhVienRepository extends CrudRepository<ThanhVienEnity,Long>{}
