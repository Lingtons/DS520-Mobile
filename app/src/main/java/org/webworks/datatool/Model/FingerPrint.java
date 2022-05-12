package org.webworks.datatool.Model;

public class FingerPrint {
    private String fpClientIdentifier, fingerPosition, fingerPrintCapture;
    private int captureQuality;

    public String getFingerPosition() { return fingerPosition; }

    public void setFingerPosition(String fingerPosition) { this.fingerPosition = fingerPosition; }

    public String getFingerPrintCapture() { return fingerPrintCapture; }

    public void setFingerPrintCapture(String fingerPrintCapture) { this.fingerPrintCapture = fingerPrintCapture; }

    public String getFpClientIdentifier() { return fpClientIdentifier; }

    public void setFpClientIdentifier(String fpClientIdentifier) { this.fpClientIdentifier = fpClientIdentifier; }

    public int getCaptureQuality() { return captureQuality; }

    public void setCaptureQuality(int captureQuality) { this.captureQuality = captureQuality; }
}
