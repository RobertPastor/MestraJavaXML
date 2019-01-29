package test;

import gui.StatusBar;

import java.io.File;
import java.io.IOException;

import mestra.MestraFileType;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import poi.WordFile;
import configuration.ConfigurationFileXMLReader;
import configuration.MestraStylesMap;

public class TestWordFile {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        System.out.println("========Begin============");

        String configurationFilePath = "D:/MESTRA TOOL/Mestra_Tool_V3.xml";
        ConfigurationFileXMLReader configuration = new ConfigurationFileXMLReader(new File(configurationFilePath));
        
        String LongFileName = "G:/SCN_SRS-ECO_052_Rev-draft4.doc";
        //LongFileName = "G:/Test Bug Apache.doc";
        //LongFileName = "G:/Test_Bug_Apache new.doc";
        LongFileName = "G:/COOPANS SMFs/SMF-ECO-001_rev-.doc";
        LongFileName = "D:/MESTRA TOOL/MESTRA Analysis/Pierre-Florent Battin/SMF-ECO-233_rev-.doc";
        LongFileName = "D:\\MESTRA TOOL\\MESTRA Analysis\\01 - Jean Etienne BARDEY\\Jean-Etienne Bardey - June 2016\\SSS-ENG-NEO_RevB.docx";
        
        MestraStylesMap mestraStyles = configuration.getMestraStyles(MestraFileType.MestraFileTypeEnum.SSS);
        if (mestraStyles != null) {
        	WordFile aWordFile = new WordFile();
        	XWPFDocument doc = aWordFile.open(LongFileName);
            if (doc != null) {
                StatusBar statusBar = null;       
                aWordFile.ExtractMestraMarkers(doc, mestraStyles, statusBar) ;
            }
            try {
				doc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        System.out.println("========End============");
    }
}
