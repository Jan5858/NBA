package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class MainTest {

    List<Player> playerList = new ArrayList<>();
    int stevec = 0;
    private ChromeDriver driver;
    private final List<String> playerLinks = getPlayerLinks();



    public List<String> getPlayerLinks()
    {
        Main main = new Main();
        JsonArray response = main.getPlayerData();

        response.forEach(jsonElement ->
        {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String globalTeamID = jsonObject.get("NbaDotComPlayerID").getAsString();
            String firstName = jsonObject.get("FirstName").getAsString();
            String lastName = jsonObject.get("LastName").getAsString();
            Player player = new Player(globalTeamID, firstName, lastName);
            playerList.add(player);
        });


        List<String> playerlinks = new ArrayList<>();
        playerList.forEach(player ->
        {
            String playerName = player.firstName()+"-"+ player.lastName();
            playerName = playerName.replace(".","");
            String URL = "https://www.nba.com/player/"+player.globalTeamID()+"/"+ playerName;
            playerlinks.add(URL.replace(" ","-"));
        });
        return playerlinks;

    }

    @BeforeMethod
    public void setUp()
    {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.get(playerLinks.get(stevec++));
    }


    @AfterMethod
    public void tearDown()
    {
            driver.quit();
   }


    @Test(invocationCount = 17)
    public void testMain()
    {

        System.out.println("break here");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__next']/div[2]/div[2]/section/div[4]/section[2]/div")));

        WebElement iframe = driver.findElement(By.xpath("//*[@id='__next']/div[2]/div[2]/section/div[4]/section[2]/div"));
        new Actions(driver)
                .moveToElement(iframe)
                .perform();


        float vsota=0;
        for(int indeks = 1; indeks <= 5; indeks++ )
        {
            WebElement prvaVrstica = driver.findElement((By.xpath("//tbody/tr["+indeks+"]/td[9]")));
            String prva = prvaVrstica.getText();
            vsota = vsota+ Integer.parseInt(prva);
            System.out.println(prva);
        }

        float povprecje = vsota / 5;

        assertTrue(povprecje >1,"Povprecje je nad ena");

    }
}
