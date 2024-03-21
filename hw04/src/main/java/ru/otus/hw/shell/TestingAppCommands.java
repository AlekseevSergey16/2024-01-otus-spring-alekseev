package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class TestingAppCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(key = "start", value = "start testing")
    public void startTesting() {
        testRunnerService.run();
    }

}
