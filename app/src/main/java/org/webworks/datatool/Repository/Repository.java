package org.webworks.datatool.Repository;

import android.content.Context;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.webworks.datatool.Model.Facility;
import org.webworks.datatool.Model.State;
import org.webworks.datatool.Model.Lga;
import org.webworks.datatool.Model.TestingPoint;
import org.webworks.datatool.Model.TestingPointParent;
import org.webworks.datatool.R;
import org.webworks.datatool.Utility.XMLParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Repository {
    private Context context;
    private FacilityRepository facilityRepository;

    public Repository(Context _context) {
        context = _context;
    }

    public ArrayList<Facility> bindLgaFacilityData(String _lga){
        facilityRepository = new FacilityRepository(context);
        final ArrayList<Facility> lgaFacilities = new ArrayList<>();
        final ArrayList<Facility> facilities = facilityRepository.getFacilities();
        for (Facility facility: facilities
             ) {

            if (facility.getLgaCode().equals(_lga)){
                lgaFacilities.add(facility);
            }
        }

        return lgaFacilities;
    }

    public ArrayList<Lga> bindLgaData(String _lga) throws XmlPullParserException, IOException {

        XMLParser parser = new XMLParser();
        final ArrayList<Lga> lgas = new ArrayList<>();
        Document doc = parser.getDomElement(readLga());
        NodeList nl = doc.getElementsByTagName("row");


        for (int i = 0; i < nl.getLength(); i++){
            Element element = (Element)nl.item(i);
            Lga lga = new Lga();
            if (parser.getValue(element, "state_code").equals(_lga)) {
                lga.setLga_code(parser.getValue(element, "lga_code"));
                lga.setLga_name(parser.getValue(element, "lga_name"));
            }
            else {
                continue;
            }
            lgas.add(lga);
        }
        return lgas;
    }

    public String getLgaCodeByStateAndIndex(String _stateCode, int _lgaPosition) throws XmlPullParserException, IOException {
        final ArrayList<Lga> lgas = bindLgaData(_stateCode);
        if (_lgaPosition != 0) _lgaPosition = _lgaPosition - 1;
        return  lgas.get(_lgaPosition).getLga_code();
    }

    public String getTestingPointCodeByParentAndIndex(String _parentCode, int _testingPointPosition) throws XmlPullParserException, IOException {
        final ArrayList<TestingPoint> testingPoints = bindTestingPointData(_parentCode);
        if (_testingPointPosition != 0) _testingPointPosition = _testingPointPosition - 1;
        return  testingPoints.get(_testingPointPosition).getTestingPointCode();
    }

    public int getIndexOfSelectedState(String _stateCode)throws XmlPullParserException, IOException{
        if (_stateCode != null){
            final ArrayList<State> states = bindStateData();
            for (State state: states
            ) {
                if (state.getState_code().equals(_stateCode)) {
                    return states.indexOf(state);
                } else {
                    continue;
                }
            }
        }
        return 0;
    }

    public int getIndexOfSelectedTestingArea(String _pCode)throws XmlPullParserException, IOException{
        if (_pCode != null){
            final ArrayList<TestingPointParent> testingPointParents = bindParentTestingPointData();
            for (TestingPointParent testingPointParent: testingPointParents
            ) {
                if (testingPointParent.getParentCode().equals(_pCode)) {
                    return testingPointParents.indexOf(testingPointParent);
                } else {
                    continue;
                }
            }
        }
        return 0;
    }


    private String readLga(){

        InputStream inputStream = context.getResources().openRawResource(R.raw.lga_xml);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toString();
    }


    public ArrayList<State> bindStateData() throws XmlPullParserException, IOException {

        XMLParser parser = new XMLParser();
        final ArrayList<State> states = new ArrayList<>();
        Document doc = parser.getDomElement(readState());
        NodeList nl = doc.getElementsByTagName("row");

        for (int i = 0; i < nl.getLength(); i++){
            Element element = (Element)nl.item(i);
            State state = new State();
            state.setState_name(parser.getValue(element, "state_name"));
            state.setState_code(parser.getValue(element, "state_code"));

            states.add(state);
        }
        return  states;
    }

    private String readState(){

        InputStream inputStream = context.getResources().openRawResource(R.raw.state_xml);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toString();
    }

    private String readParentTestingPoint(){

        InputStream inputStream = context.getResources().openRawResource(R.raw.testingpoint_parent_xml);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toString();
    }
    private String readTestingPoint(){

        InputStream inputStream = context.getResources().openRawResource(R.raw.testingpoints_xml);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toString();
    }


    public ArrayList<TestingPointParent> bindParentTestingPointData() throws XmlPullParserException, IOException {

        XMLParser parser = new XMLParser();
        final ArrayList<TestingPointParent> parentTPs = new ArrayList<>();
        Document doc = parser.getDomElement(readParentTestingPoint());
        NodeList nl = doc.getElementsByTagName("row");

        for (int i = 0; i < nl.getLength(); i++){
            Element element = (Element)nl.item(i);
            TestingPointParent testingPointParent = new TestingPointParent();
            testingPointParent.setParentCode(parser.getValue(element, "parent_code"));
            testingPointParent.setParentName(parser.getValue(element, "parent_name"));

            parentTPs.add(testingPointParent);
        }
        return  parentTPs;
    }
    public ArrayList<TestingPoint> bindTestingPointData(String _parentCode) throws XmlPullParserException, IOException {

        XMLParser parser = new XMLParser();
        final ArrayList<TestingPoint> testingPoints = new ArrayList<>();
        Document doc = parser.getDomElement(readTestingPoint());
        NodeList nl = doc.getElementsByTagName("row");


        for (int i = 0; i < nl.getLength(); i++){
            Element element = (Element)nl.item(i);
            TestingPoint testingPoint = new TestingPoint();
            if (parser.getValue(element, "parent_code").equals(_parentCode)) {
                testingPoint.setTestingPointCode(parser.getValue(element, "tp_code"));
                testingPoint.setTestingPointName(parser.getValue(element, "tp_name"));
            }
            else {
                continue;
            }
            testingPoints.add(testingPoint);
        }
        return testingPoints;
    }

    public ArrayList<Facility> FilterLgaFacilities(String _lga, ArrayList<Facility> facilities){

        final ArrayList<Facility> lgaFacilities = new ArrayList<>();
        for (Facility facility: facilities
        ) {

            if (facility.getLgaCode().equals(_lga)){
                lgaFacilities.add(facility);
            }
        }

        return lgaFacilities;
    }

}
