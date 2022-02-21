package br.cin.ffcs.servicemonitor.service;

import br.cin.ffcs.servicemonitor.model.Metric;
import br.cin.ffcs.servicemonitor.repository.MetricRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MonitorTaskService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorTaskService.class);

    private static final String DOCKER_STATS_CMD = "docker stats --no-stream --format \"table {{.NAME}}\\t{{.CPUPerc}}\"";

    @Autowired
    private MetricRepository metricRepository;

    @Scheduled(fixedRate = 10000)//runs every 10 segs
    public void eventCollector(){
        Process process;
        try {
            process = Runtime.getRuntime().exec(DOCKER_STATS_CMD);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String linha;
            StringBuilder sb = new StringBuilder();
            while( (linha = reader.readLine())!=null){
              sb.append(linha+"|");
            }
            String[] tempArray = sb.toString().split("|");
            List<String> lista = Arrays.asList(tempArray);
            List<Metric> processedData = eventPreProcessing(lista);
            symptomGenerator(processedData);
        } catch (IOException ioe) {
            logger.info(ioe.getMessage());
        }
    }

    //receive, parse and save data
    private void symptomGenerator(List<Metric> processedData) {
        MonitorSymptomSenderService symptomSenderService = new MonitorSymptomSenderService();
        for (Metric m : processedData) {
            //update knowledge base
            updateMSM(m);

            //send symptoms to analyzer
            symptomSenderService.sendSymptoms(m);
        }

    }
    /*
    process data, normalize and aggregate
     */
    private List<Metric> eventPreProcessing(List<String> lista) {
        List<Metric> data = new ArrayList<>();
        for (String l : lista){
            //create metric obj and persists
            Metric m = Metric.builder()
                             .chave("CPU_UTILIZATION")
                             .valor(l)
                             .build();
            data.add(m);
        }
        return data;
    }

    public void updateMSM(Metric m){
        metricRepository.save(m);
    }
}