package com.ascentya.AsgriV2.Interfaces_Class;

import com.ascentya.AsgriV2.Models.FarmXBuy_Model;

import java.util.List;

public interface Mycart_Interface {
    public List<FarmXBuy_Model> getList();
    public void mycart(String total, Boolean refresh);
}
