package org.webworks.datatool.Utility;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import com.satsuware.usefulviews.LabelledSpinner;


import org.webworks.datatool.Adapter.FacilityAdapter;
import org.webworks.datatool.Adapter.LgaAdapter;
import org.webworks.datatool.Adapter.ServicesNeededAdapter;
import org.webworks.datatool.Adapter.StateAdapter;
import org.webworks.datatool.Adapter.TestingPointAdapter;
import org.webworks.datatool.Adapter.TestingPointParentAdapter;
import org.webworks.datatool.Adapter.UtilAdapter;
import org.webworks.datatool.Model.Facility;
import org.webworks.datatool.Model.State;
import org.webworks.datatool.Model.Lga;
import org.webworks.datatool.Model.ServicesNeeded;
import org.webworks.datatool.Model.TestingPoint;
import org.webworks.datatool.Model.TestingPointParent;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.FacilityRepository;
import org.webworks.datatool.Repository.Repository;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class BindingMeths {
    private Context context;
    Repository repository;
    FacilityRepository facilityRepository;

    public BindingMeths(Context _context) {
        context = _context;
        repository = new Repository(_context);
        facilityRepository = new FacilityRepository(context);
    }

    public void bindSex(LabelledSpinner spinner) {
        String[] sex = context.getResources().getStringArray(R.array.gender);

        UtilAdapter adapter = new UtilAdapter(context, sex);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindMaritalStatus(LabelledSpinner spinner) {
        String[] marital_status = context.getResources().getStringArray(R.array.marital_status);

        UtilAdapter adapter = new UtilAdapter(context, marital_status);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindEmploymentStatus(LabelledSpinner spinner){
        String[] employment_status = context.getResources().getStringArray(R.array.employment_status);
        UtilAdapter adapter = new UtilAdapter(context, employment_status);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindEducationLevel(LabelledSpinner spinner){
        String[] education_level = context.getResources().getStringArray(R.array.education_level);
        UtilAdapter adapter = new UtilAdapter(context, education_level);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindYesNo(LabelledSpinner spinner){
        String[] yes_no = context.getResources().getStringArray(R.array.yes_no);
        UtilAdapter adapter = new UtilAdapter(context, yes_no);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindRecencyResult(LabelledSpinner spinner){
        String[] recency_result = context.getResources().getStringArray(R.array.final_recency_test_result);
        UtilAdapter adapter = new UtilAdapter(context, recency_result);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindRiskLevel(LabelledSpinner spinner){
        String[] risk_levels = context.getResources().getStringArray(R.array.client_risk_level);
        UtilAdapter adapter = new UtilAdapter(context, risk_levels);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindServices(LabelledSpinner spinner) {
        String[] services = context.getResources().getStringArray(R.array.services);
        ArrayList<ServicesNeeded> servicesNeededs = new ArrayList<>();
        for (String service : services) {
            ServicesNeeded serviceNeeded = new ServicesNeeded(service);
            servicesNeededs.add(serviceNeeded);
        }
        /*for (int i = 0; i < services.length; i++) {
            ServicesNeeded serviceNeeded = new ServicesNeeded(services[i]);
            servicesNeededs.add(serviceNeeded);
        }*/
        ServicesNeededAdapter adapter = new ServicesNeededAdapter(context, servicesNeededs);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindFacilities(String _lgaCode, LabelledSpinner spinnerFacility) {

        ArrayList<Facility> facilities = facilityRepository.getFacilities();
        facilities = repository.FilterLgaFacilities(_lgaCode, facilities);
        Facility facility = new Facility();
        facility.setFacilityName("Select Facility");
        facilities.add(0, facility);
        FacilityAdapter facilityAdapter = new FacilityAdapter(context, facilities);
        spinnerFacility.getSpinner().setAdapter(facilityAdapter);
    }

    public void bindTestingPoint(LabelledSpinner spinner) {
        String[] points = context.getResources().getStringArray(R.array.testing_point);

        UtilAdapter adapter = new UtilAdapter(context, points);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindHivResult(LabelledSpinner spinner){
        String[] result = context.getResources().getStringArray(R.array.hiv_results);

        UtilAdapter adapter = new UtilAdapter(context, result);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindTestedBefore(LabelledSpinner spinner){
        String[] testedBefore = context.getResources().getStringArray(R.array.tested_before);

        UtilAdapter adapter = new UtilAdapter(context, testedBefore);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindSessionType(LabelledSpinner spinner) {
        String[] session = context.getResources().getStringArray(R.array.type_of_session);

        UtilAdapter adapter = new UtilAdapter(context, session);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindIndexTestingType(LabelledSpinner spinner) {
        String[] index = context.getResources().getStringArray(R.array.index_testing);

        UtilAdapter adapter = new UtilAdapter(context, index);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindAgeGroup(LabelledSpinner spinner) {
        String[] age_group = context.getResources().getStringArray(R.array.age_group);

        UtilAdapter adapter = new UtilAdapter(context, age_group);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindRstTestResult(LabelledSpinner spinner) {
        String[] rst_test_result = context.getResources().getStringArray(R.array.rst_hiv_results);

        UtilAdapter adapter = new UtilAdapter(context, rst_test_result);
        spinner.getSpinner().setAdapter(adapter);
    }

    public void bindStateData(final LabelledSpinner spinner, final LabelledSpinner dependent) {
        try {
            final ArrayList<State> states = repository.bindStateData();
            StateAdapter stateAdapter = new StateAdapter(context, states);
            spinner.getSpinner().setAdapter(stateAdapter);
            spinner.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String stateCode = states.get(i).getState_code();
                    //LabelledSpinner lga = (LabelledSpinner)spinner.findViewById(R.id.referral_client_lga);
                    getSelectedStateLga(stateCode, dependent);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSelectedStateLga(String code, LabelledSpinner spinner) {
        try {
            final ArrayList<Lga> lgas = repository.bindLgaData(code);
            LgaAdapter lgaAdapter = new LgaAdapter(context, addSelectToList(lgas));
            spinner.getSpinner().setAdapter(lgaAdapter);


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSelectedStateLgaSelected(String code, LabelledSpinner spinner, String value) {
        try {
            final ArrayList<Lga> lgas = repository.bindLgaData(code);
            LgaAdapter lgaAdapter = new LgaAdapter(context, addSelectToList(lgas));
            spinner.getSpinner().setAdapter(lgaAdapter);
            int position = 0;
            for (Lga lga: lgas) {
                if (lga.getLga_code().equals(value)){
                  position = lgas.indexOf(lga);
                }
            }
            spinner.getSpinner().setSelection(position);


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSelectedFacilitySelected(String lgaCode, LabelledSpinner spinner, int value){
        final ArrayList<Facility> facilities = repository.bindLgaFacilityData(lgaCode);
        Facility facility = new Facility();
        facility.setFacilityName("Select Facility");
        facility.setFacilityId(0);
        facilities.add(0, facility);
        FacilityAdapter facilityAdapter = new FacilityAdapter(context, facilities);
        spinner.getSpinner().setAdapter(facilityAdapter);
        int position = 0;
        for (Facility facility1 : facilities){
            if (facility1.getFacilityId() == value){
                position = facilities.indexOf(facility1);
            }
        }
        spinner.getSpinner().setSelection(position);
    }

    private ArrayList<Lga> addSelectToList(ArrayList<Lga> lgas) {
        Lga lga = new Lga();
        lga.setLga_name("Select LGA");
        lga.setLga_code("");
        lga.setState_code("");
        lgas.add(0, lga);
        return lgas;
    }


    public String getSexType(int id) {
        String[] result = context.getResources().getStringArray(R.array.gender);
        return result[id];
    }

    public String getHivResult(int id) {
        String[] result = context.getResources().getStringArray(R.array.hiv_results);
        if (id == 0) {
            return "";
        }
        else {
            return result[id];
        }
    }

    public int getTestingPointId(String testing) {
        String[] result = context.getResources().getStringArray(R.array.testing_point);
        int id = 0;
        for (int i = 0; i < result.length; i++) {
            if (result[i].equals(testing)) {
                id = i;
                break;
            }
        }
        return id;
    }

    public String getClientIndextType(int id) {
        String[] result = context.getResources().getStringArray(R.array.index_testing);
        if (id == 0) {
            return "";
        }
        else {
            return result[id];
        }
    }

    public int getClientIndextTypeId(String indexType) {
        String[] result = context.getResources().getStringArray(R.array.index_testing);
        int id = 0;
        for (int i = 0; i < result.length; i++) {
            if (result[i].equals(indexType)) {
                id = i;
                break;
            }
        }
        return id;
    }

    public int getSessionTypeId(String sessionType){
        String[] sessions = context.getResources().getStringArray(R.array.type_of_session);
        int id = 0;
        for (int i = 0; i < sessions.length; i++) {
            if (sessions[i].equals(sessionType)) {
                id = i;
                break;
            }
        }
        return id;
    }

    public int getRstAgeGroupId(String ageGroup){
        String[] age_groups = context.getResources().getStringArray(R.array.age_group);
        int id = 0;
        for (int i = 0; i < age_groups.length; i++) {
            if (age_groups[i].equals(ageGroup)) {
                id = i;
                break;
            }
        }
        return id;
    }

    public int getRstTestResultId(String testResult){
        String[] test_results = context.getResources().getStringArray(R.array.rst_hiv_results);
        int id = 0;
        for (int i = 0; i < test_results.length; i++) {
            if (test_results[i].equals(testResult)) {
                id = i;
                break;
            }
        }
        return id;
    }

    public void bindTestingPointParentData(final LabelledSpinner spinner, final LabelledSpinner dependent) {
        try {
            final ArrayList<TestingPointParent> testingPointParents = repository.bindParentTestingPointData();
            TestingPointParentAdapter testingPointParentAdapter = new TestingPointParentAdapter(context, testingPointParents);
            spinner.getSpinner().setAdapter(testingPointParentAdapter);
            spinner.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String parentCode = testingPointParents.get(i).getParentCode();
                    getTestingPointsOfSelectedParentTP(parentCode, dependent);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTestingPointsOfSelectedParentTP(String code, LabelledSpinner spinner) {
        try {
            final ArrayList<TestingPoint> testingPoints = repository.bindTestingPointData(code);
            TestingPoint testingPoint = new TestingPoint();
            testingPoint.setTestingPointName("Select Testing Point");
            testingPoint.setTestingPointCode("");
            testingPoints.add(0,testingPoint);
            TestingPointAdapter testingPointAdapter = new TestingPointAdapter(context, testingPoints);
            spinner.getSpinner().setAdapter(testingPointAdapter);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSelectedTestingPointSelected(String code, LabelledSpinner spinner, String value) {
        try {
            final ArrayList<TestingPoint> testingPoints = repository.bindTestingPointData(code);
            TestingPoint testingPoint = new TestingPoint();
            testingPoint.setTestingPointName("Select Testing Point");
            testingPoint.setTestingPointCode("");
            testingPoints.add(0,testingPoint);
            TestingPointAdapter testingPointAdapter = new TestingPointAdapter(context, testingPoints);
            spinner.getSpinner().setAdapter(testingPointAdapter);
            int position = 0;
            for (TestingPoint testingPoint1: testingPoints) {
                if (testingPoint1.getTestingPointCode().equals(value)){
                    position = testingPoints.indexOf(testingPoint1);
                }
            }
            spinner.getSpinner().setSelection(position);


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPreviouslyTestedWithinYear(int id) {
        String[] result = context.getResources().getStringArray(R.array.tested_before);
        if (id == 0) {
            return "";
        }
        else {
            return result[id];
        }
    }

    public String getMaritalStatus(int id) {
        String[] result = context.getResources().getStringArray(R.array.marital_status);
        if (id == 0) {
            return "";
        }
        else {
            return result[id];
        }
    }

    public String getEmploymentStatus(int id) {
        String[] result = context.getResources().getStringArray(R.array.employment_status);
        if (id == 0) {
            return "";
        }
        else {
            return result[id];
        }
    }

    public String getEducationLevel(int id) {
        String[] result = context.getResources().getStringArray(R.array.education_level);
        if (id == 0) {
            return "";
        }
        else {
            return result[id];
        }
    }

    public String getRecencyResults(int id) {
        String[] result = context.getResources().getStringArray(R.array.final_recency_test_result);
        if (id == 0) {
            return "";
        }
        else {
            return result[id];
        }
    }

    public String getSessionType(int id) {
        String[] result = context.getResources().getStringArray(R.array.type_of_session);
        if (id == 0) {
            return "";
        }
        else {
            return result[id];
        }
    }









}
