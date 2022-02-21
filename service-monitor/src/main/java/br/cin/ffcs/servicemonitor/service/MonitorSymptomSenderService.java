package br.cin.ffcs.servicemonitor.service;

import br.cin.ffcs.servicemonitor.model.Metric;
import br.cin.ffcs.servicemonitor.util.ParameterStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class MonitorSymptomSenderService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorSymptomSenderService.class);
    //TODO: replace for a service discovery
    private static final String SVC_ANALYZER_URL = "http://localhost:8083/api/v1/receiveSymptom";

    public void sendSymptoms(Metric metric) {
        URL url;
        try {
            url = new URL(SVC_ANALYZER_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            Map<String, String> parameters = new HashMap<>();
            parameters.put("metrics", metric.toString());

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            int status = con.getResponseCode();
            logger.info("Status do request: " + status);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
