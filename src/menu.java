import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class menu extends JPanel {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private static final int BUTTON_X = 460;
    private static final int BUTTON_WIDTH = 360;
    private static final int BUTTON_HEIGHT = 80;
    private static final int[] BUTTON_Y = {350, 450, 550};

    private final String[] buttonNames = { "ИГРАТЬ", "НАСТРОЙКИ", "ВЫХОД" };

    // Пути к файлам на жестком диске
    public final String[] normalFiles = {
            "src:/picture/pause-again.png",
            "src:/picture/honey-gear.png/" ,
            "src:/picture/pause-leave.png"
    };
    public final String[] hoverFiles = {
            "src:/picture/pause-again.png",
            "src:/picture/honey-gear.png/" ,
            "src:/picture/pause-leave.png"
    };

    private BufferedImage background;
    private final BufferedImage[] normalImages = new BufferedImage[3];
    private final BufferedImage[] hoverImages = new BufferedImage[3];
    private int hoveredButton = -1;

    public menu() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);

// Фоновое изображение все еще загружается из ресурсов проекта (внутри JAR/src)
        background = loadResourceImage("/images/background.png");

// Кнопки загружаются напрямую по путях с диска H:
        for (int i = 0; i < buttonNames.length; i++) {
            normalImages[i] = loadDiskImage(normalFiles[i]);
            hoverImages[i] = loadDiskImage(hoverFiles[i]);
        }

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int newHoveredButton = findButton(e.getX(), e.getY());
                if (hoveredButton != newHoveredButton) {
                    hoveredButton = newHoveredButton;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoveredButton = -1;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int button = findButton(e.getX(), e.getY());
                if (button == 0) {
                    System.out.println("Нажата кнопка ИГРАТЬ");
                } else if (button == 1) {
                    System.out.println("Нажата кнопка НАСТРОЙКИ");
                } else if (button == 2) {
                    System.out.println("Нажата кнопка ВЫХОД");
                }
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    // Метод для загрузки картинок из ресурсов (внутри проекта)
    private BufferedImage loadResourceImage(String path) {
        try (InputStream input = getClass().getResourceAsStream(path)) {
            if (input == null) {
                System.out.println("Ресурс не найден: " + path);
                return null;
            }
            return ImageIO.read(input);
        } catch (IOException e) {
            System.out.println("Ошибка загрузки ресурса: " + path);
            return null;
        }
    }

    // НОВЫЙ МЕТОД: для загрузки внешних файлов по абсолютному пути (например, с диска H:)
    private BufferedImage loadDiskImage(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Файл на диске не найден: " + filePath);
                return null;
            }
            return ImageIO.read(file);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла с диска: " + filePath);
            return null;
        }
    }

    private int findButton(int mouseX, int mouseY) {
        for (int i = 0; i < buttonNames.length; i++) {
            boolean insideX = mouseX >= BUTTON_X && mouseX <= BUTTON_X + BUTTON_WIDTH;
            boolean insideY = mouseY >= BUTTON_Y[i] && mouseY <= BUTTON_Y[i] + BUTTON_HEIGHT;
            if (insideX && insideY) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

// Отрисовка фона
        if (background != null) {
            g2.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
        } else {
            g2.setColor(new Color(5, 12, 32));
            g2.fillRect(0, 0, WIDTH, HEIGHT);

            g2.setColor(new Color(0, 100, 120));
            for (int x = 0; x < WIDTH; x += 80) { g2.drawLine(x, 0, x, HEIGHT); }
            for (int y = 0; y < HEIGHT; y += 80) { g2.drawLine(0, y, WIDTH, y); }

            g2.setColor(new Color(0, 255, 180, 35));
            g2.fillOval(500, 60, 280, 280);
            g2.setColor(new Color(0, 255, 180, 100));
            g2.fillOval(560, 120, 160, 160);
        }

// Отрисовка кнопок
        for (int i = 0; i < buttonNames.length; i++) {
            BufferedImage image;
            if (hoveredButton == i && hoverImages[i] != null) {
                image = hoverImages[i];
            } else {
                image = normalImages[i];
            }

            if (image != null) {
                g2.drawImage(image, BUTTON_X, BUTTON_Y[i], BUTTON_WIDTH, BUTTON_HEIGHT, null);
            } else {
// Если картинка не нашлась, сработает красивый стандартный текст (Fallback)
                drawFallbackButton(g2, i, hoveredButton == i);
            }
        }

        g2.dispose();
    }

    private void drawFallbackButton(Graphics2D g2, int index, boolean hovered) {
        Color borderColor = hovered ? new Color(100, 255, 100) : new Color(0, 230, 100);

        g2.setColor(new Color(5, 20, 35, 220));
        g2.fillRoundRect(BUTTON_X, BUTTON_Y[index], BUTTON_WIDTH, BUTTON_HEIGHT, 12, 12);

        g2.setColor(borderColor);
        g2.drawRoundRect(BUTTON_X, BUTTON_Y[index], BUTTON_WIDTH, BUTTON_HEIGHT, 12, 12);

        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 36));
        int textWidth = g2.getFontMetrics().stringWidth(buttonNames[index]);
        int textX = BUTTON_X + (BUTTON_WIDTH - textWidth) / 2;
        int textY = BUTTON_Y[index] + (BUTTON_HEIGHT + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;

        g2.setColor(Color.WHITE);
        g2.drawString(buttonNames[index], textX, textY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("att4k.On.v1r#s — Tower Defense");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setContentPane(new menu());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

