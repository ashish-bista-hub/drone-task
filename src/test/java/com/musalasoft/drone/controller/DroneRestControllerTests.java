package com.musalasoft.drone.controller;

import com.musalasoft.drone.DroneApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DroneApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DroneRestControllerTests {

}
