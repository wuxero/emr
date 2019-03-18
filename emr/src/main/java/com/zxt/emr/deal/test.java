package com.zxt.emr.deal;

public class test {
    public static void main(String[] args){
        try{
            String cmdStr = "cmd /c copy d:\\1.png f:\\";
            Runtime.getRuntime().exec(cmdStr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
