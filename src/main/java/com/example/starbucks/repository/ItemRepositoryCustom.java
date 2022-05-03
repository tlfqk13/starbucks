package com.example.starbucks.repository;

import com.example.starbucks.dto.ItemSearchDto;
import com.example.starbucks.dto.MainItemDto;
import com.example.starbucks.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
