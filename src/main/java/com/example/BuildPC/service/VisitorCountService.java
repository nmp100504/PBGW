package com.example.BuildPC.service;

import com.example.BuildPC.model.VisitorCount;
import com.example.BuildPC.repository.VisitorCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisitorCountService {

    @Autowired
    private VisitorCountRepository visitorCountRepository;

    @Transactional
    public void incrementVisitorCount(String postUrl) {
        VisitorCount visitorCount = visitorCountRepository.findByPostUrl(postUrl);
        if (visitorCount == null) {
            visitorCount = new VisitorCount();
            visitorCount.setPostUrl(postUrl);
            visitorCount.setCount(1);
        } else {
            visitorCount.setCount(visitorCount.getCount() + 1);
        }
        visitorCountRepository.save(visitorCount);
    }

    public int getVisitorCount(String postUrl) {
        VisitorCount visitorCount = visitorCountRepository.findByPostUrl(postUrl);
        return visitorCount != null ? visitorCount.getCount() : 0;
    }

    public Integer getTotalVisitorCount() {
        return visitorCountRepository.getTotalVisitorCount();
    }

    public Map<String, Integer> getTopPostUrlsByViewCount() {
        List<Object[]> results = visitorCountRepository.findTopPostUrlsByViewCount();

        // Initialize the map to hold post URLs and view counts
        Map<String, Integer> topPostUrlsByViewCount = new LinkedHashMap<>();

        // Process the results and populate the map
        for (int i = 0; i < Math.min(10, results.size()); i++) {
            Object[] result = results.get(i);
            String postUrl = (String) result[0];
            Integer viewCount = (Integer) result[1];
            topPostUrlsByViewCount.put(postUrl, viewCount);
        }

        return topPostUrlsByViewCount;
    }
}
