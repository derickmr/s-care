package com.tcc.webserver.controller;

import com.tcc.webserver.models.Context;
import com.tcc.webserver.service.ReportService;
import com.tcc.webserver.service.UserService;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Getter
@RestController
@RequestMapping(value = "/reports")
public class ReportController {

    @Resource
    private ReportService reportService;

    @Resource
    private UserService userService;

    @GetMapping(value = "/today")
    public List<Context> getReportsForToday(@RequestParam Long userId) {
        return this.reportService.getContextsFromCurrentDay(userService.getById(userId));
    }

    @GetMapping(value = "/untilDate")
    public List<Context> getReportsUntilDate(@RequestParam Long userId, @RequestParam Date date) {
        return this.reportService.getContextsUntilDate(userService.getById(userId), date);
    }

    @GetMapping(value = "/timeFrame")
    public List<Context> getReportsFromTimeFrame(@RequestParam Long userId, @RequestParam Date lowerDateLimit, @RequestParam Date higherDateLimit) {
        return this.reportService.getContextsWithinTimeFrame(userService.getById(userId), lowerDateLimit, higherDateLimit);
    }

    @GetMapping(value = "/last")
    public Context getLastReport(@RequestParam Long userId) {
        return this.reportService.getUserLastContext(this.userService.getById(userId));
    }

}
