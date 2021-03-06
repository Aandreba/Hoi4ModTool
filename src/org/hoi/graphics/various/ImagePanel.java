package org.hoi.graphics.various;

import org.hoi.various.img.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private BufferedImage transformed;

    private float zoom;
    private float dx, dy;
    private FillStyle style;

    private int pastWidth, pastHeight;

    public ImagePanel(BufferedImage image) {
        this(image, FillStyle.RESIZE);
    }

    public ImagePanel(BufferedImage image, FillStyle style) {
        this.style = style;
        this.zoom = 1f;
        this.dx = 0f;
        this.dy = 0f;
        this.setImage(image);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        transformImage();
    }

    public int getImageWidth () {
        return image.getWidth();
    }

    public int getImageHeight () {
        return image.getHeight();
    }

    public int getTransformedWidth () {
        return transformed.getWidth();
    }

    public int getTransformedHeight () {
        return transformed.getHeight();
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom (float zoom) {
        this.zoom = Math.max(1f, Math.min(10, zoom));
        transformImage();
    }

    public void addZoom (float zoom) {
        setZoom(this.zoom + zoom);
    }

    public float getDeltaX () {
        return dx;
    }

    public float getDeltaY () {
        return dy;
    }

    public Point2D.Float getDelta () {
        return new Point2D.Float(dx, dy);
    }

    public void setDelta (float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
        transformImage();
    }

    public void addDelta (float dx, float dy) {
        setDelta(this.dx + dx, this.dy + dy);
    }

    private void transformImage () {
        BufferedImage transformed = ImageUtils.transform(this.image, this.zoom, (int) this.dx, (int) this.dy);

        if (this.transformed == null && transformed != null) {
            this.pastWidth = transformed.getWidth();
            this.pastHeight = transformed.getHeight();
        } else if (this.transformed != null) {
            this.pastWidth = this.transformed.getWidth();
            this.pastHeight = this.transformed.getHeight();
        }

        this.transformed = transformed;
        updateUI();
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    @Override
    protected void paintComponent (Graphics g) {
        g.setColor(new Color(0, true));
        g.fillRect(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);

        if (image != null) {
            if (this.style == FillStyle.OVERFLOW) {
                g.drawImage(this.transformed, 0, 0, this);
            } else if (this.style == FillStyle.RESIZE) {
                g.drawImage(this.transformed, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public enum FillStyle {
        OVERFLOW,
        RESIZE
    }
}
