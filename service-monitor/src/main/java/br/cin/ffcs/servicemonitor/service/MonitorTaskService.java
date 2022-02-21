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

@Service
public class MonitorTaskService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorTaskService.class);

    private static final String DOCKER_STATS_CMD = "docker service ls";

    @Autowired
    private MetricRepository metricRepository;

    @Scheduled(fixedRate = 10000)
    public void eventCollector(){
        Process process;
        try {
            process = Runtime.getRuntime().exec(DOCKER_STATS_CMD);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String linha;
            StringBuilder sb = new StringBuilder();
            while( (linha = reader.readLine())!=null){
              sb.append(linha);
            }
            symptomGenerator(sb);
        } catch (IOException ioe) {
            logger.info(ioe.getMessage());
        }

    }

    //receive, parse and save data
    private void symptomGenerator(StringBuilder sb) {
        //create metric obj and persists
        Metric m = Metric.builder().build();
        updateMSM(m);

        //send symptoms to analyzer
        MonitorSymptomSenderService symptomSenderService = new MonitorSymptomSenderService();
        symptomSenderService.sendSymptoms(m);
    }

    public void updateMSM(Metric m){
        metricRepository.save(m);
    }
}
