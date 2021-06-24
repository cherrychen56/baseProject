package com.magic_chen_.baseproject.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.example.screen_adaptation.utils.AutoUtils;
import com.example.screen_adaptation.utils.DimenUtils;


/**
 * Created by Administrator on 2016/10/9.
 */
public class ViewUtils {

    public static int[] getViewLocationOnScreen(View view) {
        int[] location = new int[2];
        try {
            view.getLocationOnScreen(location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return location;
    }

    public static int[] getViewCenterLocationOnScreen(View view){
        int[] location = new int[2];
        int [] locationCenter = new int[2];
        try {
            view.getLocationOnScreen(location);
            locationCenter  = new int[]{location[0]+view.getWidth()/2,location[1]+view.getHeight()/2};
            Log.d("viewUtils....","old x:"+location[0]+"  old y:"+location[1]);
            Log.d("viewUtils....","new x:"+locationCenter[0]+"  new y:"+locationCenter[1]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return locationCenter;
    }

    public static int getAutoLayoutSize(Context context, int id) {
        TypedValue value = new TypedValue();
        context.getResources().getValue(id, value, true);
        if (value.type != TypedValue.TYPE_DIMENSION) {
            return 0;
        }

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int pixelValue = TypedValue.complexToDimensionPixelOffset(
                value.data, metrics);
        if (!DimenUtils.isPxVal(value)) {
            return pixelValue;
        }
        return AutoUtils.getPercentWidthSizeBigger(pixelValue);
    }

    /**
     * @param px 标注尺寸单位px
     * @param isVertical 是否为垂直方向的尺寸
     * @return 将设计尺寸转换为实际尺寸
     */
    public static int getAutoLayoutSize(int px,boolean isVertical ){
        if(!isVertical){
            return AutoUtils.getPercentWidthSizeBigger(px);
        }else {
            float w_val = AutoUtils.getPercentWidthSizeBigger(px);
            float h_val = AutoUtils.getPercentHeightSizeBigger(px);
            if(h_val/w_val>1.05f || h_val/w_val<0.95f){
                return  AutoUtils.getPercentHeightSizeBigger(px);
            }else {
                return AutoUtils.getPercentWidthSizeBigger(px);
            }
        }
    }

    public static int getAutoLayoutSize(TypedArray typedArray, int index, int defValue) {
        int pixelValue;
        try {
            pixelValue = typedArray.getDimensionPixelOffset(index, defValue);
        } catch (Exception ignore) {
            return defValue;
        }

        if (!DimenUtils.isPxVal(typedArray.peekValue(index))) {
            return pixelValue;
        }
        return AutoUtils.getPercentWidthSizeBigger(pixelValue);
    }

    public static boolean setViewHeight(View view, int height) {
        boolean changed = false;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.height != height) {
            layoutParams.height = height;
            changed = true;
        }
        return changed;
    }

    public static boolean setViewPaddingTop(View view, int paddingTop) {
        boolean changed = false;
        int top = view.getPaddingTop();
        if (top != paddingTop) {
            int left = view.getPaddingLeft();
            int right = view.getPaddingRight();
            int bottom = view.getPaddingBottom();
            view.setPadding(left, paddingTop, right, bottom);
            view.invalidate();
            return true;
        }
        return changed;
    }


}
