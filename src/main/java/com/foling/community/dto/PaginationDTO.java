package com.foling.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious = true;
    private boolean showFirstPage = true;
    private boolean showNext = true;
    private boolean showEndPage = true;
    private Integer page;
    private Integer totalPage;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        totalPage= (totalCount + size -1 ) / size;

        if (page<1){
            this.page = 1;
        }else if (page > totalPage){
            this.page = totalPage;
        }else {
            this.page = page;
        }

        for (int i=-3;i<3;i++){
            int j = this.page + i;
            if (j >= 1 && j<= totalPage){
                pages.add(j);
            }
        }

        //是否展示上一页
        if (this.page == 1){
            showPrevious = false;
        }
        //是否展示下一页
        if (this.page == totalPage){
            showNext = false;
        }
        //是否展示首页
        if (pages.contains(1)){
            showFirstPage = false;
        }
        //是否展示尾页
        if (pages.contains(totalPage)){
            showEndPage = false;
        }

    }
}
