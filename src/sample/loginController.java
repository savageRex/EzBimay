package sample;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class loginController implements Serializable{
    public TextField emailText;
    public PasswordField passwordText;
    public Button loginButton;
    public Text wrongText;
    public Text waitText;
    public Text failText;
    public Text failText2;
    public Text failText3;
    public Hyperlink failText4;
    List<String> options = new ArrayList<>();
    public LinkedHashMap<String, String> studCourse = new LinkedHashMap<>();

    public void run(){
        new Thread(this::login).start();
    }

    public void login(){
        emailText.setDisable(true);
        passwordText.setDisable(true);
        loginButton.setDisable(true);
        wrongText.setVisible(false);
        failText.setVisible(false);
        failText2.setVisible(false);
        failText3.setVisible(false);
        failText4.setVisible(false);
        WebDriver driver = null;
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("window-size=1200x600");
        String os = System.getProperty("os.name");
        String currentChromeVer = "";
        try {
            if (os.contains("Mac")) {
                File chrome = new File("/Applications/Google Chrome.app/Contents/Versions");
                File[] chromeVers = chrome.listFiles();
                currentChromeVer = chromeVers[chromeVers.length - 1].toString().substring(50, 52);
                if (chrome.exists()) {
                    File chromedriver = new File(System.getProperty("user.dir") + "/chromedriver");
                    if (!chromedriver.exists()) {
                        URL website = new URL("https://chromedriver.storage.googleapis.com/" + getChromedriverVer(currentChromeVer) + "/chromedriver_mac64.zip");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream("chromedriver_mac64.zip");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        fos.close();
                        String[] args = new String[]{"unzip", System.getProperty("user.dir") + "/chromedriver_mac64.zip", "chromedriver", "-d", System.getProperty("user.dir")};
                        Process proc = new ProcessBuilder(args).start();
                        proc.waitFor();
                        File zip = new File(System.getProperty("user.dir") + "/chromedriver_mac64.zip");
                        zip.delete();
                    }
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver");
                    driver = new ChromeDriver(chromeOptions);
                } else driver = new SafariDriver();
            } else if (os.contains("Windows 10")) {
                File chrome = new File("C:/Program Files (x86)/Google/Chrome/Application");
                File[] chromeVers = chrome.listFiles();
                currentChromeVer = chromeVers[0].toString().substring(49, 51);
                if (chrome.exists()) {
                    File chromedriver = new File(System.getProperty("user.dir") + "\\chromedriver.exe");
                    if (!chromedriver.exists()) {
                        URL website = new URL("https://chromedriver.storage.googleapis.com/" + getChromedriverVer(currentChromeVer) + "/chromedriver_win32.zip");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream("chromedriver_win32.zip");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        fos.close();
                        String[] args = new String[]{"powershell.exe", "-NoP", "-NonI", "-Command", "\"Expand-Archive '" + System.getProperty("user.dir") + "\\chromedriver_win32.zip' '" + System.getProperty("user.dir") + "'\""};
                        Process proc = (new ProcessBuilder(args)).start();
                        proc.waitFor();
                        File zip = new File(System.getProperty("user.dir") + "\\chromedriver_win32.zip");
                        zip.delete();
                    }
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
                    driver = new ChromeDriver(chromeOptions);
                }
            }
//        else if (os.contains("Windows 7")) {
//            File chrome = new File("C:/Program Files (x86)/Google/Application");
//            if(chrome.exists()) {
//                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
//                driver = new ChromeDriver(chromeOptions);
//            }
//            else driver = new InternetExplorerDriver();
//        }
            driver.navigate().to("https://binusmaya.binus.ac.id/login/");
            WebElement emailBox = driver.findElement(By.xpath("//input[@type='text']"));
            emailBox.sendKeys(emailText.getText());
            WebElement passwordBox = driver.findElement(By.xpath("//input[@type='password']"));
            passwordBox.sendKeys(passwordText.getText());
            WebElement button = driver.findElement(By.xpath("//input[@type='submit']"));
            button.click();
            if (driver.getCurrentUrl().equals("https://binusmaya.binus.ac.id/newStudent/") || driver.getCurrentUrl().equals("https://binusmaya.binus.ac.id/newStudent/#/index")) {
                waitText.setVisible(true);
                WebDriverWait wait = new WebDriverWait(driver, 60);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"nobledream\"]")));
                WebElement menu = driver.findElement(By.xpath("//*[@id=\"main-nav-expand\"]"));
                menu.click();
                waitText.setText("Please wait... Initializing. 20%");
                Thread.sleep(1500);
                WebElement courses = driver.findElement(By.linkText("Courses"));
                courses.click();
                WebElement tab3 = driver.findElement(By.xpath("//*[@id=\"main-nav-expand\"]/div/ul[3]"));
                BufferedReader semesters = new BufferedReader(new StringReader(tab3.getText()));
                String currentSemester;
                while ((currentSemester = semesters.readLine()) != null) {
                    break;
                }
                WebElement semester = driver.findElement(By.linkText(currentSemester));
                semester.click();
                WebElement element = driver.findElement(By.xpath("//*[@id=\"main-nav-expand\"]/div/ul[4]"));
                BufferedReader allCourses = new BufferedReader(new StringReader(element.getText()));
                String line;
                while ((line = allCourses.readLine()) != null) {
                    WebElement course = driver.findElement(By.linkText(line));
                    studCourse.put(line, course.getAttribute("href"));
                }
                waitText.setText("Please wait... Initializing. 50%");
                driver.navigate().to("https://binusmaya.binus.ac.id/newstudent/#/exam/studentexam");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody")));
                String exam;
                if (driver.findElements(By.xpath("//*[@id=\"tableTemplate\"]/table")).size() == 1) exam = "mid";
                else exam = "final";
                waitText.setText("Please wait... Initializing. 80%");
                options.add("//I know you can read this file unlike the others, hackerman. But please don't edit anything or you're gonna have a bad time (no, seriously tho.)");
                options.add("[General]");
                options.add("checkforupdate=\"yes\"");
                options.add("currentexam=\"" + exam + "\"");
                options.add("currentversion=\"\"");
                options.add("chromeversion=\"" + currentChromeVer + "\"");
                options.add("[Course]");
                Set<String> course = studCourse.keySet();
                for (String s : course) {
                    if (studCourse.get(s).substring(70, 73).equals("LAB")) {
                        ((JavascriptExecutor) driver).executeScript("window.open('" + studCourse.get(s) + "','_blank');");
                        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                        driver.switchTo().window(tabs.get(tabs.size() - 1));
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ddlclasslist\"]/option[2]")));
                        WebElement CLASS = driver.findElement(By.xpath("//*[@id=\"ddlclasslist\"]/option[2]"));
                        String link = studCourse.get(s).substring(0, 70) + "LEC/" + CLASS.getAttribute("value");
                        options.add(s + "," + link);
                    } else options.add(s + "," + studCourse.get(s));
                }
                waitText.setText("Please wait... Initializing. 100%");
                driver.close();
                driver.quit();
                Files.write(Paths.get("options"), options);

                ObjectOutputStream kext = new ObjectOutputStream(new FileOutputStream("kext"));
                ObjectOutputStream lib = new ObjectOutputStream(new FileOutputStream("lib"));
                ObjectOutputStream dat = new ObjectOutputStream(new FileOutputStream("dat"));
                KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
                SecretKey myDesKey = keygenerator.generateKey();
                Cipher desCipher;
                byte[] key = myDesKey.getEncoded();
                kext.writeObject(key);
                kext.close();

                desCipher = Cipher.getInstance("DES");
                byte[] text = emailText.getText().getBytes();
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                byte[] textEncrypted = desCipher.doFinal(text);
                lib.writeObject(textEncrypted);
                lib.close();

                desCipher = Cipher.getInstance("DES");
                text = passwordText.getText().getBytes();
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                textEncrypted = desCipher.doFinal(text);
                dat.writeObject(textEncrypted);
                dat.close();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("app.fxml"));
                            Stage stage = (Stage) loginButton.getScene().getWindow();
                            stage.setScene(new Scene(root));
                        }
                        catch (Exception e){}
                    }
                });
            } else {
                driver.close();
                driver.quit();
                emailText.setDisable(false);
                passwordText.setDisable(false);
                loginButton.setDisable(false);
                wrongText.setVisible(true);
                failText.setVisible(false);
                failText2.setVisible(false);
                failText3.setVisible(false);
                failText4.setVisible(false);
            }
        }
        catch (Exception e){
            emailText.setDisable(false);
            passwordText.setDisable(false);
            loginButton.setDisable(false);
            waitText.setVisible(false);
            wrongText.setVisible(false);
            failText.setVisible(true);
            failText2.setVisible(true);
            failText3.setVisible(true);
            failText4.setVisible(true);
        }
    }

    public String getChromedriverVer(String ver) {
        String versionNumber = "";
        try {
            URL url = new URL("https://chromedriver.storage.googleapis.com/LATEST_RELEASE_" + ver);
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                builder.append(inputLine.trim());
            in.close();
            String htmlPage = builder.toString();
            versionNumber = htmlPage.replaceAll("\\<.*?>", "");
        }
        catch (Exception e){}
        return versionNumber;
        // Source
        // https://stackoverflow.com/a/8278156/11106220chro
    }

    public void FAQ() {
        HostServices hostServices = (HostServices)failText4.getScene().getWindow().getProperties().get("hostServices");
        hostServices.showDocument("https://github.com/savageRex/EzBimay#faq");
    }

    public void minimize(){
        Main.primaryStage.setIconified(true);
    }

    public void close() { System.exit(0); }
}
