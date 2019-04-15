package top.fomeiherz.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hystrix2")
@DefaultProperties(defaultFallback = "defaultFail")
public class HystrixController2 {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @HystrixCommand(commandProperties =
            {
                    // 熔断器在整个统计时间内是否开启的阀值，默认是true
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    // 至少有3个请求才进行熔断错误比率计算
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    // 当出错率超过50%后熔断器启动
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    // 熔断器工作时间，超过这个时间，先放一个请求进去，成功的话就关闭熔断，失败就再等一段时间
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "20000"),
                    // 统计滚动的时间窗口
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")
            }, commandKey = "hystrix2/test1")
    @GetMapping("/test1")
    public String test1(@RequestParam("id") Integer id) {
        logger.error("id:" + id);
        if (id % 2 == 0) {
            throw new RuntimeException();
        }
        return "test_" + id;
    }

    /**
     * When raise Exception, but the fallbackMethod is not configured. And this method will be invoked.
     *
     * @return
     */
    private String defaultFail() {
        logger.error("default fail");
        return "default fail";
    }

}
