package com.example.mediaplayer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Metadata {
    public static void main(String[] args) {
        storeMetadata();
    }


    private static String[] getMetadataFromFile(File file) {
        String fileName = file.getName();

        // Get the last modified time
        String date = formatDate(file.lastModified());

        // Get the file size
        String fileSize = formatSize(file.length());

        // Get the file duration


        //String duration = getVideoDuration(file); -- unfinished
        //return new String[]{fileName, date, fileSize, duration};

        return new String[]{fileName, date, fileSize};
    }

    /**
     * Retrieves metadata for all files in a specified directory and prints it to the console.
     * Metadata includes filename, last modified date, file size, and video duration (if applicable).
     */
    public static void storeMetadata() {
        String filepath = "/Users/winnex/Desktop/MediaPlayer/Media"; // location of media
        List<String[]> metadataList = new ArrayList<>(); // arraylist object to hold metadata

        File folder = new File(filepath); // Create a File object representing the directory at the specified file path - in this case the media folder
        File[] files = folder.listFiles(); // retrieve all files in the folder

        if (files != null) { // if no files are found, stop
            for (File file : files) {

                if (file.isFile()) { // checks if the file is of the file datatype
                    String[] metadata = getMetadataFromFile(file); // retrieve metadata from each file
                    metadataList.add(metadata); // add metadata to list for each file
                }
            }

            for (String[] metadata : metadataList) {
                System.out.println(String.join(", ", metadata)); // print metadata for each file
            }
        }
    }


    /**
     * Formats a timestamp into a human-readable date string.
     *
     * @param timestamp The timestamp to be formatted.
     * @return A formatted date string in the "yyyy-MM-dd HH:mm:ss" format.
     */
    private static String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


    /**
     * Formats a size in bytes into a human-readable string representation.
     *
     * @param sizeInBytes The size to be formatted in bytes.
     * @return A formatted string representing the size with appropriate units (B, KB, MB, GB).
     */
    private static String formatSize(long sizeInBytes) {
        final long kiloByte = 1024;
        final long megaByte = kiloByte * 1024;
        final long gigaByte = megaByte * 1024;

        if (sizeInBytes < kiloByte) {
            return sizeInBytes + " B";
        } else if (sizeInBytes < megaByte) {
            return String.format("%.3f KB", (double) sizeInBytes / kiloByte);
        } else if (sizeInBytes < gigaByte) {
            return String.format("%.3f MB", (double) sizeInBytes / megaByte);
        } else {
            return String.format("%.3f GB", (double) sizeInBytes / gigaByte);
        }
    }



    public static void insertMetadata() {
        //insert all the metadata for each file in each row in the database, the table is called "dbo.Media" it has the following columns: "MediaName", "MediaDate", "MediaDuration", and "MediaVolume"
    }
}

