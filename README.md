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

The tool relies upon Apache POI https://poi.apache.org/ to extract these "markers" using the style.
Hence the style is the Interface Specification between a SSS System Spec, SRS SW Req, artefacts and the tool.

The tool has the following objectives
1) extract markers and combine them to achieve 
     a) artefact verification 
     b) artefact filtering ... example a System Req allocated to a Component CSCI
     
2) extract markers and create traceability items.

A traceability item is some kind of abstract object that is containing a link 
1) to one "marker" in one artefact
2) to another "marker" in another artefact

The rest depends on the artefacts involved in the exercice and the purpose of the exercice.
The most advanced Use Cases , it to 
1) put a set of WORD documents docx as System Requirements containers
2) extract them and build a huge list (filter duplicates when applicable)
3) filter only those System Requirements allocated on one component CSCI
4) extract the markers from the WORD docx files defined as the set of Component CSCI Software Requirements
5) start the traceability analysis and state :
       a) that all System Requirements allocated to CSCI A are "implemented" in the Component CSCI
       b) that some System Requirements allocated to CSCI A are not "declared as implemented" in the DOCx of component CSCI
       c) that some Software Requirements are claiming to cover System Requirements where the SSS does not say that this System Requirement is allocated to component CSCI
