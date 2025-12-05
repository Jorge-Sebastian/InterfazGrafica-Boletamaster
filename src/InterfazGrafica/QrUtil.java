package InterfazGrafica;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * Genera QRs dinámicos intentando usar ZXing si está en el classpath.
 * Si no encuentra la librería, devuelve un QR placeholder (no escaneable)
 * para que la UI siga funcionando.
 */
public final class QrUtil {

    private QrUtil() {
    }

    public static BufferedImage generateQrImage(String data, int size) {
        BufferedImage viaZxing = tryZxing(data, size);
        if (viaZxing != null) return viaZxing;
        return generatePlaceholder(data, size);
    }

    @SuppressWarnings("unchecked")
    private static BufferedImage tryZxing(String data, int size) {
        try {
            Class<?> writerClass = Class.forName("com.google.zxing.qrcode.QRCodeWriter");
            Object writer = writerClass.getDeclaredConstructor().newInstance();
            Class<Enum> barcodeFormatClass = (Class<Enum>) Class.forName("com.google.zxing.BarcodeFormat");
            Enum<?> qrEnum = Enum.valueOf(barcodeFormatClass, "QR_CODE");

            Method encode = writerClass.getMethod("encode", String.class, barcodeFormatClass, int.class, int.class);
            Object matrix = encode.invoke(writer, data, qrEnum, size, size);

            Class<?> bitMatrixClass = Class.forName("com.google.zxing.common.BitMatrix");
            Method getWidth = bitMatrixClass.getMethod("getWidth");
            Method getHeight = bitMatrixClass.getMethod("getHeight");
            Method get = bitMatrixClass.getMethod("get", int.class, int.class);

            int w = (int) getWidth.invoke(matrix);
            int h = (int) getHeight.invoke(matrix);

            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    boolean black = (boolean) get.invoke(matrix, x, y);
                    img.setRGB(x, y, black ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            return img;
        } catch (Exception e) {
            return null;
        }
    }

    private static BufferedImage generatePlaceholder(String data, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);
        Random rnd = new Random(data.hashCode());
        int block = Math.max(3, size / 40);
        for (int x = 0; x < size; x += block) {
            for (int y = 0; y < size; y += block) {
                if (rnd.nextBoolean()) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x, y, block, block);
            }
        }
        // marco
        g.setColor(new Color(31, 59, 115));
        g.drawRect(0, 0, size - 1, size - 1);
        g.dispose();
        return img;
    }
}
