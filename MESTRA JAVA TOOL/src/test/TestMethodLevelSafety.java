package test;

import configuration.ConfigurationFileXLSReader;
import configuration.MethodLevelSafety;

public class TestMethodLevelSafety {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String ExcelConfigurationFilePath = "D:/MESTRA TOOL/Mestra_Tool_V2_43 26_July_2007.xls";

        ConfigurationFileXLSReader configuration = new ConfigurationFileXLSReader(ExcelConfigurationFilePath);
        
        String testStr = "A-CSCI";
        MethodLevelSafety methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isSafety(methodLevelSafety,configuration);
       
        testStr = "A-CSCI/SAFETY";
        methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isSafety(methodLevelSafety,configuration);
        
        testStr = "A-CSCI/safety";
        methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isSafety(methodLevelSafety,configuration);
        
        testStr = "safety/A-CSCI";
        methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isSafety(methodLevelSafety,configuration);
        
        testStr = "I-CSCI";
        methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isMethodKnow(methodLevelSafety);
        isLevelKnown(methodLevelSafety);
        isSafety(methodLevelSafety,configuration);
        
        testStr = "A-CSC/SAFETY";
        methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isMethodKnow(methodLevelSafety);
        isLevelKnown(methodLevelSafety);
        
        testStr = "D-SYSTEM/SAFETY";
        methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isMethodKnow(methodLevelSafety);
        isLevelKnown(methodLevelSafety);
        
        testStr = "T-CSCI/SAFETY";
        methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isMethodKnow(methodLevelSafety);
        
        testStr = "R-CSCI/SAFETY";
        methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isMethodKnow(methodLevelSafety);
        
        testStr = "R-CSCI/I-SYSTEM/SAFETY";
        methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isMethodKnow(methodLevelSafety);
        
        testStr = "S-CSCI/SAFETY";
        methodLevelSafety = new MethodLevelSafety(testStr,configuration);
        isMethodKnow(methodLevelSafety);
        
        
    }
    
    private static void isMethodKnow(MethodLevelSafety methodLevelSafety) {
        if (methodLevelSafety.isOneMethodNotExisting()) {
            System.out.println(methodLevelSafety.getStrMethodLevelSafety()+" one method is not existing");
        } else {
            System.out.println(methodLevelSafety.getStrMethodLevelSafety()+" all method are existing");
        }
    }
    
    private static void isLevelKnown(MethodLevelSafety methodLevelSafety) {
        if (methodLevelSafety.isOneLevelNotExisting()) {
            System.out.println(methodLevelSafety.getStrMethodLevelSafety()+" one level is not existing");
        } else {
            System.out.println(methodLevelSafety.getStrMethodLevelSafety()+" all level are existing");
        }
    }

    
    private static void isSafety(MethodLevelSafety methodLevelSafety,ConfigurationFileXLSReader configuration) {
        if (methodLevelSafety.isSafety(configuration)) {
            System.out.println(methodLevelSafety.getStrMethodLevelSafety()+" is Safety");
        } else {
            System.out.println(methodLevelSafety.getStrMethodLevelSafety()+" is NOT Safety");
        }
    }

}
