package com.x1unix.cashlytics.ui.common;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.x1unix.cashlytics.R;


/**
 * Poster view holder
 */
public class PosterViewHolder {
    private TextView label;
    private TextView description;
    private Button actionButton;
    private ImageView icon;
    private View root;

    /**
     * Constructs viewholder from an included poster
     * @param rootView Poster include
     */
    public PosterViewHolder(View rootView) {
        root = rootView;
        label = (TextView) rootView.findViewById(R.id.posterTitle);
        description = (TextView) rootView.findViewById(R.id.posterDescription);
        actionButton = (Button) rootView.findViewById(R.id.posterAction);
        icon = (ImageView) rootView.findViewById(R.id.posterLogo);
    }

    /**
     * Sets poster's title
     * @param title Title
     */
    public void setTitle(String title) {
        label.setText(title);
    }

    /**
     * Sets poster's title
     * @param stringId String resource ID
     */
    public void setTitle(int stringId) {
        label.setText(stringId);
    }

    /**
     * Set's poster's description
     * @param text Description
     */
    public void setText(String text) {
        description.setText(text);
    }

    /**
     * Set's poster's description
     * @param stringId String resource ID
     */
    public void setText(int stringId) {
        description.setText(stringId);
    }

    /**
     * Sets poster's icon
     * @param iconId Icon resource ID
     */
    public void setIcon(int iconId) {
        icon.setImageResource(iconId);
    }

    /**
     * Sets action button text
     * @param stringId String ID
     */
    public void setButtonTitle(int stringId) {
        actionButton.setText(stringId);
    }

    /**
     * Sets action button visibility state
     * @param visible Is visible
     */
    public void displayActionButton(boolean visible) {
        setViewVisibilityState(actionButton, visible);
    }

    /**
     * Sets action button click listener
     * @param listener
     */
    public void setActionListener(View.OnClickListener listener) {
        actionButton.setOnClickListener(listener);
    }

    private void setViewVisibilityState(View w, boolean visible) {
        w.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Hides poster
     */
    public void hide() {
        setViewVisibilityState(root, false);
    }

    /**
     * Shows poster
     */
    public void show() {
        setViewVisibilityState(root, true);
    }

    public void finalize() {
        dispose();
    }

    /**
     * Destructs poster class
     */
    public void dispose() {
        label = null;
        description = null;
        actionButton = null;
        icon = null;
        root = null;
    }
}