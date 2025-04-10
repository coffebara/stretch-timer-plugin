package org.example;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

public class StretchTimerPlugin extends AnAction {
    private Timer timer;
    private int minutes;
    private int seconds;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        // 타이머 설정 다이얼로그
        TimerSettingsDialog dialog = new TimerSettingsDialog(project);
        dialog.show();
        if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            minutes = dialog.getMinutes();
            seconds = dialog.getSeconds();
            startTimer(project);
        }
    }

    private void startTimer(Project project) {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        long delay = (minutes * 60 + seconds) * 1000L;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    int result = Messages.showYesNoDialog(
                        project,
                        "자리에서 일어나 스트레칭을 하세요!",
                        "스트레칭 시간",
                        Messages.getQuestionIcon()
                    );

                    if (result == Messages.YES) {
                        try {
                            Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=AEbr_-Z86tU"));
                        } catch (Exception ex) {
                            Messages.showErrorDialog(project, "영상을 열 수 없습니다.", "오류");
                        }
                    }
                });
            }
        }, delay);
    }
}

class TimerSettingsDialog extends DialogWrapper {
    private final JSpinner minutesSpinner;
    private final JSpinner secondsSpinner;

    protected TimerSettingsDialog(Project project) {
        super(project);
        setTitle("타이머 설정");

        SpinnerNumberModel minutesModel = new SpinnerNumberModel(0, 0, 60, 1);
        SpinnerNumberModel secondsModel = new SpinnerNumberModel(30, 0, 59, 1);

        minutesSpinner = new JSpinner(minutesModel);
        secondsSpinner = new JSpinner(secondsModel);

        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("분:"));
        panel.add(minutesSpinner);
        panel.add(new JLabel("초:"));
        panel.add(secondsSpinner);
        return panel;
    }

    public int getMinutes() {
        return (int) minutesSpinner.getValue();
    }

    public int getSeconds() {
        return (int) secondsSpinner.getValue();
    }
} 