package basicWeb;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class Curriculum {

    public static void main(String[] args) {
        // ✅ chromedriver 자동 경로 설정
        String currentPath = Paths.get("").toAbsolutePath().toString();
        String driverPath = currentPath + File.separator + "chromedriver";
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            driverPath += ".exe";
        }
        System.setProperty("webdriver.chrome.driver", driverPath);

        WebDriver driver = new ChromeDriver();
        List<Map<String, Object>> subjects = new ArrayList<>();

        try {
            driver.get("https://cse.knu.ac.kr/sub3_2_b.php");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("tbody")));

            String year = "";
            String division = "";

            for (WebElement tbody : driver.findElements(By.tagName("tbody"))) {
                for (WebElement row : tbody.findElements(By.tagName("tr"))) {
                    var ths = row.findElements(By.tagName("th"));
                    var tds = row.findElements(By.tagName("td"));

                    if (!ths.isEmpty()) {
                        if (ths.size() == 2) {
                            year = ths.get(0).getText().trim();
                            division = ths.get(1).getText().trim();
                        } else if (ths.size() == 1) {
                            String txt = ths.get(0).getText().trim();
                            if (txt.matches("\\d")) year = txt;
                            else division = txt;
                        }
                    }
                    if (year.equals("")) continue;

                    if (tds.size() >= 3) {
                        Map<String, Object> sub = parseSubject(year, "1학기", division, tds.get(0), tds.get(1), tds.get(2));
                        if (sub != null) subjects.add(sub);
                    }

                    if (tds.size() >= 6) {
                        Map<String, Object> sub = parseSubject(year, "2학기", division, tds.get(3), tds.get(4), tds.get(5));
                        if (sub != null) subjects.add(sub);
                    }
                }
            }

            if (subjects.isEmpty()) {
                System.out.println("❌ 저장된 과목이 없습니다. 필터 조건 또는 HTML 구조를 확인하세요.");
            } else {
                System.out.println("✅ 저장된 과목 수: " + subjects.size());
                for (Map<String, Object> sub : subjects) {
                    System.out.printf("%s학년 %s [%s] %s - %s, 필수: %s, 설계: %s, 학점: %s%n",
                        sub.get("year"), sub.get("semester"), sub.get("division"),
                        sub.get("code"), sub.get("name"),
                        sub.get("isRequired"), sub.get("isDesign"), sub.get("credit")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static Map<String, Object> parseSubject(String year, String semester, String division,
                                                    WebElement codeElem, WebElement nameElem, WebElement creditElem) {
        String code = codeElem.getText().trim();
        String html = nameElem.getAttribute("innerHTML");
        String name = html.replaceAll("<[^>]*>", "").split("\\(")[0].trim();
        String credit = creditElem.getText().trim().replaceAll("\\s+", "");

        if (code.isBlank() || code.equals("-") || credit.isBlank() || credit.equals("-")) return null;
        if (name.isBlank() || name.equals("-")) return null;

        boolean isRequired = html.contains("bum01");
        boolean isDesign = html.contains("bum02");

        Map<String, Object> subject = new HashMap<>();
        subject.put("year", year);
        subject.put("semester", semester);
        subject.put("division", division);
        subject.put("code", code);
        subject.put("name", name);
        subject.put("isRequired", isRequired);
        subject.put("isDesign", isDesign);
        subject.put("credit", credit);

        return subject;
    }
}
