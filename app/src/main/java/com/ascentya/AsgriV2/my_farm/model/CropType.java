package com.ascentya.AsgriV2.my_farm.model;

public class CropType {

   private String name, value;

   public CropType(String name, String value){
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return name;
   }
}
