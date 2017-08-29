package com.example.android.cursometertestapp;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.FloatingActionButton.Behavior;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Класс, определяющий поведение FAB. Скрывает FAB при скролле вниз, и показывает при скролле вверх.
 *
 * https://guides.codepath.com/android/Floating-Action-Buttons
 * см. раздел "Using CoordinatorLayout"
 */

public class FABBehaviour extends Behavior {

    public FABBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);

        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {

            // Если просто вызвать метод child.hide() без OnVisibilityChangedListener(), то FAB не
            // появляется снова при прокрутке вверх.
            // Разобраться до конца, почему иак, и как работает:
            // https://issuetracker.google.com/issues/37130108#c5
            // https://stackoverflow.com/questions/41641579/scroll-aware-fab-hides-but-then-does-not-reappear

            child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onShown(fab);
                    fab.setVisibility(View.INVISIBLE);
                }
            });

        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            child.show();
        }
    }

    // Following overriding prevents overlapping of FAB's padding by SnackBar
    // https://stackoverflow.com/questions/42202216/bottom-padding-of-fab-gone-when-snackbar-appears

    @Override
    public boolean getInsetDodgeRect(
            @NonNull CoordinatorLayout parent,
            @NonNull FloatingActionButton child,
            @NonNull Rect rect) {
        return false;
    }
}