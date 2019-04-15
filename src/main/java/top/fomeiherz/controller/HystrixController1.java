package top.fomeiherz.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hystrix1")
@DefaultProperties(defaultFallback = "defaultFail")
public class HystrixController1 {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Invoke flow: test()1 -> fail()
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "fail1", commandKey = "hystrix1/test1")
    @GetMapping("/test1")
    public String test1() {
        throw new RuntimeException();
    }

    private String fail1() {
        logger.error("fail1");
        return "fail1";
    }

    /**
     * Invoke flow: test2() -> fail2() -> fail3() -> defaultFail()
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "fail2")
    @GetMapping("/test2")
    public String test2() {
        throw new RuntimeException();
    }

    /**
     * Because this method raise Exception, it will invoke fail3() method.
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "fail3")
    private String fail2() {
        logger.error("fail2");
        throw new RuntimeException();
    }

    /**
     * Because this method raise Exception, it will invoke defaultFail() method.
     *
     * @return
     */
    @HystrixCommand
    private String fail3() {
        logger.error("fail3");
        throw new RuntimeException();
    }

    private String defaultFail() {
        logger.error("default fail");
        return "default fail";
    }

}
