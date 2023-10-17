package com.example

import com.example.plugins.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import java.util.regex.Pattern

suspend fun parseSchedule(user: User): String {
    val username = user.username
    val password = user.password

    System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe")

    val driver: WebDriver = ChromeDriver()
    try {
        driver.get("https://portal.aues.kz/login/index.php")

        val usernameField = driver.findElement(By.id("username"))
        val passwordField = driver.findElement(By.id("password"))
        val loginButton = driver.findElement(By.id("loginbtn"))

        usernameField.sendKeys(username)
        passwordField.sendKeys(password)

        loginButton.click()

        //Thread.sleep(5000)

        driver.get("https://portal.aues.kz/token.php")
        //Thread.sleep(5000)

        val htmlContent = driver.pageSource

        val pattern = Pattern.compile("token=([^&]+)&amp;page_id=69")
        val matcher = pattern.matcher(htmlContent)

        var token = ""
        if (matcher.find()) {
            token = matcher.group(1)
        }

        val link = "https://portal.aues.kz/intranet/page.php?token=${token}&page_id=69&lang=1&action=schedule"
        println("Ссылка на расписание: $link")

        driver.get(link)
        //Thread.sleep(5000)
        delay(5000)

        val modalDialog = driver.findElements(By.className("modal-dialog")).firstOrNull()

        if (modalDialog != null) {
            val logoutButton = modalDialog.findElement(By.xpath("//button[contains(@class, 'btn-primary')]"))
            logoutButton.click()

            usernameField.clear()
            passwordField.clear()
            usernameField.sendKeys(username)
            passwordField.sendKeys(password)
            loginButton.click()

            //Thread.sleep(5000)
        }


        val dayElements = driver.findElements(By.cssSelector("td.day"))
        val daysOfWeek = listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")

        var days = 0
        val resultBuilder = StringBuilder()

        for (dayElement in dayElements) {
            resultBuilder.append("День недели: ${daysOfWeek[days]}\n")
            resultBuilder.append("--------------------------\n")
            days++

            val schElements = dayElement.findElements(By.className("sch"))



            for (schElement in schElements) {
                val timeDiv = schElement.findElement(By.xpath("./div[@class='schedule_entry']/div[1]"))
                val teacherDiv = schElement.findElement(By.xpath("./div[@class='schedule_entry']/div[2]"))
                val lessonDiv = schElement.findElement(By.xpath("./div[@class='schedule_entry']/div[3]"))

                val auditoriumScript = """
                    return arguments[0].querySelector('.schedule_entry div:last-child').textContent.trim();
                """
                val auditoriumData = (driver as JavascriptExecutor).executeScript(auditoriumScript, schElement) as String

                val teacher = teacherDiv.text.trim()
                val time = timeDiv.text.trim()
                val lesson = lessonDiv.text.trim()

                resultBuilder.append("Время: $time\n")
                resultBuilder.append("Занятие: $lesson\n")
                resultBuilder.append("Преподаватель: $teacher\n")
                resultBuilder.append("Аудитория: $auditoriumData\n")
                resultBuilder.append("--------------------------\n")

            }
        }
        return resultBuilder.toString()
    }finally {
        driver.quit()
    }
}

suspend fun parseScheduleAsync(user: User): String = runBlocking(Dispatchers.IO) {
    val result = async {
        parseSchedule(user)
    }
    result.await()
}