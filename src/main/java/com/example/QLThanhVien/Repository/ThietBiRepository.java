package com.example.QLThanhVien.Repository;


import com.example.QLThanhVien.Enity.ThietBiEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThietBiRepository extends CrudRepository<ThietBiEntity, Integer> {
}