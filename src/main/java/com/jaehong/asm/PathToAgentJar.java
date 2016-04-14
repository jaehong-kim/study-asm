package com.jaehong.asm;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;

final class PathToAgentJar {

    private URL thisClassLocation;

    String getPathToJarFile() {
        String jarFilePath = getPathToJarFileContainingThisClass();
        if (jarFilePath != null) {
            return jarFilePath;
        }

        throw new IllegalStateException("No jar file");
    }

    private String getPathToJarFileContainingThisClass() {
        CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();

        if (codeSource == null) {
            return null;
        }

        thisClassLocation = codeSource.getLocation();
        String jarFilePath;


        jarFilePath = findLocalJarOrZipFileFromThisClassLocation();

        return jarFilePath;
    }

    private String findLocalJarOrZipFileFromThisClassLocation() {
        String locationPath = thisClassLocation.getPath();
        File localMETAINFFile = new File(locationPath.replace("target/classes/", "META-INF.zip"));
        return localMETAINFFile.getPath();
    }
}
