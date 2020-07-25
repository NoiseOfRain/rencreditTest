/*
 *  Copyright 2019 Qameta Software OÜ
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package rencredit;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.selenide.LogType;
import io.qameta.allure.util.ResultsUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings("unused")
public class AllureSelenideCustom implements LogEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureSelenideCustom.class);
    private final Map<LogType, Level> logTypesToSave = new HashMap<>();
    private final AllureLifecycle lifecycle;
    private final List<EventFormatter> formatters = getDefaultFormatters();
    private boolean saveScreenshots = true;
    private boolean savePageHtml = true;
    private boolean includeSelenideLocatorsSteps = true;


    public AllureSelenideCustom() {
        this(Allure.getLifecycle());
    }

    public AllureSelenideCustom(final AllureLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    private static byte[] getScreenshotBytes() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    private static byte[] getPageSourceBytes() {
        return WebDriverRunner.getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    private static String getBrowserLogs(final LogType logType, final Level level) {
        return String.join("\n\n", Selenide.getWebDriverLogs(logType.toString(), level));
    }

    public AllureSelenideCustom screenshots(final boolean saveScreenshots) {
        this.saveScreenshots = saveScreenshots;
        return this;
    }

    public AllureSelenideCustom savePageSource(final boolean savePageHtml) {
        this.savePageHtml = savePageHtml;
        return this;
    }

    public AllureSelenideCustom includeSelenideSteps(final boolean includeSelenideSteps) {
        this.includeSelenideLocatorsSteps = includeSelenideSteps;
        return this;
    }

    public AllureSelenideCustom enableLogs(final LogType logType, final Level logLevel) {
        logTypesToSave.put(logType, logLevel);
        return this;
    }

    public AllureSelenideCustom disableLogs(final LogType logType) {
        logTypesToSave.remove(logType);

        return this;
    }

    @Override
    public void beforeEvent(final LogEvent event) {
    }

    @Override
    public void afterEvent(final LogEvent event) {
        getFormatter(event).ifPresent(formatter -> {

            String title = formatter.format(event);
            final String stepUUID = UUID.randomUUID().toString();
            lifecycle.startStep(stepUUID, new StepResult()
                    .setName(title)
                    .setStatus(Status.PASSED));

            lifecycle.updateStep(stepResult -> stepResult.setStart(stepResult.getStart() - event.getDuration()));
            if (saveScreenshots) {
                lifecycle.addAttachment("Screenshot", "image/png", "png", getScreenshotBytes());
            }
            if (LogEvent.EventStatus.FAIL.equals(event.getStatus())) {
                if (savePageHtml) {
                    lifecycle.addAttachment("Page source", "text/html", "html", getPageSourceBytes());
                }
                lifecycle.updateStep(stepResult -> {
                    final StatusDetails details = ResultsUtils.getStatusDetails(event.getError())
                            .orElse(new StatusDetails());
                    stepResult.setStatus(ResultsUtils.getStatus(event.getError()).orElse(Status.BROKEN));
                    stepResult.setStatusDetails(details);
                });
                logTypesToSave
                        .forEach((logType, level) -> {
                            final byte[] content = getBrowserLogs(logType, level).getBytes(UTF_8);
                            lifecycle.addAttachment("Logs from: " + logType, "application/json", ".txt", content);
                        });
            }
            lifecycle.stopStep(stepUUID);
        });

    }

    private Optional<EventFormatter> getFormatter(LogEvent event) {
        return formatters.stream()
                .filter(f -> f.isApplicable(event))
                .findFirst();
    }

    private List<EventFormatter> getDefaultFormatters() {
        List<EventFormatter> formatters = new ArrayList<>();
        formatters.add(new EventFormatter(
                Pattern.compile("\\$\\(\"open\"\\) (?<url>.*)"),
                "Открываем страницу \"${url}\""
        ));
        formatters.add(new EventFormatter(
                Pattern.compile("\\$\\(\"(?<list>.*)\\.findBy\\(text '(?<element>.*)'\\)\"\\) click\\(\\)"),
                "Кликаем на элемент \"${element}\" из списка \"${list}\""
        ));
        formatters.add(new EventFormatter(
                Pattern.compile("\\$\\((?<element>.*)\\) click\\(\\)"),
                "Кликаем на элемент ${element}"
        ));
        formatters.add(new EventFormatter(
                Pattern.compile("\\$\\((?<element>.*)\\) set value\\((?<value>.*)\\)"),
                "Вводим в элемент ${element} значение [${value}]"
        ));
        formatters.add(new EventFormatter(
                Pattern.compile("(?<element>.*)"),
                "Действие не найдено: ${element}"
        ));
        return formatters;
    }

    protected class EventFormatter {

        private final Pattern pattern;

        private final String replacement;

        EventFormatter(Pattern pattern, String replacement) {
            this.replacement = replacement;
            this.pattern = pattern;
        }

        boolean isApplicable(LogEvent event) {
            return pattern.matcher(event.toString()).find();
        }

        public String format(LogEvent event) {
            return pattern.matcher(event.toString()).replaceAll(replacement);
        }
    }

}
