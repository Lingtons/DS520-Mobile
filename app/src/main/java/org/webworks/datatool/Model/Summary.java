package org.webworks.datatool.Model;


public class Summary {

    private String AgeBracket;
    private int AgeBracketId;
    private String Gender;
    private int Count;
    private String date;

    public String getAgeBracket() {
        return AgeBracket;
    }

    public void setAgeBracket(String ageBracket) {
        AgeBracket = ageBracket;
    }

    public int getAgeBracketId() {
        return AgeBracketId;
    }

    public void setAgeBracketId(int ageBracketId) {
        AgeBracketId = ageBracketId;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
