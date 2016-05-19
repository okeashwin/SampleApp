package com.app;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.Float;
import java.lang.System;
import java.sql.Timestamp;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;

public class App
{
    private static String USER_AGENT = "Mozilla/5.0";
    private static String URL = "http://localhost:8080/Pindropapp/add";
    private static int RECORD_ID=0;
    private static final Logger logger = Logger.getLogger(App.class.getName());
    public static void main( String[] args ) {
        float memUsed=0;
        float diskUsed=0;
        float cpuUsed=0;
        int counter=0;
        String[] memUsedCommand = {"/bin/sh", "-c", "free | grep Mem | awk '{print $3/$2}'"};
        String[] cpuUsedCommand = {"/bin/sh", "-c", "top -bn1 | grep \"Cpu(s)\" | sed \"s/.*, *\\([0-9.]*\\)%* id.*/\\1/\" | awk '{print 100 - $1}'"};
        String[] diskUsedCommand = {"/bin/sh", "-c", "df -hl | grep 'sda2\\|sda3' | awk 'BEGIN{} {percent+=$5;} END{print percent}' | column -t"};

        Timestamp start=new Timestamp(System.currentTimeMillis());
        while(true) {
            memUsed=0;
            diskUsed=0;
            cpuUsed=0;
            counter=1;
            start=new Timestamp(System.currentTimeMillis());
            while(System.currentTimeMillis() - start.getTime() <= 1000) {
                String sampleMemUsage= executeCommand(memUsedCommand);
                String sampleCPUUsage= executeCommand(cpuUsedCommand);
                String sampleDisk = executeCommand(diskUsedCommand);
                logger.log(Level.INFO, sampleMemUsage);
//                memUsed=(memUsed + Float.parseFloat(sampleMemUsage))/counter;
//                cpuUsed=(cpuUsed + Float.parseFloat(sampleCPUUsage))/counter;
//                diskUsed=(diskUsed + Float.parseFloat(sampleDisk))/counter;
                memUsed=Float.parseFloat(sampleMemUsage);
                cpuUsed=Float.parseFloat(sampleCPUUsage);
                diskUsed=Float.parseFloat(sampleDisk);
                counter++;
            }
            RECORD_ID++;
            try {
                long startTime = start.getTime();
                long endTime = System.currentTimeMillis();
                //sendPostRequest(start.getTime(), System.currentTimeMillis(), cpuUsed, memUsed, diskUsed);
                URL obj = new URL(URL);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add request header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = "id="+RECORD_ID+"&startTime="+startTime+"&endTime="+endTime+"&cpuUsed="+cpuUsed+"&memUsed="+100*memUsed+"&diskUsed="+diskUsed;
                logger.log(Level.INFO, "POST parameters : "+urlParameters);

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                logger.log(Level.INFO, "\nSending 'POST' request to URL : " + URL);
                logger.log(Level.INFO, "Post parameters : " + urlParameters);
                logger.log(Level.INFO, "Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                logger.log(Level.INFO, response.toString());
            }
            catch( Exception e) {
                logger.log(Level.WARNING, "There was an exception "+e.toString());
            }
        }
    }

    private static String executeCommand(String[] command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }
}

