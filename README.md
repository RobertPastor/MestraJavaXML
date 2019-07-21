# MestraJavaXML
Mestra Java Mestra with XML config

MESTRA is a french NORM to manage traceability between development artefacts.

In one word, MESTRA is a litest Requirements / Test Specification management tool.
It is hence the DOORS lite for a poor project willing to master its requirements , tests etc.

MESTRA allows to defined styles inside Word DOCX documents in order to consider them
1) as System Requirements
2) as System Tests covering the System Requirements
3) as Software Requirements etc.
4) Software Tests

The tool relies upon Apache POI to extract these "markers" using the style.
Hence the style in the Interface Specification between a SSS System Spec, SRS SW Req, artefacts and the tool.

The tool has to objectives
1) extract markers and combine them to achieve 
     a) artefact verification 
     b) artefact filtering ... example a System Req allocated to a Component CSCI
     
2) extract markers and create traceability items.

A traceability item is some kind of abstract object that is containing a link 
1) to one "marker" in one artefact
2) to another "marker" in another artefact

The rest depends on the artefacts involved in the exercice.
