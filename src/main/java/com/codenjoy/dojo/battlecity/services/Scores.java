package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class Scores implements PlayerScores {

    private final Parameter<Integer> killYourTankPenalty;
    private final Parameter<Integer> killOtherTankScore;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        killYourTankPenalty = settings.addEditBox("Kill your tank penalty").type(Integer.class).def(50);
        killOtherTankScore = settings.addEditBox("Kill other tank score").type(Integer.class).def(100);
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(Events.KILL_YOUR_TANK)) {
            score -= killYourTankPenalty.getValue();
        } else if (event.equals(Events.KILL_OTHER_TANK)) {
            score += killOtherTankScore.getValue();
        }
        score = Math.max(0, score);
    }
}
