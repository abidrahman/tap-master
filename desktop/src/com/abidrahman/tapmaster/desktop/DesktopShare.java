package com.abidrahman.tapmaster.desktop;

import com.abidrahman.tapmaster.Share;
import com.badlogic.gdx.Gdx;

public class DesktopShare implements Share {


    @Override
    public void shareScore(String promo) {
        Gdx.app.log("DesktopShare",promo);
    }
}