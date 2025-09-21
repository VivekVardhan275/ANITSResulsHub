package rocks.vivek275.anitsresultshub.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.vivek275.anitsresultshub.service.StudentPerformanceService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/get")
public class StudentPerformanceViewController {
    @Autowired
    private StudentPerformanceService studentPerformanceService;
    @GetMapping("/student-performance")
    public ResponseEntity<List<Map<String, Object>>> getStudentPerformance(@RequestParam("batch") String batch, @RequestParam("branch") String branch , @RequestParam("semester") String semester) {
        try{
            return new ResponseEntity<>(studentPerformanceService.fetchPerformance(batch.toLowerCase(),branch.toLowerCase(),semester.replace("-" , "_")), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
