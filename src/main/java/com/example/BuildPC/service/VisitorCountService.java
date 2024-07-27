package com.example.BuildPC.service;

import com.example.BuildPC.model.VisitorCount;
import com.example.BuildPC.repository.VisitorCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
