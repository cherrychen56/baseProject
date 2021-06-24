package com.magic_chen_.baseproject.jumper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

/**
 *
 */
public class BaseIntent {
    final Intent mIntent;

    public BaseIntent(final Intent intent) {
        this.mIntent = intent;
    }

    public void startActivity(final Context context) {
        context.startActivity(mIntent);
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void startActivityForResult(final Activity activity, int requestCode) {
        activity.startActivityForResult(mIntent, requestCode);
    }

    public void startActivityForResult(final Fragment fragment, int requestCode) {
        fragment.startActivityForResult(mIntent, requestCode);
    }

    public BaseIntent addFlag(final int flag) {
        mIntent.addFlags(flag);
        return this;
    }

    public BaseIntent addCategory(final String catalog) {
        mIntent.addCategory(catalog);
        return this;
    }
}
