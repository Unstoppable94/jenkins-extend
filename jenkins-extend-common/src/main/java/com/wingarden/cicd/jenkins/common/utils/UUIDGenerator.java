
package com.wingarden.cicd.jenkins.common.utils;

import java.util.UUID;

public class UUIDGenerator {

    public static String getUUID32(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    public static String getUUID36(){
        return UUID.randomUUID().toString();
    }
}

