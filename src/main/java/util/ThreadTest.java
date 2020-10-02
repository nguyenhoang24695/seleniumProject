package util;

import Entity.MyCookie;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.VarUtil.*;

public class ThreadTest extends Thread {
    public static int COUNT;

    private Thread t;
    private int name;
    private MyCookie cookie;
    private int flag;
    private static List<MyCookie> myCookies;
    private String tS;
    public ThreadTest(int numb) {
        name = numb;
        System.out.println("Run thread : " + numb);
    }

    public ThreadTest(MyCookie cookie) {
        this.cookie = cookie;
    }

    public ThreadTest(boolean needCOUNT) {

        if (!needCOUNT) {
            this.flag = 1;
        }
    }

    public ThreadTest(String threadString){
        this.tS = threadString;
    }

    @Override
    public void run() {

        System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
        WebDriver driver;

        if (IS_MOBILE) {
            driver = getTransformToMobile();
        } else {
            driver = new ChromeDriver();
        }


        if (flag == 1) {
            runWithThreadAndCookies(driver);
        }else if(this.tS.equals("RWT")){
            runWithThread(driver);
        } else {
            runWithCookie(driver, cookie);
        }

        driver.close();
        driver.quit();
    }

    public void start() {

        System.out.println("Start Login" + name);
        if (t == null) {
            t = new Thread(this, String.valueOf(name));
        }
        t.start();
    }

    public void runWithThreadAndCookies(WebDriver driver) {

        MyCookie myCookie = COOKIES_LIST.get(0);
        COOKIES_LIST.remove(0);

        runWithCookie(driver, myCookie);
        if (!COOKIES_LIST.isEmpty()) {
            runWithThreadAndCookies(driver);
        }
    }

    public void runWithThread(WebDriver driver) {

        COUNT_PEND++;
        runWithCookie(driver, null);
        if (COUNT_PEND < COOKIES_SIZE) {
            runWithThread(driver);
        }
    }

    public void runWithCookie(WebDriver driver, MyCookie cookie) {
        try {

            driver.get(HOST_URL);
            if (cookie != null) {
                driver.manage().addCookie(new Cookie("ASP.NET_SessionId", cookie.getSid()));
                driver.manage().addCookie(new Cookie("_gid", cookie.getGid()));
                driver.manage().addCookie(new Cookie("_ga", cookie.getGa()));
            }else{
                driver.manage().deleteAllCookies();
            }

            WebElement userLogin = driver.findElement(By.xpath("//*[@id=\"ContentMain_txtUsername\"]"));
            WebElement passLogin = driver.findElement(By.xpath("//*[@id=\"ContentMain_txtPassword\"]"));
            WebElement btnLogin = driver.findElement(By.xpath("//*[@id=\"ContentMain_btnLogin\"]"));

            userLogin.sendKeys("400824");
            passLogin.sendKeys("123");
            btnLogin.click();
            WebElement newBtn;
            try {
                newBtn = driver.findElement(By.id("ContentMain_btnAddNewPre"));
            } catch (Exception e) {
                newBtn = driver.findElement(By.id("ContentMain_btnAddNewPost"));
            }
            newBtn.click();
            WebElement aTag = driver.findElement(By.xpath("//*[@id=\"MenuReport\"]"));
            aTag.click();
            WebElement bTag = driver.findElement(By.xpath("//*[@id=\"A2\"]"));
            bTag.click();
            WebElement reportBtn = driver.findElement(By.xpath("//*[@id=\"ContentMain_btnReport\"]"));
            reportBtn.click();

            if (cookie == null) {

                MyCookie myCookie = new MyCookie();
                myCookie.setSid(driver.manage().getCookieNamed("ASP.NET_SessionId").getValue());
                myCookie.setGid(driver.manage().getCookieNamed("_gid").getValue());
                myCookie.setGa(driver.manage().getCookieNamed("_ga").getValue());
                COOKIES_LIST.add(myCookie);
            }
            if (flag != 1) {
                COUNT++;
            }
            MiniToolUtil.getInstance().BarProgress(++COUNT_DONE, COOKIES_SIZE);
        } catch (Exception e) {
            runWithCookie(driver, cookie);

        }
    }

    public WebDriver getTransformToMobile() {
        Map<String, Object> deviceMetrics = new HashMap<String, Object>();
        deviceMetrics.put("width", 560);
        deviceMetrics.put("height", 640);
        deviceMetrics.put("pixelRatio", 4.0);
        Map<String, Object> mobileEmulation = new HashMap<String, Object>();
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation
                .put("userAgent",
                        "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
        Map<String, Object> chromeOptions = new HashMap<String, Object>();
        chromeOptions.put("mobileEmulation", mobileEmulation);
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        return new ChromeDriver(capabilities);
    }
}
