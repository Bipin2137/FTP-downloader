package com.antariksha.ftpdownloader;

import android.os.Environment;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FTPUtils {

    private static final String FTP_DIRECTORY = "/";
    private FTPClient ftp;
    private static final String TAG = "FTPUtils";

    public boolean login(String server, String username, String password) {
        ftp = new FTPClient();
        //try to connect
        try {
            ftp.connect(server, 21);
            //login to server
            if (ftp.login(username, password)) {
                return true;
            } else {
                ftp.logout();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setConfigurations(String directory) {
        ftp.enterLocalPassiveMode();
        int reply = ftp.getReplyCode();
        //FTPReply stores a set of constants for FTP reply codes.
        if (!FTPReply.isPositiveCompletion(reply)) {
            try {
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            System.out.println("Connection Error");
        }

        //enter passive mode
        ftp.enterLocalPassiveMode();
        //get system name
        try {
            System.out.println("Remote system is " + ftp.getSystemType());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        //get current directory
        try {
            System.out.println("Current directory is " + ftp.printWorkingDirectory());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setBufferSize(1024000);
            ftp.changeWorkingDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public FTPFile[] getfiles() {
        FTPFile[] ftpFiles = new FTPFile[0];
        try {
            FTPClientConfig conf = new FTPClientConfig();
            conf.setServerTimeZoneId("UTC");
            ftp.configure(conf);

            ftpFiles = ftp.listFiles();
            if (ftpFiles != null && ftpFiles.length > 0) {
                //loop thru files
                for (FTPFile file : ftpFiles) {
                    if (file.isFile()) {
                        System.out.println("File is " + file.getName());
                    } else if (file.isDirectory()) {
                        System.out.println("Directory is " + file.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpFiles;
    }

    public long isFilePresent(String s) {
        FTPFile[] ftpFiles;
        try {
            ftp.changeWorkingDirectory(FTP_DIRECTORY);
            ftpFiles = ftp.listFiles();
            if (ftpFiles != null && ftpFiles.length > 0) {
                //loop thru files
                for (FTPFile file : ftpFiles) {
                    if (file.isFile() && file.getName().equalsIgnoreCase(s)) {
                        return file.getSize();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean downloadFile(String fileName) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/ftp");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = new File(folder, fileName);
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            return ftp.retrieveFile(fileName, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
