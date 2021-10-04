package org.hoi.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DDS {
    public static BufferedImage read32 (File file) throws IOException {
        DDSReader reader = new DDSReader(file);

        reader.skip(12);
        int height = reader.nextInt();
        int width = reader.nextInt();

        int bytes = width * height * 4;
        reader.skip(108);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i=0;i<bytes;i++) {
            int col = i / width;
            int row = i % width;

            try {
                image.setRGB(row, col, reader.nextInt());
            } catch (Exception e) {
                break;
            }
        }

        return image;
    }

    private static class DDSReader {
        private FileInputStream fis;

        public DDSReader (File file) throws FileNotFoundException {
            this.fis = new FileInputStream(file);
        }

        public void skip (int n) throws IOException {
            fis.skipNBytes(n);
        }

        public byte next () throws IOException {
            return (byte) fis.read();
        }

        public byte[] next (int n) throws IOException {
            return fis.readNBytes(n);
        }

        public short nextShort () throws IOException {
            byte[] bytes = next(2);
            return (short) ((bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8));
        }

        public int nextInt () throws IOException {
            byte[] bytes = next(4);
            return (bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8) | ((bytes[2] & 0xFF) << 16) | ((bytes[3] & 0xFF) << 24);
        }
    }
}
