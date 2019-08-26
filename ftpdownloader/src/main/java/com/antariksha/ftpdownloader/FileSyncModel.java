package com.antariksha.ftpdownloader;

import java.io.Serializable;

public class FileSyncModel implements Serializable {

    private String name,classname, subject, resourcename;
    private int unitno, downloadStatus;

    public String getResourcename() {
        return resourcename;
    }

    public void setResourcename(String resourcename) {
        this.resourcename = resourcename;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getUnitno() {
        return unitno;
    }

    public void setUnitno(int unitno) {
        this.unitno = unitno;
    }
}
