package com.Lee.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EasyUIDataGridResultDTO implements Serializable{

    private Long total;
    private List rows;

}
