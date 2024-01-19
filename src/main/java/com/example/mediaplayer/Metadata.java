package com.example.mediaplayer;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
                    insertMetadata(metadata[0], metadata[1], metadata[2]);
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



    public static void insertMetadata(String fileName, String fileDate, String fileSize) {
        Connection connection = Database.connection; // Use connection variable from database class

        PreparedStatement handleMetadata; // Prepare SQL statement (CRUD)

        // Insert the collected and stored metadata into the database in the media table.
        // If there already is a file with that name created, it will update data instead of insert.
        // That way we avoid duplicate entries, and ensure they are up-to-date.
        try {
            handleMetadata = connection.prepareStatement(
                    "MERGE INTO Media AS target " + // use Media table as target
                            "USING (VALUES (?, ?, ?)) AS source (Name, Date, Filesize) " + // use Name, Date, Filesize ad placeholders
                            "ON target.Name = source.Name " + // when there's an entry in the media table with the same name as one of the media then
                            "WHEN MATCHED BY SOURCE THEN " + // if there is a database entry, but no media, delete the database entry
                            "   DELETE " +
                            "WHEN MATCHED AND (target.Date <> source.Date OR target.Filesize <> source.Filesize) THEN " + // if there is a name match, but date or filesize differs, do SQL update
                            "   UPDATE SET target.Date = source.Date, target.Filesize = source.Filesize " +
                            "WHEN NOT MATCHED THEN " + // if one does not exist in the database, but the media folder, create it
                            "   INSERT (Name, Date, Filesize) VALUES (source.Name, source.Date, source.Filesize);"

            );




            // Set values for the parameters in the prepared statement
            handleMetadata.setString(1, fileName);
            handleMetadata.setString(2, fileDate);
            handleMetadata.setString(3, fileSize);

            // Execute the update (insert) operation
            int rowsAffected = handleMetadata.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

